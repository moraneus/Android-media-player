package com.example.mediaplayer.model;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.mediaplayer.sync.MediaStoreAudioSync;
import java.util.List;

public class SongsRepository {

    private static final String LOG_TAG = SongsRepository.class.getSimpleName();
    private static SongsRepository sInstance;
    private static final Object LOCK = new Object();
    private static SongsDatabase mSongsDatabase;

    private SongsRepository(Context context) {
        Context applicationContext = context.getApplicationContext();
        mSongsDatabase = SongsDatabase.getInstance(applicationContext);
        MediaStoreAudioSync mMediaStoreAudioSync = new MediaStoreAudioSync(applicationContext);
        mMediaStoreAudioSync.loadSongsFromMediaStore();
    }

    public static SongsRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new repository instance");
                sInstance = new SongsRepository(context);
            };
        }
        Log.d(LOG_TAG, "Getting the repository instance");
        return sInstance;
    }

    public LiveData<List<SongEntry>> getSongs() {
        Log.d(LOG_TAG, "getSongs()");
        return mSongsDatabase.songDao().loadAllSongs();
    }

}
