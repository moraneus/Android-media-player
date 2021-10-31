package com.example.mediaplayer.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mediaplayer.model.SongEntry;

public class MediaStoreAudioUtil {

    private static final String LOG_TAG = MediaStoreAudioUtil.class.getSimpleName();
    private static final String AUDIO_MUSIC_FILE = "mp3";
    private static final String SELECTION_ALL = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    private static final String SORT_BY_LAST_ADDED = MediaStore.Audio.AudioColumns.DATE_ADDED + " DESC";
    private static final String[] PROJECTION = {
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.ArtistColumns.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.AudioColumns.MIME_TYPE,
            MediaStore.Audio.AudioColumns.DATE_ADDED
    };


    private static final Uri AUDIO_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


    @Nullable
    public static SongEntry CreateSongEntry(@NonNull Cursor songCursor) {
        Log.d(LOG_TAG, "CreateSongEntry()");
        String uri = songCursor.getString(0);
        if (uri.endsWith(AUDIO_MUSIC_FILE)) {
            String title = songCursor.getString(1);
            String artist = songCursor.getString(2);
            Long duration = songCursor.getLong(3);
            Long albumId = songCursor.getLong(4);
            Uri image = AudioThumbnailImageExtractor.getImageUri(albumId);
            Log.d(LOG_TAG, String.format("CreateSongEntry() -> Create: {}", title));
            return new SongEntry(Uri.parse(uri), title, artist, duration, image);
        }
        return null;
    }

    public static Cursor QueryMediaStoreForAudioFiles(@NonNull Context context) {
        Log.d(LOG_TAG, "QueryMediaStoreForAudioFiles()");
        ContentResolver songsResolver = context.getContentResolver();

        Cursor songCursor = songsResolver.query(
                AUDIO_URI,
                PROJECTION,
                SELECTION_ALL,
                null,
                null);
        return songCursor;
    }

    @Nullable
    public static Cursor findLastAddedAudioFile(@NonNull Context context) {
        Log.d(LOG_TAG, "findLastAddedAudioFile()");
        final Cursor cursor = context.getContentResolver()
                .query(
                        AUDIO_URI,
                        PROJECTION,
                        null,
                        null,
                        SORT_BY_LAST_ADDED);
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }
}
