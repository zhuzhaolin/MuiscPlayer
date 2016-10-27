package com.zhu.muiscplayer.ui.local.all;




import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.data.source.AppRepository;
import com.zhu.muiscplayer.utils.FileUtils;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/21
 * Time: 15:33
 * Desc:com.zhu.muiscplayer.ui.local.all
 */

public class LocalMusicPresenter implements LocalMusicContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "LocalMusicPresenter";

    private static final int URL_LOAD_LOCAL_MUSIC = 1;

    private static final Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    private static final String WHERE = MediaStore.Audio.Media.IS_MUSIC + "=1 AND "
            + MediaStore.Audio.Media.SIZE + ">0";

    private static final String ORDER_BY = MediaStore.Audio.Media.DISPLAY_NAME + "ASC";

    private static String[] PROJECTIONS ={
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.IS_RINGTONE ,
            MediaStore.Audio.Media.IS_MUSIC ,
            MediaStore.Audio.Media.IS_NOTIFICATION ,
            MediaStore.Audio.Media.DURATION ,
            MediaStore.Audio.Media.SIZE
    };

    private LocalMusicContract.View mView;
    private AppRepository mRepository;
    private CompositeSubscription mSubscription;

    public LocalMusicPresenter(AppRepository repository, LocalMusicContract.View view) {
        mRepository = repository;
        mSubscription = new CompositeSubscription();
        mView = view;
        mView.setPresenter(this);

    }
    @Override
    public void subscribe() {
      loadLocalMusic();
    }

    @Override
    public void unSubscribe() {
        mView = null;
        mSubscription.clear();
    }

    @Override
    public void loadLocalMusic() {
        mView.showProgress();
        mView.getLoaderManager().initLoader(URL_LOAD_LOCAL_MUSIC , null , this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        return new CursorLoader(
                mView.getContext() ,
                MEDIA_URI ,
                PROJECTIONS ,
                WHERE ,
                null,
                MediaStore.Audio.Media.DISPLAY_NAME
        );
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (loader.getId() != URL_LOAD_LOCAL_MUSIC) return;

        Subscription subscription = Observable.just(cursor)
                .flatMap(new Func1<Cursor, Observable<List<Song>>>() {
                    @Override
                    public Observable<List<Song>> call(Cursor cursor) {
                        List<Song> songs = new ArrayList<>();
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                Song song = cursorToMusic(cursor);
                                songs.add(song);
                            }while (cursor.moveToNext());

                        }
                        //关闭cursor
                        cursor.close();

                        return mRepository.inSert(songs);
                    }
                })
                .doOnNext(new Action1<List<Song>>() {
                    @Override
                    public void call(List<Song> songs) {
                        final Collator collator = Collator.getInstance(java.util.Locale.CHINA);
                         Collections.sort(songs, new Comparator<Song>() {
                            @Override
                            public int compare(Song left , Song right) {

                                if (left.getDisplayName() == null || right.getDisplayName() == null){
                                    return -1;
                                }
                                return collator.getCollationKey(left.getDisplayName())
                                        .compareTo(collator.getCollationKey(right.getDisplayName()));
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Song>>() {

                    @Override
                    public void onStart() {
                        mView.showProgress();
                    }

                    @Override
                    public void onCompleted() {
                        mView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                      mView.hideProgress();
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(List<Song> songs) {
                        mView.onLocalMusicLoaded(songs);
                        mView.emptyView(songs.isEmpty());
                    }
                });
        mSubscription.add(subscription);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private Song cursorToMusic(Cursor cursor) {
       Song song= getMusicDataFromMediaMetadataRetriever(cursor);
        if (song != null) {
            return song;
        } else {
            return getMusicDataFromCursor(cursor);
        }
    }

    private Song getMusicDataFromMediaMetadataRetriever(Cursor cursor){
//        String realPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        Song song = FileUtils.parseMuiscPath(cursor);
        return  song;
    }

    private Song getMusicDataFromCursor(Cursor cursor){
        Song song = new Song();
        song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
        String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
        if (displayName.endsWith(".mp3")) {
            displayName = displayName.substring(0 , displayName.length() - 4);
        }

        song.setDisplayName(displayName);
        song.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
        song.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
        song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
        song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
        song.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
        return song;
    }

}
