package com.zhu.muiscplayer.ui.local.folder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.RxBus;
import com.zhu.muiscplayer.data.model.Folder;
import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.data.source.AppRepository;
import com.zhu.muiscplayer.event.AddFolderEvent;
import com.zhu.muiscplayer.event.PlayListCreatedEvent;
import com.zhu.muiscplayer.ui.base.BaseFragment;
import com.zhu.muiscplayer.ui.base.adapter.OnItemClickListener;
import com.zhu.muiscplayer.ui.common.DefaultDividerDecoration;
import com.zhu.muiscplayer.ui.details.PlayListDetailsActivity;
import com.zhu.muiscplayer.ui.local.filesystem.FileSystemActivity;
import com.zhu.muiscplayer.ui.playlist.AddToPlayListDialogFragment;
import com.zhu.muiscplayer.ui.playlist.EditPlayListDialogFragment;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/21
 * Time: 17:22
 * Desc:com.zhu.muiscplayer.ui.local.folder
 */

public class FolderFragmet extends BaseFragment implements
 FolderContract.View , FolderAdapter.AddFolderCallback{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private FolderAdapter mAdapter;
    private int mUpdateIndex , mDeleteIndex;

    FolderContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_added_folders , container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this , view);

        mAdapter = new FolderAdapter(getActivity() , null);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Folder folder = mAdapter.getItem(position);
                startActivity(PlayListDetailsActivity.launchIntentForPlayList(getActivity() , folder));
            }
        });
        mAdapter.setAddFolderCallback(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DefaultDividerDecoration());
        new FolderPresenter(AppRepository.getInstance() , this).subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unSubscribe();
    }

    @Override
    protected Subscription subscribeEvents() {
        return RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof AddFolderEvent) {
                            onAddFolders((AddFolderEvent) o);
                        }
                    }
                })
                .subscribe(RxBus.defaultSubscriber());
    }

    private void onAddFolders(AddFolderEvent event) {
        final List<File> folders = event.folders;
        final List<Folder> existedFodlers = mAdapter.getData();
        mPresenter.addFolders(folders , existedFodlers);
    }

    @Override
    public void onAction(View actionView, final int position) {
        final Folder folder = mAdapter.getItem(position);
        PopupMenu actionMenu = new PopupMenu(getActivity(), actionView , Gravity.END|Gravity.BOTTOM);
        actionMenu.inflate(R.menu.folders_action);
        actionMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_add_to_play_list:
                        new AddToPlayListDialogFragment()
                                .setCallback(new AddToPlayListDialogFragment.Callback() {
                                    @Override
                                    public void onPlayListSelected(PlayList playList) {
                                        mPresenter.addFolderToPlayList(folder, playList);
                                    }
                                })
                                .show(getFragmentManager().beginTransaction(), "AddToPlayList");
                        break;

                    case R.id.menu_item_create_play_list:
                        PlayList playList = PlayList.fromFolder(folder);
                        EditPlayListDialogFragment.editPlayList(playList)
                                .setCallback(new EditPlayListDialogFragment.Callback() {
                                    @Override
                                    public void onCreated(PlayList playList) {

                                    }

                                    @Override
                                    public void onEdited(PlayList playList) {
                                        mPresenter.createPlayList(playList);
                                    }
                                })
                                .show(getFragmentManager().beginTransaction(), "CreatePlayList");
                        break;

                    case R.id.menu_item_refresh:
                        mUpdateIndex = position;
                        mPresenter.refreshFolder(folder);
                        break;

                    case R.id.menu_item_delete:
                        mDeleteIndex = position;
                        mPresenter.deleteFolder(folder);
                        break;
                }
                return true;
            }
        });

        actionMenu.show();
    }

    @Override
    public void onAddFolder() {
        startActivity(new Intent(getActivity(), FileSystemActivity.class));
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void handleError(Throwable error) {
        Toast.makeText(getActivity() , error.getMessage() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFoldersLoaded(List<Folder> folders) {
        mAdapter.setData(folders);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFoldersAdded(List<Folder> allNewFolder) {
        int newItemCount = allNewFolder.size() -
                (mAdapter.getData() == null ? 0 : mAdapter.getData().size());

        mAdapter.setData(allNewFolder);
        mAdapter.notifyDataSetChanged();
        mAdapter.updateFooterView();
        if (newItemCount > 0) {
            String toast = getResources().getQuantityString(
                    R.plurals.mp_play_list_items_formatter ,
                    newItemCount ,
                    newItemCount);
            Toast.makeText(getActivity() , toast , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFolderUpdated(Folder folder) {
        mAdapter.getData().set(mUpdateIndex , folder);
        mAdapter.notifyItemChanged(mUpdateIndex);
    }

    @Override
    public void onFolderDeleted(Folder folder) {
        mAdapter.getData().remove(mDeleteIndex);
        mAdapter.notifyItemRemoved(mDeleteIndex);
        mAdapter.updateFooterView();
    }

    @Override
    public void onPlayListCreated(PlayList playList) {
        RxBus.getInstance().post(new PlayListCreatedEvent(playList));
        Toast.makeText(getActivity(), getString(R.string.mp_play_list_created, playList.getName()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(FolderContract.Presenter presenter) {
       mPresenter = presenter;
    }
}
