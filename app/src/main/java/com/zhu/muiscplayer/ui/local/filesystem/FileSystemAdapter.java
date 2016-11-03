package com.zhu.muiscplayer.ui.local.filesystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhu.muiscplayer.data.model.Folder;
import com.zhu.muiscplayer.ui.base.adapter.ListAdapter;

import java.io.File;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/30
 * Time: 16:53
 * Desc:com.zhu.muiscplayer.ui.local.filesystem
 */

public class FileSystemAdapter extends ListAdapter<FileWrapper , FileItemView>{



    public FileSystemAdapter(Context context, List<FileWrapper> data) {
        super(context, data);
    }

    @Override
    protected FileItemView createView(Context context) {
        return new FileItemView(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent , viewType);
        if (holder.itemView instanceof FileItemView) {
            FileItemView fileItemView = (FileItemView) holder.itemView;
            fileItemView.mImageViewFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        getItemLongClickListener().onItemClick(position);
                    }
                }
            });
        }
        return holder;
    }

    public void clearSelections() {
        if (getData().isEmpty()) return;

        for (FileWrapper wrapper : getData()) {
            wrapper.selected = false;
        }
    }
}
