package com.example.mediaplayer.services;

import static com.example.mediaplayer.activities.PlaySongActivity.POSITION_KEY;
import static com.example.mediaplayer.activities.PlaySongActivity.mSongs;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mediaplayer.model.SongEntry;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayAudioService extends Service implements MediaPlayer.OnCompletionListener {

    private static final String LOG_TAG = PlayAudioService.class.getSimpleName();
    private final int INITIAL_POSITION = -1;
    private final int NEXT = 1;
    private final int PREVIOUS = -1;

    private MediaPlayer mMediaPlayer;
    private List<SongEntry> mSongsEntries;
    private int mCurrentIndexSong = INITIAL_POSITION;
    private boolean mIsShuffle, mIsRepeat;

    private DurationCallback mDurationCallback;
    private OnCompletionCallback mOnCompletionCallback;

    private final PlayAudioBinder mPlayAudioBinder = new PlayAudioBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind()");
        return mPlayAudioBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand()");
        mCurrentIndexSong = intent.getIntExtra(POSITION_KEY, INITIAL_POSITION);
        if (mCurrentIndexSong != INITIAL_POSITION) {
            playSong();
        }
        return START_STICKY;
    }

    private void playSong() {
        Log.d(LOG_TAG, "playSong()");
        mSongsEntries = mSongs;

        if (mMediaPlayer != null) {
            stop();
            release();
        }
        initializeMediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);

        if (mDurationCallback != null) {
            mDurationCallback.onDurationUpdate(getDuration() / 1000);
        }
        if (mOnCompletionCallback != null) {
            final String songTitle = mSongsEntries.get(mCurrentIndexSong).getTitle();
            final String songArtist = mSongsEntries.get(mCurrentIndexSong).getArtist();
            final Long songDuration = mSongsEntries.get(mCurrentIndexSong).getDuration();
            final Uri songImageUri = mSongsEntries.get(mCurrentIndexSong).getImageLocation();
            mOnCompletionCallback.onCompletion(
                    songTitle,
                    songArtist,
                    songDuration,
                    songImageUri);
        }
        start();
    }

    public void start() {
        Log.d(LOG_TAG, "start()");
        mMediaPlayer.start();
    }

    public void pause() {
        Log.d(LOG_TAG, "pause()");
        mMediaPlayer.pause();
    }

    public void stop() {
        Log.d(LOG_TAG, "stop()");
        mMediaPlayer.stop();
    }

    public void release() {
        Log.d(LOG_TAG, "release()");
        mMediaPlayer.release();
    }

    public boolean isPlaying() {
        Log.d(LOG_TAG, "isPlaying()");
        return mMediaPlayer.isPlaying();
    }

    public int getDuration() {
        Log.d(LOG_TAG, "getDuration()");
        return mMediaPlayer.getDuration();
    }

    public void seekTo(int position) {
        Log.d(LOG_TAG, "seekTo()");
        mMediaPlayer.seekTo(position);
    }

    public void setRepeatOn() {
        Log.d(LOG_TAG, "setRepeatOn()");
        mIsRepeat = true;
    }

    public void setRepeatOff() {
        Log.d(LOG_TAG, "setRepeatOff()");
        mIsRepeat = false;
    }

    public boolean getIsRepeat() {
        Log.d(LOG_TAG, "getIsRepeat()");
        return mIsRepeat;
    }

    public void setShuffleOn() {
        Log.d(LOG_TAG, "setShuffleOn()");
        mIsShuffle = true;
    }

    public void setShuffleOff() {
        Log.d(LOG_TAG, "setShuffleOff()");
        mIsShuffle = false;
    }

    public boolean getIsShuffle() {
        Log.d(LOG_TAG, "getIsShuffle()");
        return mIsShuffle;
    }

    public int getCurrentPosition() {
        Log.d(LOG_TAG, "getCurrentPosition()");
        return mMediaPlayer.getCurrentPosition();
    }


    /**
     * Set the action when press on the next or previous song.
     *
     * @param direction 1 for the next and -1 for the previous
     */
    public void moveToSongAction(int direction) {
        int songsListSize = mSongs.size();

        if (mIsShuffle) {
            mCurrentIndexSong = ThreadLocalRandom.current().nextInt(
                    0, songsListSize - 1);
        }
        else if (mIsRepeat) {
            // Do nothing
        }
        else {
            int position = (mCurrentIndexSong + direction) % songsListSize;
            mCurrentIndexSong = position < 0 ? songsListSize - 1 : position;
        }
        playSong();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(LOG_TAG, "onCompletion()");
        moveToSongAction(NEXT);
    }


    public void initializeMediaPlayer() {
        Log.d(LOG_TAG, "initializeMediaPlayer()");
        Uri currentUriSongEntry = mSongsEntries.get(mCurrentIndexSong).getFileLocation();
        mMediaPlayer = MediaPlayer.create(getBaseContext(), currentUriSongEntry);

    }

    public class PlayAudioBinder extends Binder {
        public PlayAudioService getService() {
            return PlayAudioService.this;
        }

        public void setDurationCallback(DurationCallback durationCallback) {
            mDurationCallback = durationCallback;
        }

        public void SetOnCompletionCallback(OnCompletionCallback onCompletionCallback) {
            mOnCompletionCallback = onCompletionCallback;
        }

    }

    public interface DurationCallback {
        void onDurationUpdate(int duration);
    }

    public interface OnCompletionCallback {
        void onCompletion(String songTitle, String songArtist, Long songDuration, Uri songImageUri);
    }
}
