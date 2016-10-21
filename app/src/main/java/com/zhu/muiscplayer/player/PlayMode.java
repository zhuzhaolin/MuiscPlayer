package com.zhu.muiscplayer.player;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/20
 * Time: 21:32
 * Desc:com.zhu.muiscplayer.player
 */

public enum  PlayMode {

    SINGLE ,
    LOOP ,
    LIST,
    SHUFFLE;

    public static PlayMode getDefault(){
        return LOOP;
    }

    public static PlayMode switchNextMode(PlayMode current) {
        if (current == null) return getDefault();

        switch (current){
            case LOOP:
                return LIST;
            case LIST:
                return SHUFFLE;
            case SHUFFLE:
                return SINGLE;
            case SINGLE:
                return LOOP;
        }
        return getDefault();
    }

}
