package com.zhu.muiscplayer.utils;

import com.orhanobut.logger.Logger;


/**
 * Created by Administrator on 2016/6/30.
 */
public class LoggerUtil {

    private static final boolean LOG_DEBUG = true;
    /*    private static final int VERBOSE = 1;

    private static final int DEBUG = 2;

    private static final int INFO = 3;

    private static final int WARN = 4;

    private static final int ERROR = 5;

    private static final int NOTHING = 6;

    // TODO release >>> NOTHING
    private static final int LEVEL = NOTHING;*/

    public static void v(String tag, String msg , Object...objects) {
        if (LOG_DEBUG) {
            loggerInit();
            Logger.t(tag).v(msg, objects);
        }
    }

    public static void d(String tag, String msg , Object...objects) {
        if (LOG_DEBUG) {
            loggerInit();
            Logger.t(tag).d(msg, objects);
        }
    }

    public static void i(String tag, String msg , Object...objects) {
        if (LOG_DEBUG) {
            loggerInit();
            Logger.t(tag).i(msg, objects);
        }
    }

    public static void w(String tag, String msg , Object...objects) {
        if (LOG_DEBUG) {
            loggerInit();
            Logger.t(tag).w(msg, objects);
        }
    }

    public static void e(String tag, String msg , Object...objects) {
        if (LOG_DEBUG) {
            loggerInit();
            Logger.t(tag).e(msg, objects);
        }
    }

    public static void loggerInit(){
        Logger.init().hideThreadInfo();
    }



}
