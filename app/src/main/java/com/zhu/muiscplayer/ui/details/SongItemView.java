package com.zhu.muiscplayer.ui.details;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.ui.base.adapter.IAdapterView;
import com.zhu.muiscplayer.utils.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/11/1
 * Time: 20:52
 * Desc:com.zhu.muiscplayer.ui.details
 */

public class SongItemView extends RelativeLayout implements IAdapterView<Song> {


    @BindView(R.id.text_view_index)
    TextView mTextViewIndex;
    @BindView(R.id.text_view_name)
    TextView mTextViewName;
    @BindView(R.id.text_view_info)
    TextView mTextViewInfo;
    @BindView(R.id.layout_action)
    View buttonAction;

    public SongItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_play_list_details_song, this);
        ButterKnife.bind(this);
    }

    @Override
    public void bind(Song song, int position) {
        mTextViewIndex.setText(String.valueOf(position + 1));
        mTextViewName.setText(song.getDisplayName());
        mTextViewInfo.setText(String.format("%s | %s" ,
                TimeUtils.formatDuration(song.getDuration()) , song.getArtist()));
    }

}
