package com.zhu.muiscplayer.utils;

import android.annotation.SuppressLint;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/18
 * Time: 21:54
 * Desc:com.zhu.muiscplayerlib.utils
 */

public class TimeUtils {

    /**
     * Parse the time in milliseconds into String with the format: hh:mm:ss or mm:ss
     *
     * @param duration The time needs to be parsed.
     */
    @SuppressLint("DefaultLocale")
    public static String formatDuration(int duration){
        duration /= 1000; // milliseconds into seconds
        int minute = duration / 60;
        int hour = minute / 60;
        minute %= 60;
        int second = duration % 60;
        if (hour != 0){
            return String.format("%2d:%02d:%02d", hour, minute, second);
        }else {
            return String.format("%02d:%02d", minute, second);
        }
    }

}
