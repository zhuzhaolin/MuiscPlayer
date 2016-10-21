package com.zhu.muiscplayer.ui.base.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/20
 * Time: 21:01
 * Desc:com.zhu.muiscplayer.ui.base.common
 */

public class DefaultDividerDecoration extends RecyclerView.ItemDecoration {

    private static final int DIVIDER_HEIGHT = 1;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = DIVIDER_HEIGHT;

    }
}
