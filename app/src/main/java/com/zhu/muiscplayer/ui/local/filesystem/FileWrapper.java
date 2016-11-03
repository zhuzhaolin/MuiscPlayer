package com.zhu.muiscplayer.ui.local.filesystem;

import java.io.File;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/30
 * Time: 16:54
 * Desc:com.zhu.muiscplayer.ui.local.filesystem
 */

public class FileWrapper {

    public File file;

    public boolean selected;

    public FileWrapper() {
    }

    public FileWrapper(File file) {
        this.file = file;
    }

    public FileWrapper(File file, boolean selected) {
        this.file = file;
        this.selected = selected;
    }
}
