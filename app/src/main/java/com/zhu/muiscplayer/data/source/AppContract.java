package com.zhu.muiscplayer.data.source;

import com.zhu.muiscplayer.data.model.Folder;
import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.data.model.Song;

import java.util.List;

import rx.Observable;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/20
 * Time: 21:17
 * Desc:com.zhu.muiscplayerlib.data
 */

 interface AppContract {

    //Play List
    Observable<List<PlayList>> playLists();

    List<PlayList> cachedPlayLists();

    Observable<PlayList> create(PlayList playList);

    Observable<PlayList> update(PlayList playList);

    Observable<PlayList> delete(PlayList playList);

    //Folder
    Observable<List<Folder>> folders();

    Observable<Folder> create(Folder folder);

    Observable<List<Folder>> create(List<Folder> folders);

    Observable<Folder> update(Folder folder);

    Observable<Folder> delete(Folder folder);


    //Song
    Observable<List<Song>> inSert(List<Song> songs);

    Observable<Song> update(Song song);

    Observable<Song> setSongAsFavorite(Song song, boolean isFavorite);

}
