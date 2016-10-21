package com.zhu.muiscplayer.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/16
 * Time: 22:35
 * Desc:com.zhu.muiscplayerlib
 */

public class BaseFragment extends Fragment {

private CompositeSubscription mSubscription;


    protected Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addSubscription(subscribeEvents());
    }

    protected Subscription subscribeEvents(){
        return null;
    }

    protected void addSubscription(Subscription subscription){
        if (subscription == null) return;;
        if (mSubscription == null){
            mSubscription = new CompositeSubscription();
        }
        mSubscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
