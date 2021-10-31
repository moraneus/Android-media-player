package com.example.mediaplayer.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao {
    @Query("SELECT * FROM song ORDER BY song_title")
    LiveData<List<SongEntry>> loadAllSongs();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSong(SongEntry songEntry);

    @Delete
    void deleteSong(SongEntry songEntry);
}
