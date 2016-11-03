package com.zhu.muiscplayer.ui.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhu.muiscplayer.ui.base.adapter.IAdapterView;
import com.zhu.muiscplayer.ui.base.adapter.ListAdapter;

import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/28
 * Time: 20:54
 * Desc:com.zhu.muiscplayer.ui.common
 */

public abstract class AbstractFooterAdapter<T, V extends IAdapterView> extends ListAdapter<T, V> {

    protected static final int VIEW_TYPE_ITEM = 1;
    protected static final int VIEW_TYPE_FOOTER = 2;

    public AbstractFooterAdapter(Context context, List<T> data) {
        super(context, data);
    }

    /**
     * Default footer view is disabled, override in subclass and return true if want to enable it.
     */
    protected boolean isFooterEnabled() {
        return false;
    }

    /**
     * @return Custom footer view, but override {@link #isFooterEnabled} and return true first.
     */
    protected View createFooterView() {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FOOTER) {
            return new RecyclerView.ViewHolder(createFooterView()) {};
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterEnabled() && position == getItemCount() - 1) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        if (isFooterEnabled()) {
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public T getItem(int position) {
        if (getItemViewType(position) == VIEW_TYPE_FOOTER) {
            return null;
        }
        return super.getItem(position);
    }
}
