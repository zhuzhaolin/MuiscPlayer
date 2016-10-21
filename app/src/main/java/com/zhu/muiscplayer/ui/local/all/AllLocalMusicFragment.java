package com.zhu.muiscplayer.ui.local.all;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.RxBus;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.data.source.AppRepository;
import com.zhu.muiscplayer.event.PlayListUpdatedEvent;
import com.zhu.muiscplayer.event.PlaySongEvent;
import com.zhu.muiscplayer.ui.widget.RecyclerViewFastScroller;
import com.zhu.muiscplayer.ui.base.BaseFragment;
import com.zhu.muiscplayer.ui.base.adapter.OnItemClickListener;
import com.zhu.muiscplayer.ui.base.common.DefaultDividerDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/17
 * Time: 22:15
 * Desc:com.zhu.muiscplayer.ui.local.all
 */

public class AllLocalMusicFragment extends BaseFragment implements LocalMusicContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fast_scroller)
    RecyclerViewFastScroller mFastScroller;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.text_view_empty)
    View emptyView;

    LocalMusicAdapter mAdapter;
    LocalMusicContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_local_music, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        Log.d("ZHUZHAOLIN" , "ZHUZHAOLIN");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new LocalMusicAdapter(getActivity(), null);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Song song = mAdapter.getItem(position);
                RxBus.getInstance().post(new PlaySongEvent(song));
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DefaultDividerDecoration());

        mFastScroller.setRecyclerView(mRecyclerView);

        new LocalMusicPresenter(AppRepository.getInstance() , this).subscribe();
    }



    @Override
    protected Subscription subscribeEvents() {
        return RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof PlayListUpdatedEvent) {
                            mPresenter.loadLocalMusic();
                        }
                    }
                }).subscribe();
    }

    @Override
    public void showProgress() {
       mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
     mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void emptyView(boolean visible) {
        emptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
        mFastScroller.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void handleError(Throwable error) {
        Toast.makeText(getActivity() , error.getMessage() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocalMusicLoaded(List<Song> songs) {
        mAdapter.setData(songs);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(LocalMusicContract.Presenter presenter) {
        mPresenter = presenter;
    }


}
