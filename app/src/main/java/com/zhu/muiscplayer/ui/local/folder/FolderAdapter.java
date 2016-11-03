package com.zhu.muiscplayer.ui.local.folder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.data.model.Folder;
import com.zhu.muiscplayer.ui.base.adapter.IAdapterView;
import com.zhu.muiscplayer.ui.common.AbstractFooterAdapter;

import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/28
 * Time: 21:11
 * Desc:com.zhu.muiscplayer.ui.local.folder
 */

public class FolderAdapter extends AbstractFooterAdapter<Folder , FolderItemView > {

    private Context mContext;

    private View mFooterViwe;

    private TextView mTextViewSummary;

    private AddFolderCallback mAddFolderCallback;

    public FolderAdapter(Context context, List data) {
        super(context, data);
        mContext = context;
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                updateFooterView();
            }
        });
    }


    @Override
    protected FolderItemView createView(Context context) {
        return new FolderItemView(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent , viewType);
        if (holder.itemView instanceof FolderItemView) {
            final FolderItemView itemView = (FolderItemView) holder.itemView;
            itemView.buttonAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = holder.getAdapterPosition();
                    if (mAddFolderCallback != null) {
                        mAddFolderCallback.onAction(itemView.buttonAction , postion);
                    }
                }
            });
        }
        return holder;
    }

    @Override
    protected boolean isFooterEnabled() {
        return true;
    }

    @Override
    protected View createFooterView() {
        if (mFooterViwe == null) {

            mFooterViwe = View.inflate(mContext , R.layout.item_local_folder_footer , null);
            View layoutAddFolder = mFooterViwe.findViewById(R.id.layout_add_folder);
            layoutAddFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAddFolderCallback != null) {
                        mAddFolderCallback.onAddFolder();
                    }
                }
            });
            mTextViewSummary = (TextView) mFooterViwe.findViewById(R.id.text_view_summary);
        }
        updateFooterView();
        return mFooterViwe;
    }

    public void updateFooterView() {
        if (mTextViewSummary == null) return;

        int itemCount = getItemCount() - 1;
        if (itemCount > 1) {
            mTextViewSummary.setVisibility(View.VISIBLE);
            mTextViewSummary.setText(
                    mContext.getString(R.string.mp_local_files_folder_list_end_summary_formatter));
        } else {
            mTextViewSummary.setVisibility(View.GONE);
        }
    }

    //Callback
    public void setAddFolderCallback(AddFolderCallback callback) {
        mAddFolderCallback = callback;
    }


    interface AddFolderCallback {
        void onAction(View actionView , int position);

        void onAddFolder();
    }

}
