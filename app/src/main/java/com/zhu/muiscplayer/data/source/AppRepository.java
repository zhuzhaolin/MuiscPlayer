package com.zhu.muiscplayer.data.source;

import com.zhu.muiscplayer.Injection;
import com.zhu.muiscplayer.data.model.Folder;
import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.data.source.db.LiteOrmHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/20
 * Time: 21:19
 * Desc:com.zhu.muiscplayer.data.source
 */

 public class AppRepository implements AppContract{


    private static volatile AppRepository sInstance;

    private AppLocalDataSource mLocalDataSource;

    private List<PlayList> mCachedPlayLists;

    private AppRepository(){
        mLocalDataSource = new AppLocalDataSource(Injection.provideContext() , LiteOrmHelper.getInstance());
    }

    public static AppRepository getInstance() {
        if (sInstance == null) {
            synchronized (AppRepository.class) {
                if (sInstance == null) {
                    sInstance = new AppRepository();
                }
            }
        }
        return sInstance;
    }

    @Override
    public Observable<List<PlayList>> playLists() {
        return mLocalDataSource.playLists()
                .doOnNext(new Action1<List<PlayList>>() {
                    @Override
                    public void call(List<PlayList> playLists) {
                        mCachedPlayLists = playLists;
                    }
                });
    }

    @Override
    public List<PlayList> cachedPlayLists() {
        if (mCachedPlayLists == null) {
            return new ArrayList<>();
        }
        return mCachedPlayLists;
    }

    @Override
    public Observable<PlayList> create(PlayList playList) {
        return mLocalDataSource.create(playList);
    }

    @Override
    public Observable<PlayList> update(PlayList playList) {
        return mLocalDataSource.update(playList);
    }

    @Override
    public Observable<PlayList> delete(PlayList playList) {
        return mLocalDataSource.delete(playList);
    }

    // Folders
    @Override
    public Observable<List<Folder>> folders() {
        return mLocalDataSource.folders();
    }

    @Override
    public Observable<Folder> create(Folder folder) {
        return mLocalDataSource.create(folder);
    }

    @Override
    public Observable<List<Folder>> create(List<Folder> folders) {
        return mLocalDataSource.create(folders);
    }

    @Override
    public Observable<Folder> update(Folder folder) {
        return mLocalDataSource.update(folder);
    }

    @Override
    public Observable<Folder> delete(Folder folder) {
        return mLocalDataSource.delete(folder);
    }

    @Override
    public Observable<List<Song>> inSert(List<Song> songs) {
        return mLocalDataSource.inSert(songs);
    }

    @Override
    public Observable<Song> update(Song song) {
        return mLocalDataSource.update(song);
    }

    @Override
    public Observable<Song> setSongAsFavorite(Song song, boolean favorite) {
        return mLocalDataSource.setSongAsFavorite(song ,favorite);
    }
}
