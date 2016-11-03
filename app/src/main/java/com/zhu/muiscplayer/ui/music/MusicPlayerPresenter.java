package com.zhu.muiscplayer.ui.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.drm.DrmStore;
import android.os.IBinder;

import com.zhu.muiscplayer.RxBus;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.data.source.AppRepository;
import com.zhu.muiscplayer.data.source.PreferenceManager;
import com.zhu.muiscplayer.event.FavoriteChangeEvent;
import com.zhu.muiscplayer.player.PlayMode;
import com.zhu.muiscplayer.player.PlaybackService;
import com.zhu.muiscplayer.ui.local.all.LocalMusicContract;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/25
 * Time: 20:39
 * Desc:com.zhu.muiscplayer.ui.music
 */

public class MusicPlayerPresenter implements MusicPlayerContract.Presenter {

    private Context mContext;
    private MusicPlayerContract.View mView;
    private AppRepository mRepository;
    private CompositeSubscription mSubscriptions;

    private PlaybackService mPlaybackService;
    private boolean mIsServiceBound;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mPlaybackService = ((PlaybackService.LocalBinder) service).getService();
            mView.onPlaybackServiceBound(mPlaybackService);
            mView.onSongUpdated(mPlaybackService.getPlayingSong());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mPlaybackService = null;
            mView.onPlaybackServiceUnbound();
        }
    };

    public MusicPlayerPresenter(Context context , AppRepository repository , MusicPlayerContract.View view) {
        mContext = context;
        mView = view;
        mRepository = repository;
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        bindPlaybackService();
        retrieveLastPlayMode();

        if (mPlaybackService != null && mPlaybackService.isPlaying()) {
            mView.onSongUpdated(mPlaybackService.getPlayingSong());
        }else {
            //-load last play list/folder/song
        }
    }

    @Override
    public void unSubscribe() {
       unbindPlaybackService();
        //Release context reference
        mContext = null;
        mView = null;
        mSubscriptions.clear();
    }

    @Override
    public void retrieveLastPlayMode() {
        PlayMode lastPlayMode = PreferenceManager.lastPlayMode(mContext);
        mView.updatePlayMode(lastPlayMode);
    }

    @Override
    public void setSongAsFavorite(Song song, boolean favorite) {
        Subscription subscription = mRepository.setSongAsFavorite(song ,favorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Song>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                       mView.handleError(e);
                    }

                    @Override
                    public void onNext(Song song) {
                       mView.onSongSetAsFavorite(song);
                        RxBus.getInstance().post(new FavoriteChangeEvent(song));
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void bindPlaybackService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        mContext.bindService(new Intent(mContext , PlaybackService.class)
                , mConnection , Context.BIND_AUTO_CREATE);
        mIsServiceBound = true;
    }

    @Override
    public void unbindPlaybackService() {
        if (mIsServiceBound) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            mIsServiceBound = false;
        }
    }


}
