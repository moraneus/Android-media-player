package com.example.mediaplayer.sync;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mediaplayer.model.SongEntry;
import com.example.mediaplayer.model.SongsDatabase;
import com.example.mediaplayer.utilities.AppExecutors;
import com.example.mediaplayer.utilities.MediaStoreAudioUtil;

public class MediaStoreAudioObserver extends ContentObserver {

    private static final String LOG_TAG = MediaStoreAudioObserver.class.getSimpleName();
    private final Context mContext;

    public MediaStoreAudioObserver(Handler handler, Context context) {
        super(handler);
        mContext = context.getApplicationContext();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, @Nullable Uri uri) {
        super.onChange(selfChange, uri);
        Log.d(LOG_TAG, String.format("onChange(uri = {})", uri.toString()));
        Cursor cursor = MediaStoreAudioUtil.findLastAddedAudioFile(mContext);
        if (cursor != null) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    SongEntry newSongEntry = MediaStoreAudioUtil.CreateSongEntry(cursor);
                    SongsDatabase songsDatabase = SongsDatabase.getInstance(mContext);
                    songsDatabase.songDao().insertSong(newSongEntry);
                }
            });
        }
    }
}





        //        getContentResolver().registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true,
    //                new ContentObserver(new Handler()) {
    //                    @Override
    //                    public void onChange(boolean selfChange) {
    //                    final Cursor cursor = getContentResolver()
    //                            .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
    //                                    null, MediaStore.Audio.AudioColumns.DATE_ADDED + " DESC");
    //
    //                        // Put it in the image view
    //                        if (cursor.moveToFirst()) {
    //                            int titleColumn = cursor.getColumnIndex
    //                                    (MediaStore.Audio.Media.TITLE_RESOURCE_URI);
    //                            String thisTitle = cursor.getString(titleColumn);
    //                            Log.d("your_tag", thisTitle);
    //                        }
    //
    //
    //                    }
    //                }
    //        );
