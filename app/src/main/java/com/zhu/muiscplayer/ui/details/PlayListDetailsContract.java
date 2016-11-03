package com.zhu.muiscplayer.ui.details;

import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.ui.base.BasePresenter;
import com.zhu.muiscplayer.ui.base.BaseView;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/11/1
 * Time: 21:07
 * Desc:com.zhu.muiscplayer.ui.details
 */

public interface PlayListDetailsContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void handleError(Throwable e);

        void onSongDeleted(Song song);
    }

    interface Presenter extends BasePresenter {
        void addSongToPlayList(Song song, PlayList playList);

        void delete(Song song, PlayList playList);
    }



}
