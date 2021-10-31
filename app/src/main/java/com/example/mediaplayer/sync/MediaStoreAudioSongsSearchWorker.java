package com.example.mediaplayer.sync;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.mediaplayer.model.SongEntry;
import com.example.mediaplayer.model.SongsDatabase;
import com.example.mediaplayer.utilities.MediaStoreAudioUtil;

public class MediaStoreAudioSongsSearchWorker extends Worker {

    private static final String LOG_TAG = MediaStoreAudioSongsSearchWorker.class.getSimpleName();
    private final SongsDatabase mSongsDatabase;

    public MediaStoreAudioSongsSearchWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        mSongsDatabase = SongsDatabase.getInstance(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(LOG_TAG, "doWork()");
        Cursor songCursor = MediaStoreAudioUtil.QueryMediaStoreForAudioFiles(
                getApplicationContext());
        if (songCursor != null) {
            Log.d(LOG_TAG, "doWork() -> cursor is not null");
            while (songCursor.moveToNext()) {
                Log.d(LOG_TAG, "doWork() -> cursor has next");
                SongEntry newSongEntry = MediaStoreAudioUtil.CreateSongEntry(songCursor);
                mSongsDatabase.songDao().insertSong(newSongEntry);
            }
        }
        return Result.success();
    }
}



//    private Cursor QueryMediaStore() {
//        Context context = getApplicationContext();
//        ContentResolver songsResolver = context.getContentResolver();
//        Uri songsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {
//                MediaStore.Audio.AudioColumns.DATA,
//                MediaStore.Audio.AudioColumns.TITLE,
//                MediaStore.Audio.ArtistColumns.ARTIST,
//                MediaStore.Audio.Media.ALBUM_ID
//        };
//
//        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
//        Cursor songCursor = songsResolver.query(
//                songsUri,
//                projection,
//                selection,
//                null,
//                null);
//        return songCursor;
//    }
//
//    private void CreateSongEntry(Cursor songCursor) {
//        String uri = songCursor.getString(0);
//        if (uri.endsWith(AUDIO_MUSIC_FILE)) {
//            String title = songCursor.getString(1);
//            String artist = songCursor.getString(2);
//            Long albumId = songCursor.getLong(3);
//            Uri image = Mp3ImageExtractor.getImageUri(albumId);
//            SongEntry newSong = new SongEntry(Uri.parse(uri), title, artist, image);
//            mSongsDatabase.songDao().insertSong(newSong);
//        }
//    }
//
//    public void registerContentObserver() {
//        if (!mObserverRegistered) {
//            ContentResolver contentResolver = getApplicationContext().getContentResolver();
//            contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true,
//                    new ContentObserver(new Handler()) {
//                        public void onChange(boolean selfChange) {
//                            final Cursor songCursor = contentResolver.query(
//                                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                                    null,
//                                    null,
//                                    null,
//                                    MediaStore.Audio.AudioColumns.DATE_ADDED + " DESC");
//
//                            // Put it in the image view
//                            if (songCursor.moveToFirst()) {
//                                int titleColumn = songCursor.getColumnIndex
//                                        (MediaStore.Audio.Media.TITLE_RESOURCE_URI);
//                                String thisTitle = songCursor.getString(titleColumn);
//                                Log.d("QQQQQ", thisTitle);
//                                CreateSongEntry(songCursor);
//                            }
//                        }
//
//                    });
//
//            mObserverRegistered = true;
//        }
//    }



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



