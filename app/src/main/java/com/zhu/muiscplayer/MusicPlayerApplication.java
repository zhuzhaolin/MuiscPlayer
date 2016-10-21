package com.zhu.muiscplayer;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/16
 * Time: 17:10
 * Desc:com.zhu.muiscplayer
 */

public class MusicPlayerApplication extends Application {

    private static MusicPlayerApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        //Custom fonts
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Monospace-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static MusicPlayerApplication getInstance(){
        return sInstance;
    }
}
