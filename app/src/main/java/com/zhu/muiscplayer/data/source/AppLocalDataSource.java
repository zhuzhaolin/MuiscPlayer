package com.zhu.muiscplayer.data.source;

import android.content.Context;

import android.util.Log;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.zhu.muiscplayer.data.model.Folder;
import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.utils.DBUtils;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/20
 * Time: 22:47
 * Desc:com.zhu.muiscplayer.data.source
 */

public class AppLocalDataSource implements AppContract {

    private static final String TAG = "AppLocalDataSource";

    private Context mContext;
    private LiteOrm mLiteOrm;

    public AppLocalDataSource(Context context, LiteOrm liteOrm) {
        mContext = context;
        mLiteOrm = liteOrm;
    }

    @Override
    public Observable<List<PlayList>> playLists() {
        return Observable.create(new Observable.OnSubscribe<List<PlayList>>() {
            @Override
            public void call(Subscriber<? super List<PlayList>> subscriber) {
                List<PlayList> playLists = mLiteOrm.query(PlayList.class);
                if (playLists.isEmpty()) {
                    PlayList playList = DBUtils.generateFavoritePlayList(mContext);
                    long result = mLiteOrm.save(playList);
                    Log.d(TAG, "Create default playlist(Favorite) with " + (result == 1 ? "success" : "failure"));
                    playLists.add(playList);
                }
                subscriber.onNext(playLists);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public List<PlayList> cachedPlayLists() {
        return null;
    }

    @Override
    public Observable<PlayList> create(final PlayList playList) {
        return Observable.create(new Observable.OnSubscribe<PlayList>() {
            @Override
            public void call(Subscriber<? super PlayList> subscriber) {
                Date now = new Date();
                playList.setCreatedAt(now);
                playList.setUpdatedAt(now);

                long result = mLiteOrm.save(playList);
                if (result > 0) {
                    subscriber.onNext(playList);
                } else {
                    subscriber.onError(new Exception("Create play list failed"));
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<PlayList> update(final PlayList playList) {
        return Observable.create(new Observable.OnSubscribe<PlayList>() {
            @Override
            public void call(Subscriber<? super PlayList> subscriber) {
                playList.setUpdatedAt(new Date());

                long result = mLiteOrm.update(playList);
                if (result > 0) {
                    subscriber.onNext(playList);
                } else {
                    subscriber.onError(new Exception("Update play list failed"));
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<PlayList> delete(final PlayList playList) {
        return Observable.create(new Observable.OnSubscribe<PlayList>() {
            @Override
            public void call(Subscriber<? super PlayList> subscriber) {
                long result = mLiteOrm.delete(playList);
                if (result > 0) {
                    subscriber.onNext(playList);
                }else {
                    subscriber.onError(new Exception("Delete play list failed"));
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<Folder>> folders() {
        return Observable.create(new Observable.OnSubscribe<List<Folder>>() {
            @Override
            public void call(Subscriber<? super List<Folder>> subscriber) {
                if (PreferenceManager.isFirstQueryFolders(mContext)) {
                    List<Folder> defaultFolders = DBUtils.generateDefaultFolders();

                    long result = mLiteOrm.save(defaultFolders);
                    Log.d(TAG, "Create default folders effected " + result + "rows");
                    PreferenceManager.reportFirstQueryFolders(mContext);
                }
                List<Folder> folders = mLiteOrm.query(
                        QueryBuilder.create(Folder.class));
                subscriber.onNext(folders);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Folder> create(final Folder folder) {
        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(Subscriber<? super Folder> subscriber) {
                folder.setCreatedAt(new Date());

                long result = mLiteOrm.save(folder);
                if (result > 0) {
                    subscriber.onNext(folder);
                } else {
                    subscriber.onError(new Exception("Create folder failed"));
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<Folder>> create(final List<Folder> folders) {
        return Observable.create(new Observable.OnSubscribe<List<Folder>>() {
            @Override
            public void call(Subscriber<? super List<Folder>> subscriber) {
                Date now = new Date();
                for (Folder folder : folders) {
                    folder.setCreatedAt(now);
                }

                long result = mLiteOrm.save(folders);
                if (result > 0) {
                    List<Folder> allNewFolders = mLiteOrm.query(
                            QueryBuilder.create(Folder.class).appendOrderAscBy(Folder.COLUMN_NAME));
                    subscriber.onNext(allNewFolders);
                } else {
                    subscriber.onError(new Exception("Create folders failed"));
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Folder> update(final Folder folder) {
        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(Subscriber<? super Folder> subscriber) {
                mLiteOrm.delete(folder);
                long result = mLiteOrm.save(folder);
                if (result > 0){
                    subscriber.onNext(folder);
                }else {
                    subscriber.onError(new Exception("Update folder failed"));
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Folder> delete(final Folder folder) {
        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(Subscriber<? super Folder> subscriber) {
                long result = mLiteOrm.delete(folder);
                if (result > 0){
                    subscriber.onNext(folder);
                }else {
                    subscriber.onError(new Exception("Delete folder failed"));
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<Song>> inSert(final List<Song> songs) {
        return Observable.create(new Observable.OnSubscribe<List<Song>>() {
            @Override
            public void call(Subscriber<? super List<Song>> subscriber) {
                for (Song song : songs){
                    mLiteOrm.insert(song , ConflictAlgorithm.Abort);
                }
                List<Song> allSong = mLiteOrm.query(Song.class);
                subscriber.onNext(allSong);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Song> update(final Song song) {
        return Observable.create(new Observable.OnSubscribe<Song>() {
            @Override
            public void call(Subscriber<? super Song> subscriber) {
                int result = mLiteOrm.update(song);
                if (result > 0) {
                    subscriber.onNext(song);
                } else {
                    subscriber.onError(new Exception("Update song failed"));
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Song> setSongAsFavorite(final Song song, final boolean isFavorite) {
        return Observable.create(new Observable.OnSubscribe<Song>() {
            @Override
            public void call(Subscriber<? super Song> subscriber) {
                List<PlayList> playLists = mLiteOrm.query(
                        QueryBuilder.create(PlayList.class));
                if (playLists.isEmpty()) {
                    PlayList defaultFavorite = DBUtils.generateFavoritePlayList(mContext);
                    playLists.add(defaultFavorite);
                }

                PlayList favorite = playLists.get(0);
                song.setFavorite(isFavorite);
                favorite.setUpdatedAt(new Date());
                if (isFavorite) {
                    favorite.addSong(song, 0);
                } else {
                    favorite.removeSong(song);
                }

                mLiteOrm.insert(song , ConflictAlgorithm.Replace);
                long result = mLiteOrm.insert(favorite, ConflictAlgorithm.Replace);
                if (result > 0) {
                    subscriber.onNext(song);
                }else {
                    if (isFavorite) {
                        subscriber.onError(new Exception("Set song as favorite failed"));
                    }else {
                        subscriber.onError(new Exception("Set song as unfavorite failed"));
                    }
                }
                subscriber.onCompleted();
            }
        });
    }
}
