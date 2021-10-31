package com.example.mediaplayer.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mediaplayer.sync.MediaStoreAudioObserver;
import com.example.mediaplayer.utilities.AppExecutors;

public class MediaStoreAudioObserverService extends Service {

    private static final String LOG_TAG = MediaStoreAudioObserverService.class.getSimpleName();
    private MediaStoreAudioObserver mAudioObserver;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand()");
        Handler handler;
        handler = new Handler(getMainLooper());
        mAudioObserver = new MediaStoreAudioObserver(handler, getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            final Context context = getApplicationContext();
            @Override
            public void run() {
                context.getContentResolver().
                        registerContentObserver(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                true,
                                mAudioObserver);
            }
        });
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy()");
        getContentResolver().unregisterContentObserver(mAudioObserver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
