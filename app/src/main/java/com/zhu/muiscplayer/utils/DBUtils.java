package com.zhu.muiscplayer.utils;

import android.content.Context;
import android.os.Environment;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.data.model.Folder;
import com.zhu.muiscplayer.data.model.PlayList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/21
 * Time: 10:55
 * Desc:com.zhu.muiscplayer.utils
 */

public class DBUtils {

    public static PlayList generateFavoritePlayList(Context context) {
        PlayList favorite = new PlayList();
        favorite.setFavorite(true);
        favorite.setName(context.getString(R.string.mp_play_list_favorite));
        return favorite;
    }

    public static List<Folder> generateDefaultFolders(){
        List<Folder> defaultFolder = new ArrayList<>(3);
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        defaultFolder.add(FileUtils.folderFromDir(downloadDir));
        defaultFolder.add(FileUtils.folderFromDir(musicDir));
        return defaultFolder;
    }

}
