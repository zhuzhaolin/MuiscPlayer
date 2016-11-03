package com.zhu.muiscplayer.utils;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/29
 * Time: 22:11
 * Desc:com.zhu.muiscplayer.utils
 */

public class GradientUtils {

    public static GradientDrawable create(@ColorInt int startColor , @ColorInt int endColor, int radius ,
                                      @FloatRange(from = 0f , to = 1f) float centerX ,
                                      @FloatRange(from = 0f , to = 1f) float centerY) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColors(new int[]{
                startColor ,
                endColor});
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gradientDrawable.setGradientRadius(radius);
        gradientDrawable.setGradientCenter(centerX , centerY);
        return gradientDrawable;
    }

}
