package com.zhu.muiscplayer.event;

import com.zhu.muiscplayer.data.model.Song;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/25
 * Time: 21:02
 * Desc:com.zhu.muiscplayer.event
 */

public class FavoriteChangeEvent {

    public Song mSong;

    public FavoriteChangeEvent(Song song) {
        this.mSong = song;
    }

}
