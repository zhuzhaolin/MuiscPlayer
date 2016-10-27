package com.zhu.muiscplayer.player;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.data.model.Song;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/23
 * Time: 16:54
 * Desc:com.zhu.muiscplayer.player
 */

public interface IPlayback {

    void setPlayList(PlayList list);

    boolean play();

    boolean play(PlayList list);

    boolean play(PlayList list, int startIndex);

    boolean play(Song song);

    boolean playLast();

    boolean playNext();

    boolean pause();

    boolean isPlaying();

    int getProgress();

    Song getPlayingSong();

    boolean seekTo(int progress);

    void setPlayMode(PlayMode playMode);

    void registerCallback(Callback callback);

    void unregisterCallback(Callback callback);

    void removeCallback();

    void releasePlayer();



    interface Callback{

        void onSwitchLast(@Nullable Song last);

        void onSwitchNext(@Nullable Song next);

        void onComplete(@NonNull Song next);

        void onPlayStatusChanged(boolean isPlaying);
    }

}
