package com.zhu.muiscplayer.event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/29
 * Time: 22:46
 * Desc:com.zhu.muiscplayer.event
 */

public class AddFolderEvent {

    public List<File> folders = new ArrayList<>();

    public AddFolderEvent(File file) {
        folders.add(file);
    }

    public AddFolderEvent(List<File> files) {
        if (files != null)
        this.folders = files;
    }
}
