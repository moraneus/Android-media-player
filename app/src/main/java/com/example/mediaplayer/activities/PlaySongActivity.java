package com.example.mediaplayer.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mediaplayer.R;
import com.example.mediaplayer.databinding.ActivityPlaySongBinding;
import com.example.mediaplayer.model.MediaPlayerViewModel;
import com.example.mediaplayer.model.SongEntry;
import com.example.mediaplayer.services.PlayAudioService;
import com.example.mediaplayer.utilities.TimeConvertor;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaySongActivity extends AppCompatActivity
        implements ServiceConnection {

    private static final String LOG_TAG = PlaySongActivity.class.getSimpleName();
    public static final String POSITION_KEY = "songPositionKey";
    private final int INITIAL_POSITION = -1;
    private final int NEXT = 1;
    private final int PREVIOUS = -1;

    private int mCurrentSong = INITIAL_POSITION;
    private ActivityPlaySongBinding mActivityPlaySongBinding;

    public static List<SongEntry> mSongs;
    public static Uri mCurrentSongUri;

    private Handler mHandler;
    private PlayAudioService mPlayAudioService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view.
        mActivityPlaySongBinding = ActivityPlaySongBinding.inflate(getLayoutInflater());
        View view = mActivityPlaySongBinding.getRoot();
        setContentView(view);

        // Start playing selected song.
        getIntentData();
        extractSongs();

        // Set seekbar listener.
        setSeekBarListener();
    }


    @Override
    protected void onResume() {
        Intent intent = new Intent(this, PlayAudioService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private void extractSongs() {
        Log.d(LOG_TAG, "setupViewModel()");
        final MediaPlayerViewModel mediaPlayerViewModel =
                ViewModelProviders.of(this).get(MediaPlayerViewModel.class);
        LiveData<List<SongEntry>> songs = mediaPlayerViewModel.getSongs();
        songs.observe(this, new Observer<List<SongEntry>>() {
            @Override
            public void onChanged(@Nullable List<SongEntry> songEntries) {
                songs.removeObserver(this);
                updateSongsList(songEntries);
                playCurrentSong();
            }
        });
    }

    private void updateSongsList(List<SongEntry> songEntries) {
        mSongs = songEntries;
    }


    private void getIntentData() {
        mCurrentSong = getIntent().getIntExtra(MainActivity.SELECTED_SONG_KEY, INITIAL_POSITION);
    }

    private void playCurrentSong() {
        if (mSongs != null) {
            mActivityPlaySongBinding.fbPlayPause.setImageResource(R.drawable.ic_pause);
            mCurrentSongUri = mSongs.get(mCurrentSong).getFileLocation();
        }

        Intent intent = new Intent(this, PlayAudioService.class);
        intent.putExtra(POSITION_KEY, mCurrentSong);
        startService(intent);
    }

    /**
     * Set the activity song views with the relevant song data.
     */
    private void SetMetadataInViews(String songTitle,
                                    String songArtist,
                                    Long songDuration,
                                    Uri songImageUri) {

        playPauseAction();

        // Set total time.
        String totalTimeDuration = TimeConvertor.LongMillisecondsToDuration(songDuration);
        mActivityPlaySongBinding
                .tvTotalDuration
                .setText(totalTimeDuration);

        // Set album image.
        Picasso.get().load(songImageUri)
                .placeholder(R.drawable.ic_android_black)
                .into(mActivityPlaySongBinding.ivAlbumImage);

        // Set title
        mActivityPlaySongBinding
                .tvSongName.
                setText(songTitle);

        // Set artist
        mActivityPlaySongBinding
                .tvSongArtist
                .setText(songArtist);
    }

    private void setSeekBarListener() {
        mActivityPlaySongBinding.sbSeekBar.
                setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (mPlayAudioService != null && fromUser) {
                            mPlayAudioService.seekTo(progress * 1000);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        mHandler = new Handler(getMainLooper());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mPlayAudioService != null) {
                    int currentSeekPosition = mPlayAudioService.getCurrentPosition() / 1000;
                    mActivityPlaySongBinding.sbSeekBar.setProgress(currentSeekPosition);
                    mActivityPlaySongBinding.tvDurationPlayed.setText(
                            TimeConvertor.IntSecondsToDuration(currentSeekPosition));
                }
                mHandler.postDelayed(this, 100);
            }
        });
    }


    /**
     * Define the listener of the play/pause song button.
     * @param view Button view
     */
    public void PlayPauseButtonAction(View view) {
        playPauseAction();
    }

    /**
     * Play / Pause the song according to current state.
     * If the song is playing it will be paused and vice versa.
     */
    private void playPauseAction() {
        if (mPlayAudioService.isPlaying()) {
            mActivityPlaySongBinding.fbPlayPause.setImageResource(R.drawable.ic_play_arrow);
            mPlayAudioService.pause();
        } else {
            mActivityPlaySongBinding.fbPlayPause.setImageResource(R.drawable.ic_pause);
            mPlayAudioService.start();
        }
    }

    /**
     * Define the listener of the next song button.
     * @param view Button view
     */
    public void NextSongButtonAction(View view) {
        mPlayAudioService.moveToSongAction(NEXT);
    }

    /**
     * Define the listener of the previous song button.
     * @param view Button view
     */
    public void PreviousSongButtonAction(View view) {
        mPlayAudioService.moveToSongAction(PREVIOUS);
    }

    /**
     * Setup the listener for shuffle button.
     * @param view Button View
     */
    public void shuffleOnClickAction(View view) {
        if (mPlayAudioService.getIsShuffle()) {
            shuffleOff();
        } else {
            if(mPlayAudioService.getIsRepeat()) {
                repeatOff();
            }
            shuffleOn();
        }
    }

    /**
     * Setup the listener for repeat button.
     * @param view Button View
     */
    public void repeatOnClickAction(View view) {
        if (mPlayAudioService.getIsRepeat()) {
            repeatOff();
        } else {
            if(mPlayAudioService.getIsShuffle()) {
                shuffleOff();
            }
            repeatOn();
        }
    }

    /**
     * Set the action when shuffle is turned on.
     */
    private void shuffleOn() {
        mPlayAudioService.setShuffleOn();
        mActivityPlaySongBinding.ivShuffle.setImageResource(R.drawable.ic_shuffle_on);
    }

    /**
     * Set the action when shuffle is turned off.
     */
    private void shuffleOff() {
        mPlayAudioService.setShuffleOff();
        mActivityPlaySongBinding.ivShuffle.setImageResource(R.drawable.ic_shuffle_off);
    }

    /**
     * Set the action when repeat is turned on.
     */
    private void repeatOn() {
        mPlayAudioService.setRepeatOn();
        mActivityPlaySongBinding.ivRepeat.setImageResource(R.drawable.ic_repeat_on);
    }

    /**
     * Set the action when repeat is turned off.
     */
    private void repeatOff() {
        mPlayAudioService.setRepeatOff();
        mActivityPlaySongBinding.ivRepeat.setImageResource(R.drawable.ic_repeat_off);
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PlayAudioService.PlayAudioBinder playAudioBinder =
                (PlayAudioService.PlayAudioBinder) service;
        mPlayAudioService = playAudioBinder.getService();

        playAudioBinder.setDurationCallback(duration ->
                mActivityPlaySongBinding.sbSeekBar.setMax(duration));

        playAudioBinder.SetOnCompletionCallback(this::SetMetadataInViews);
    }


    @Override
    public void onServiceDisconnected(ComponentName name) {
        mPlayAudioService = null;
    }
}
