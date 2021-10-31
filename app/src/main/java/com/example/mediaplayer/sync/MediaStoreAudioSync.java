package com.example.mediaplayer.sync;

import android.content.Context;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MediaStoreAudioSync {

    private static final String LOG_TAG = MediaStoreAudioObserver.class.getSimpleName();
    private final WorkManager mWorkManager;

    public MediaStoreAudioSync(Context context) {
        mWorkManager = WorkManager.getInstance(context.getApplicationContext());
        loadSongsFromMediaStore();
    }

    public void loadSongsFromMediaStore() {
        Log.d(LOG_TAG, "loadSongsFromMediaStore()");
        mWorkManager.enqueue(OneTimeWorkRequest.from(MediaStoreAudioSongsSearchWorker.class));
    }
}
