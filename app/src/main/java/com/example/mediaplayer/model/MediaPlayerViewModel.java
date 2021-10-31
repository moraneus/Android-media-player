package com.example.mediaplayer.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class MediaPlayerViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MediaPlayerViewModel.class.getSimpleName();
    private final SongsRepository mSongRepository;

    public MediaPlayerViewModel(@NonNull Application application) {
        super(application);
        mSongRepository = SongsRepository.getInstance(application);
    }

    public LiveData<List<SongEntry>> getSongs() {
        Log.d(LOG_TAG, "getSongs()");
        return mSongRepository.getSongs();
    }
}
