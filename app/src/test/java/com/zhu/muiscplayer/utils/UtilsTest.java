package com.zhu.muiscplayer.utils;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/29
 * Time: 20:03
 * Desc:com.zhu.muiscplayer.utils
 */
public class UtilsTest {


    @Test
    public void formatDuration() throws Exception {
        String songDuration = TimeUtils.formatDuration(220000);
        Assert.assertEquals("03:40" , songDuration);
    }



}