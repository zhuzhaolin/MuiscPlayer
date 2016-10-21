package com.zhu.muiscplayer.ui.local.all;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.ui.base.adapter.IAdapterView;
import com.zhu.muiscplayer.utils.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/18
 * Time: 12:36
 * Desc:com.zhu.muiscplayer.ui.local.all
 */

public class LocalMusicItemView extends RelativeLayout implements IAdapterView<Song> {

    @BindView(R.id.text_view_name)
    TextView mTextViewName;
    @BindView(R.id.text_view_artist)
    TextView mTextViewArtist;
    @BindView(R.id.text_view_duration)
    TextView mTextViewDuration;

    public LocalMusicItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_local_music, this);
        ButterKnife.bind(this);
    }


    @Override
    public void bind(Song song, int position) {
        mTextViewName.setText(song.getDisplayName());
        mTextViewArtist.setText(song.getArtist());
        mTextViewDuration.setText(TimeUtils.formatDuration(song.getDuration()));
    }
}
