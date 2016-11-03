package com.zhu.muiscplayer.ui.local.filesystem;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/30
 * Time: 17:05
 * Desc:com.zhu.muiscplayer.ui.local.filesystem
 */

public class SystemFileFilter implements FilenameFilter {

    public static SystemFileFilter DEFAULT_INSTANCE = new SystemFileFilter();

    @Override
    public boolean accept(File dir, String name) {
        return !name.startsWith(".");
    }
}
