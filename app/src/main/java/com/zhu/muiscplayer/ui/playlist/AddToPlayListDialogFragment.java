package com.zhu.muiscplayer.ui.playlist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.data.source.AppRepository;
import com.zhu.muiscplayer.ui.base.BaseDialogFragment;
import com.zhu.muiscplayer.ui.base.adapter.ListAdapter;
import com.zhu.muiscplayer.ui.base.adapter.OnItemClickListener;
import com.zhu.muiscplayer.ui.common.DefaultDividerDecoration;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/30
 * Time: 14:25
 * Desc:com.zhu.muiscplayer.ui.playlist
 */

public class AddToPlayListDialogFragment extends BaseDialogFragment implements
        OnItemClickListener, DialogInterface.OnShowListener {

    RecyclerView mRecyclerView;

    CompositeSubscription mSubscription = new CompositeSubscription();

    AddPlayListAdapter mAdapter;

    Callback mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new AddPlayListAdapter(getActivity(), AppRepository.getInstance().cachedPlayLists());
        mAdapter.setOnItemClickListener(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.mp_play_list_dialog_add_to)
                .setView(R.layout.dialog_add_to_play_list)
                .setNegativeButton(R.string.mp_cancel , null)
                .create();
        dialog.setOnShowListener(this);
        return dialog;
    }


    @Override
    public void onDestroyView() {
        mSubscription.clear();
        super.onDestroyView();
    }

    @Override
    public void onShow(DialogInterface dialog) {
        resizeDialogSize();
        if (mRecyclerView == null) {
            mRecyclerView = (RecyclerView)getDialog().findViewById(R.id.recycler_view);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DefaultDividerDecoration());
        }
    }

    @Override
    public void onItemClick(int position) {
        if (mCallback != null) {
            mCallback.onPlayListSelected(mAdapter.getItem(position));
        }
        dismiss();
    }

    public AddToPlayListDialogFragment setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }

    private static class AddPlayListAdapter extends ListAdapter<PlayList, PlayListItemView> {

        public AddPlayListAdapter(Context context, List<PlayList> data) {
            super(context, data);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent , viewType);
            if (holder.itemView instanceof PlayListItemView) {
                PlayListItemView itemView = (PlayListItemView) holder.itemView;
                itemView.buttonAction.setVisibility(View.GONE);
            }
            return holder;
        }

        @Override
        protected PlayListItemView createView(Context context) {
            return new PlayListItemView(context);
        }

    }

   public interface Callback {
        void onPlayListSelected(PlayList playList);
    }
}

