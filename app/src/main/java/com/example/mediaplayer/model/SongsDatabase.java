package com.example.mediaplayer.model;

import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mediaplayer.utilities.UriConvertor;

@Database(entities = {SongEntry.class}, version = 1, exportSchema = false)
@TypeConverters(UriConvertor.class)
public abstract class SongsDatabase extends RoomDatabase {

    private static final String LOG_TAG = SongsDatabase.class.getSimpleName();
    private static Object LOCK = new Object();
    private static final String DATABASE_NAME = "songs";
    private static SongsDatabase sInstance;

    public static SongsDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        SongsDatabase.class, SongsDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract SongDao songDao();

}
