package com.zhu.muiscplayer.ui.local.all;



import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.ui.base.BasePresenter;
import com.zhu.muiscplayer.ui.base.BaseView;

import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/20
 * Time: 20:20
 * Desc:com.zhu.muiscplayer.ui.local.all
 */

public interface LocalMusicContract {

    interface View extends BaseView<Presenter> {

        LoaderManager getLoaderManager();

        Context getContext();

        void showProgress();

        void hideProgress();

        void emptyView(boolean visible);

        void handleError(Throwable error);

        void onLocalMusicLoaded(List<Song> songs);
    }


    interface Presenter extends BasePresenter{

        void loadLocalMusic();
    }

}
