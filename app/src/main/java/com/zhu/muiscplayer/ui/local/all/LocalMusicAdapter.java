package com.zhu.muiscplayer.ui.local.all;

import android.content.Context;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.ui.widget.RecyclerViewFastScroller;
import com.zhu.muiscplayer.ui.common.AbstractSummaryAdapter;

import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/17
 * Time: 22:29
 * Desc:com.zhu.muiscplayer.ui.local.all
 */

public class LocalMusicAdapter extends AbstractSummaryAdapter<Song, LocalMusicItemView>
        implements RecyclerViewFastScroller.BubbleTextGetter {

    private Context mContext;

    public LocalMusicAdapter(Context context, List<Song> data) {
        super(context, data);
         mContext = context;
    }

    @Override
    protected String getEndSummaryText(int dataCount) {
        return mContext.getString(R.string.mp_local_files_music_list_end_summary_formatter , dataCount);
    }

    @Override
    protected LocalMusicItemView createView(Context context) {
        return new LocalMusicItemView(context);
    }

    @Override
    public String getTextToShowInBubble(int position) {
        Song item = getItem(position);
        if (position > 0 && item == null){
            item = getItem(position - 1);
        }
        return item.getDisplayName().substring(0, 1);
    }


}
