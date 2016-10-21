package com.zhu.muiscplayer;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/20
 * Time: 20:48
 * Desc:com.zhu.muiscplayer
 */

public class RxBus {


    private static final String TAG = "RxBus";
    private static volatile RxBus sInstance;

    public static RxBus getInstance(){
        if (sInstance == null){
            synchronized (RxBus.class){
                if (sInstance == null){
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    private PublishSubject<Object> mEventBus = PublishSubject.create();

    public void post(Object event){
        mEventBus.onNext(event);
    }

    public Observable<Object> toObservable(){
        return mEventBus;
    }

    /**
     * A simple logger for RxBus which can also prevent
     * potential crash(OnErrorNotImplementedException) caused by error in the workflow.
     */
    public static Subscriber<Object> defaultSubscriber(){
        return new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                Log.d(TAG , "Duty off !!!");
            }

            @Override
            public void onError(Throwable e) {
              Log.e(TAG , "What is this? Please solve this as soon as possible!" , e);
            }

            @Override
            public void onNext(Object o) {
               Log.d(TAG , "New event received: " + 0);
            }
        };
    }
}
