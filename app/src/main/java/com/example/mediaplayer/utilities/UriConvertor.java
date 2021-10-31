package com.example.mediaplayer.utilities;

import android.net.Uri;
import android.util.Log;

import androidx.room.TypeConverter;

public class UriConvertor {

    private static final String LOG_TAG = UriConvertor.class.getSimpleName();
    private static final String EMPTY_URI = "N/A";


    @TypeConverter
    public static Uri toUri(String uri) {
        Log.d(LOG_TAG, String.format("toUri(uri = {})", uri));
        if (uri != EMPTY_URI) {
            return Uri.parse(uri);
        }
        return Uri.EMPTY;
    }

    @TypeConverter
    public static String toString(Uri uri) {
        if(uri != null) {
            Log.d(LOG_TAG, String.format("toString(uri = {})", uri.toString()));
            return uri.toString();
        }
        Log.d(LOG_TAG, "toString(uri = null)");
        return EMPTY_URI;
    }
}