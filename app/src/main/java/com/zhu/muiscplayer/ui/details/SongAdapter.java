package com.zhu.muiscplayer.ui.details;

import android.content.Context;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.ui.common.AbstractSummaryAdapter;

import java.util.IllegalFormatCodePointException;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/11/1
 * Time: 20:51
 * Desc:com.zhu.muiscplayer.ui.details
 */

public class SongAdapter extends AbstractSummaryAdapter<Song, SongItemView> {

    private Context mContext;
    private ActionCallback mCallback;



    public SongAdapter(Context context, List<Song> data) {
        super(context, data);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);
        if (holder.itemView instanceof SongItemView) {
            SongItemView itemView = (SongItemView) holder.itemView;
            itemView.buttonAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (mCallback != null) {
                        mCallback.onAction(v , position);
                    }
                }
            });
        }
        return holder;
    }

    @Override
    protected String getEndSummaryText(int dataCount) {
        return mContext.getString(R.string.mp_play_list_details_footer_end_summary_formatter, dataCount);
    }

    @Override
    protected SongItemView createView(Context context) {
        return new SongItemView(context);
    }

    //Callback
    public void setActionCallback(ActionCallback callback) {
        mCallback = callback;
    }

    interface ActionCallback{
        void onAction(View actionView, int postion);
    }
}
