package com.zhu.muiscplayer.data.source.db;

import com.litesuits.orm.LiteOrm;
import com.zhu.muiscplayer.BuildConfig;
import com.zhu.muiscplayer.Injection;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/21
 * Time: 15:18
 * Desc:com.zhu.muiscplayer.data.source.db
 */

public class LiteOrmHelper {

    private static final String DB_NAME = "music-player.db";

    private static volatile LiteOrm sInstance;

    private LiteOrmHelper() {

    }

    public static LiteOrm getInstance() {
        if (sInstance == null) {
            synchronized (LiteOrmHelper.class) {
                if (sInstance == null) {
                    sInstance = LiteOrm.newCascadeInstance(Injection.provideContext() , DB_NAME);
                    sInstance.setDebugged(BuildConfig.DEBUG);
                }

            }
        }
        return sInstance;
    }

}
