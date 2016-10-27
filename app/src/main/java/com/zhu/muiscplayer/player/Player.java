package com.zhu.muiscplayer.player;

import android.media.MediaPlayer;
import android.util.Log;

import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.data.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/24
 * Time: 21:09
 * Desc:com.zhu.muiscplayer.player
 */

public class Player implements IPlayback , MediaPlayer.OnCompletionListener{

    private static final String TAG = "Player";

    private static volatile Player sInstance;

    private MediaPlayer mPlayer;

    private PlayList mPlayList;
    // Default size 2: for service and UI
    private List<Callback> mCallbacks = new ArrayList<>();

    //Player status
    private boolean isPaused;

    private Player(){
        mPlayer = new MediaPlayer();
        mPlayList = new PlayList();
        mPlayer.setOnCompletionListener(this);
    }

    public static Player getInstance() {
        if (sInstance == null) {
            synchronized (Player.class) {
                if (sInstance == null) {
                    sInstance = new Player();
                }
            }
        }
        return sInstance;
    }


    @Override
    public void setPlayList(PlayList list) {
        if (list == null) {
            list = new PlayList();
        }
        mPlayList = list;
    }

    @Override
    public boolean play() {
        if (isPaused) {
            mPlayer.start();
            notifyPlayStatusChanged(true);
            return true;
        }

        if (mPlayList.prepare()) {
            Song song = mPlayList.getCurrentSong();
            try {
                mPlayer.reset();
                mPlayer.setDataSource(song.getPath());
                mPlayer.prepare();
                mPlayer.start();
                notifyPlayStatusChanged(true);
            } catch (IOException e) {
                Log.e(TAG , "paly: " , e);
                notifyPlayStatusChanged(false);
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }



    @Override
    public boolean play(PlayList list) {

        if (list == null) return false;

        isPaused= false;
        setPlayList(list);

        return play();
    }



    @Override
    public boolean play(PlayList list, int startIndex) {

        if (list == null || startIndex < 0
                || startIndex < 0 || startIndex >= list.getNumOfSongs()) return false;

        isPaused = false;
        list.setPlayingIndex(startIndex);
        setPlayList(list);
        return play();
    }

    @Override
    public boolean play(Song song) {

        if (song == null) return false;

        isPaused = false;
        mPlayList.getSongs().clear();
        mPlayList.getSongs().add(song);
        return play();
    }

    @Override
    public boolean playLast() {
        isPaused = false;
        boolean hasLast = mPlayList.hasLast();
        if (hasLast) {
            Song last = mPlayList.last();
            play();
            notifyPlayLast(last);
            return true;
        }
        return false;
    }

    @Override
    public boolean playNext() {

        isPaused = false;
        boolean hasNext = mPlayList.hasNext(false);
        if (hasNext) {
            Song next = mPlayList.next();
            play();
            notifyPlayNext(next);
            return true;
        }
        return false;
    }

    @Override
    public boolean pause() {

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            isPaused = true;
            notifyPlayStatusChanged(false);
            return true;
        }

        return false;
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public Song getPlayingSong() {
        return mPlayList.getCurrentSong();
    }

    @Override
    public boolean seekTo(int progress) {

        if (mPlayList.getSongs().isEmpty()) return false;
        Song currentSong = mPlayList.getCurrentSong();
        if (currentSong != null) {
            if (currentSong.getDuration() <= progress) {
                onCompletion(mPlayer);
            }else {
                mPlayer.seekTo(progress);
            }
        }
        return true;
    }

    @Override
    public void setPlayMode(PlayMode playMode) {
         mPlayList.setPlayMode(playMode);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        Song next = null;
        // There is only one limited play mode which is list,
        // player should be stopped when hitting the list end
        if (mPlayList.getPlayMode() == PlayMode.LIST
                && mPlayList.getPlayingIndex() == mPlayList.getNumOfSongs() - 1) {
            // In the end of the list
            // Do nothing, just deliver the callback
        } else if (mPlayList.getPlayMode() == PlayMode.SINGLE) {
            next = mPlayList.getCurrentSong();
            play();
        } else {
            boolean hasNext = mPlayList.hasNext(true);
            if (hasNext){
                next = mPlayList.next();
                play();
            }
        }
        notifyComplete(next);
    }

    @Override
    public void releasePlayer() {
        mPlayList = null;
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void registerCallback(Callback callback) {
       mCallbacks.add(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        mCallbacks.remove(callback);
    }

    @Override
    public void removeCallback() {
        mCallbacks.clear();
    }

    private void notifyPlayStatusChanged(boolean isPlaying) {
        for (Callback callback : mCallbacks){
            callback.onPlayStatusChanged(isPlaying);
        }
    }

    private void notifyPlayLast(Song song) {
        for (Callback callback : mCallbacks) {
            callback.onSwitchLast(song);
        }
    }

    private void notifyPlayNext(Song song) {
        for (Callback callback : mCallbacks) {
            callback.onSwitchNext(song);
        }
    }

    private void notifyComplete(Song song) {
        for (Callback callback : mCallbacks) {
            callback.onComplete(song);
        }
    }
}
