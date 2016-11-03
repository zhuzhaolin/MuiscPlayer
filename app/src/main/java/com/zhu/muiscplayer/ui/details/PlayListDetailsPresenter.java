package com.zhu.muiscplayer.ui.details;

import com.zhu.muiscplayer.RxBus;
import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.data.source.AppRepository;
import com.zhu.muiscplayer.event.PlayListUpdatedEvent;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/11/1
 * Time: 21:19
 * Desc:com.zhu.muiscplayer.ui.details
 */

public class PlayListDetailsPresenter implements PlayListDetailsContract.Presenter {

    private PlayListDetailsContract.View mView;
    private AppRepository mRepository;
    private CompositeSubscription mSubscriptions;

    public PlayListDetailsPresenter(AppRepository repository,  PlayListDetailsContract.View view) {
        mRepository = repository;
        mSubscriptions = new CompositeSubscription();
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void subscribe() {
        //Notting to do
    }

    @Override
    public void unSubscribe() {
         mView = null;
        mSubscriptions.clear();
    }

    @Override
    public void addSongToPlayList(Song song, PlayList playList) {
        if (playList.isFavorite()) {
            song.setFavorite(true);
        }
        playList.addSong(song , 0);

        Subscription subscription = mRepository.update(playList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PlayList>() {

                    @Override
                    public void onStart() {
                        mView.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                         mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        mView.handleError(e);
                    }

                    @Override
                    public void onNext(PlayList playList) {
                        RxBus.getInstance().post(new PlayListUpdatedEvent(playList));
                    }
                });

        mSubscriptions.add(subscription);

    }

    @Override
    public void delete(final Song song, PlayList playList) {
        playList.removeSong(song);
        Subscription subscription = mRepository.update(playList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PlayList>() {

                    @Override
                    public void onStart() {
                        mView.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        mView.handleError(e);
                    }

                    @Override
                    public void onNext(PlayList playList) {
                       mView.onSongDeleted(song);
                        RxBus.getInstance().post(new PlayListUpdatedEvent(playList));
                    }
                });
        mSubscriptions.add(subscription);
    }
}
