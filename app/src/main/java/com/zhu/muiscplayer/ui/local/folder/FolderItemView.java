package com.zhu.muiscplayer.ui.local.folder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.data.model.Folder;
import com.zhu.muiscplayer.ui.base.adapter.IAdapterView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/28
 * Time: 21:26
 * Desc:com.zhu.muiscplayer.ui.local.folder
 */

public class FolderItemView extends RelativeLayout implements IAdapterView<Folder> {

    @BindView(R.id.text_view_name)
    TextView mTextViewName;
    @BindView(R.id.text_view_info)
    TextView mTextViewInfo;
    @BindView(R.id.layout_action)
    View buttonAction;

    public FolderItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_added_folder, this);
        ButterKnife.bind(this);
    }

    @Override
    public void bind(Folder folder, int position) {
        mTextViewName.setText(folder.getName());
        mTextViewInfo.setText(getContext().getString(
                R.string.mp_local_files_folder_list_item_info_formatter ,
                folder.getNumOfSongs() ,
                folder.getPath()
        ));
    }
}
