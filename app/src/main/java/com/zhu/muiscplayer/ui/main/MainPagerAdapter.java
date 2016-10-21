package com.zhu.muiscplayer.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zhu.muiscplayer.ui.base.BaseFragment;


/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/16
 * Time: 22:22
 * Desc:com.zhu.muiscplayer.ui.main
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles;
    private BaseFragment[] mFragments;

    public MainPagerAdapter(FragmentManager fm, String[] titles , BaseFragment[] fragments ) {
        super(fm);
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mTitles==null ? 0:mTitles.length;
    }
}
