package com.zhu.muiscplayer.ui.music;

import android.support.annotation.Nullable;

import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.player.PlayMode;
import com.zhu.muiscplayer.player.PlaybackService;
import com.zhu.muiscplayer.ui.base.BasePresenter;
import com.zhu.muiscplayer.ui.base.BaseView;
import com.zhu.muiscplayer.ui.local.all.LocalMusicContract;


/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/23
 * Time: 16:50
 * Desc:com.zhu.muiscplayer.ui.music
 */

public interface MusicPlayerContract {

    interface View extends BaseView<Presenter>{

        void handleError(Throwable error);

        void onPlaybackServiceBound(PlaybackService service);

        void onPlaybackServiceUnbound();

        void onSongSetAsFavorite(@Nullable Song song);

        void onSongUpdated(@Nullable Song song);

        void updatePlayMode(PlayMode playMode);

        void updatePlayToggle(boolean play);

        void updateFavoriteToggle(boolean favorite);

    }

    interface Presenter extends BasePresenter {

        void retrieveLastPlayMode();

        void setSongAsFavorite(Song song , boolean favorite);

        void bindPlaybackService();

        void unbindPlaybackService();
    }


}
