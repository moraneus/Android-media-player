package com.example.mediaplayer.utilities;

import android.content.ContentUris;
import android.net.Uri;
import android.util.Log;

public class AudioThumbnailImageExtractor {

    private static final String LOG_TAG = AudioThumbnailImageExtractor.class.getSimpleName();
    private static final Uri ALBUM_ART_URI = Uri.parse("content://media/external/audio/albumart");

    public static Uri getImageUri(Long albumId) {
        Uri imageUri = ContentUris.withAppendedId(ALBUM_ART_URI, albumId);
        Log.d(LOG_TAG, String.format("getImageUri(ImageUri = {})", imageUri.toString()));
        return imageUri;
    }
}
