package com.zhu.muiscplayer.ui.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/17
 * Time: 22:36
 * Desc:com.zhu.muiscplayer.ui.base.adapter
 */

public abstract class ListAdapter<T, V extends IAdapterView> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<T> mData;

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    private int mLastItemClickPosition = RecyclerView.NO_POSITION;

    public ListAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    protected  abstract V createView(Context context);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View itemView = (View) createView(mContext);
        final RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(itemView) {};

        if (mItemClickListener != null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mLastItemClickPosition = position;
                        mItemClickListener.onItemClick(position);
                    }
                }
            });
        }
        if (mItemClickListener != null){
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mItemLongClickListener.onItemClick(position);
                    }
                    return false;
                }
            });
        }

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IAdapterView itemView = (V) holder.itemView;
        itemView.bind(getItem(position) , position);
    }


    @Override
    public int getItemCount() {
        if (mData == null){
            return 0;
        }
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getData(){
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public void addData(List<T> data){
        if (mData == null){
            mData = data;
        }else {
            mData.addAll(data);
        }
    }

    public T getItem(int position){
        return mData.get(position);
    }

    public void clear(){
        if (mData != null){
            mData.clear();
        }
    }


    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public OnItemLongClickListener getItemLongClickListener() {
        return mItemLongClickListener;
    }
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }
}
