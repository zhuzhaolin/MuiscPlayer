package com.zhu.muiscplayer.ui.local.folder;

import com.zhu.muiscplayer.data.model.Folder;
import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.ui.base.BasePresenter;
import com.zhu.muiscplayer.ui.base.BaseView;

import java.io.File;
import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/28
 * Time: 20:39
 * Desc:com.zhu.muiscplayer.ui.local.folder
 */

interface FolderContract {

    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void handleError(Throwable error);

        void onFoldersLoaded(List<Folder> folders);

        void onFoldersAdded(List<Folder> folders);

        void onFolderUpdated(Folder folder);

        void onFolderDeleted(Folder folder);

        void onPlayListCreated(PlayList playList);
    }

    interface Presenter extends BasePresenter {
        void loadFolders();

        void addFolders(List<File> folders, List<Folder> existedFolder);

        void refreshFolder(Folder folder);

        void deleteFolder(Folder folder);

        void createPlayList(PlayList playList);

        void addFolderToPlayList(Folder folder , PlayList playList);
    }



}
