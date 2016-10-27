package com.zhu.muiscplayer.ui.music;


import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.RxBus;
import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.data.model.Song;
import com.zhu.muiscplayer.data.source.AppRepository;
import com.zhu.muiscplayer.data.source.PreferenceManager;
import com.zhu.muiscplayer.event.PlayListNowEvent;
import com.zhu.muiscplayer.event.PlaySongEvent;
import com.zhu.muiscplayer.player.IPlayback;
import com.zhu.muiscplayer.player.PlayMode;
import com.zhu.muiscplayer.player.PlaybackService;
import com.zhu.muiscplayer.ui.base.BaseFragment;
import com.zhu.muiscplayer.ui.widget.ShadowImageView;
import com.zhu.muiscplayer.utils.AlbumUtils;
import com.zhu.muiscplayer.utils.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/17
 * Time: 20:34
 * Desc:com.zhu.muiscplayer.ui.music
 */

public class MusicPlayerFragment extends BaseFragment
        implements MusicPlayerContract.View, IPlayback.Callback {

    // Update seek bar every second
    private static final long UPDATE_PROGRESS_INTERVAL = 1000;
    @BindView(R.id.image_view_album)
    ShadowImageView mImageViewAlbum;
    @BindView(R.id.text_view_name)
    TextView mTextViewName;
    @BindView(R.id.text_view_artist)
    TextView mTextViewArtist;
    @BindView(R.id.text_view_progress)
    TextView mTextViewProgress;
    @BindView(R.id.text_view_duration)
    TextView mTextViewDuration;
    @BindView(R.id.seek_bar)
    AppCompatSeekBar mSeekBarProgress;


    @BindView(R.id.button_play_mode_toggle)
    AppCompatImageView mButtonPlayModeToggle;
    @BindView(R.id.button_play_toggle)
    AppCompatImageView mButtonPlayToggle;
    @BindView(R.id.button_favorite_toggle)
    AppCompatImageView mButtonFavoriteToggle;

    private IPlayback mPlayer;

    private Handler mHandler = new Handler();

    private MusicPlayerContract.Presenter mPresenter;

    private Runnable mProgressCallback = new Runnable() {
        @Override
        public void run() {
            if (isDetached()) return;

            if (mPlayer.isPlaying()) {
                int progress = (int) (mSeekBarProgress.getMax()
                        * ((float) mPlayer.getProgress() / (float)getCurrentSongDuration()));
                updateProgressTextWithDuration(mPlayer.getProgress());
                if (progress >= 0 && progress <= mSeekBarProgress.getMax()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mSeekBarProgress.setProgress(progress , true);
                    }else {
                        mSeekBarProgress.setProgress(progress);
                    }
                    mHandler.postDelayed(this , UPDATE_PROGRESS_INTERVAL);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
       mUnbinder =  ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSeekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateProgressTextWithProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mProgressCallback);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(getDuration(seekBar.getProgress()));
                if (mPlayer.isPlaying()) {
                    mHandler.removeCallbacks(mProgressCallback);
                    mHandler.post(mProgressCallback);
                }
            }
        });

        new MusicPlayerPresenter(getActivity() , AppRepository.getInstance() ,this).subscribe();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPlayer != null && mPlayer.isPlaying()) {
            mHandler.removeCallbacks(mProgressCallback);
            mHandler.post(mProgressCallback);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mProgressCallback);
    }

    @Override
    public void onDestroyView() {
        mPresenter.unSubscribe();
        super.onDestroyView();
    }

    //Click Events

    @OnClick(R.id.button_play_toggle)
    public void onPlayToggleAction(View view) {
        if (mPlayer == null) return;

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.play();
        }
    }

    @OnClick(R.id.button_play_mode_toggle)
    public void onPlayModeToggleAction(View view) {
        if (mPlayer == null) return;

        PlayMode current = PreferenceManager.lastPlayMode(getActivity());
        PlayMode newMode = PlayMode.switchNextMode(current);
        PreferenceManager.setPlayMode(getActivity(), newMode);
        mPlayer.setPlayMode(newMode);
        updatePlayMode(newMode);
    }

    @OnClick(R.id.button_play_last)
    public void onPlayLastAction(View view) {
        if (mPlayer == null) return;

        mPlayer.playLast();
    }

    @OnClick(R.id.button_play_next)
    public void onPlayNextAction(View view) {
        if (mPlayer == null) return;
        mPlayer.playNext();
    }

    @OnClick(R.id.button_favorite_toggle)
    public void onFavoriteToggleAction(View view) {
        if (mPlayer == null) return;

        Song currentSong = mPlayer.getPlayingSong();
        if (currentSong != null){
            view.setEnabled(false);
            mPresenter.setSongAsFavorite(currentSong , !currentSong.isFavorite());
        }
    }

    @Override
    protected Subscription subscribeEvents() {
        return RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof PlaySongEvent) {
                            onPlaySongEvent((PlaySongEvent) o);
                        }else if (o instanceof PlayListNowEvent){
                            onPlayListNowEvent((PlayListNowEvent) o);
                        }
                    }
                })
                .subscribe(RxBus.defaultSubscriber());
    }

    private void onPlaySongEvent(PlaySongEvent event) {
        Song song = event.mSong;
        playSong(song);
    }

    private void onPlayListNowEvent(PlayListNowEvent event) {
        PlayList playList = event.playList;
        int playIndex = event.playIndex;
        playSong(playList , playIndex);
    }

    //Music Controals

    private void playSong(Song song) {
        PlayList playList = new PlayList(song);
        playSong(playList , 0);
    }

    private void playSong(PlayList playList , int playIndex) {
        if (playList == null) return;

        playList.setPlayMode(PreferenceManager.lastPlayMode(getActivity()));
        //boolean result=
        mPlayer.play(playList , playIndex);

        Song song = playList.getCurrentSong();
        onSongUpdated(song);

    }
    @Override
    public void onSwitchLast(@Nullable Song last) {
        onSongUpdated(last);
    }

    @Override
    public void onSwitchNext(@Nullable Song next) {
       onSongUpdated(next);
    }

    @Override
    public void onComplete(@NonNull Song next) {
       onSongUpdated(next);
    }

    @Override
    public void onPlayStatusChanged(boolean isPlaying) {
         updatePlayToggle(isPlaying);
        if (isPlaying) {
            mImageViewAlbum.resumeRotateAnimation();
            mHandler.removeCallbacks(mProgressCallback);
            mHandler.post(mProgressCallback);
        } else {
            mImageViewAlbum.pauseRotateAnimation();
            mHandler.removeCallbacks(mProgressCallback);
        }
    }

    @Override
    public void handleError(Throwable error) {
        Toast.makeText(getActivity() , error.getMessage() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlaybackServiceBound(PlaybackService service) {
        mPlayer = service;
        mPlayer.registerCallback(this);
    }

    @Override
    public void onPlaybackServiceUnbound() {
        mPlayer.unregisterCallback(this);
        mPlayer = null;
    }

    @Override
    public void onSongSetAsFavorite(@Nullable Song song) {
        mButtonFavoriteToggle.setEnabled(true);
        updateFavoriteToggle(song.isFavorite());
    }

    @Override
    public void onSongUpdated(@Nullable Song song) {
        if (song == null) {
            mImageViewAlbum.cancelRotateAnimation();
            mButtonPlayToggle.setImageResource(R.drawable.ic_play);
            mSeekBarProgress.setProgress(0);
            updateProgressTextWithProgress(0);
            seekTo(0);
            mHandler.removeCallbacks(mProgressCallback);
            return;
        }

        // Step 1: Song name and artist
        mTextViewName.setText(song.getDisplayName());
        mTextViewArtist.setText(song.getArtist());
        // Step 2: favorite
        mButtonFavoriteToggle.setImageResource(song.isFavorite() ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
        // Step 3: Duration
        mTextViewDuration.setText(TimeUtils.formatDuration(song.getDuration()));
        // Step 4: Keep these things updated
        // - Album rotation
        // - Progress(textViewProgress & seekBarProgress)
        Bitmap bitmap = AlbumUtils.parseAlbum(song);
        if (bitmap == null) {
            mImageViewAlbum.setImageResource(R.mipmap.default_record_album);
        } else {
            mImageViewAlbum.setImageBitmap(AlbumUtils.getCroppedBitmap(bitmap));
        }
        mImageViewAlbum.pauseRotateAnimation();
        mHandler.removeCallbacks(mProgressCallback);
        if (mPlayer.isPlaying()) {
            mImageViewAlbum.startRotateAnimation();
            mHandler.post(mProgressCallback);
            mButtonPlayToggle.setImageResource(R.drawable.ic_pause);
        }
    }




    @Override
    public void updatePlayMode(PlayMode playMode) {
        if (playMode == null) {
            playMode = PlayMode.getDefault();
        }
        switch (playMode) {
            case LIST:
                mButtonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_list);
                break;
            case LOOP:
                mButtonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_loop);
                break;
            case SHUFFLE:
                mButtonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_shuffle);
                break;
            case SINGLE:
                mButtonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_single);
                break;
        }
    }

    @Override
    public void updatePlayToggle(boolean play) {
       mButtonPlayToggle.setImageResource(play ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    @Override
    public void updateFavoriteToggle(boolean favorite) {
        mButtonFavoriteToggle.setImageResource(favorite ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite_no);
    }


    @Override
    public void setPresenter(MusicPlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private int getCurrentSongDuration() {
        Song currentSong = mPlayer.getPlayingSong();
        int duration = 0;
        if (currentSong != null) {
            duration = currentSong.getDuration();
        }
        return duration;
    }

    private void updateProgressTextWithProgress(int progress) {
        int targetDuration = getDuration(progress);
        mTextViewProgress.setText(TimeUtils.formatDuration(targetDuration));
    }

    private void updateProgressTextWithDuration(int duration) {
        mTextViewProgress.setText(TimeUtils.formatDuration(duration));
    }

    private int getDuration(int progress) {
        return (int) (getCurrentSongDuration() * (float)progress / mSeekBarProgress.getMax());
    }

    private void seekTo(int duration){
        mPlayer.seekTo(duration);
    }
}
