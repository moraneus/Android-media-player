package com.example.mediaplayer.model;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

// Annotate the class with Entity.
// Use "song" for the table name
@Entity(tableName = "song")
public class SongEntry {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "song_uri")
    private Uri fileLocation;

    @ColumnInfo(name = "song_title")
    private String title;

    @ColumnInfo(name = "song_artist")
    private String artist;

    @ColumnInfo(name = "song_duration")
    private Long duration;

    @ColumnInfo(name = "song_image_uri")
    private Uri imageLocation;

    public SongEntry(
            @NotNull Uri fileLocation,
            @NotNull String title,
            String artist,
            Long duration,
            Uri imageLocation) {
        this.fileLocation = fileLocation;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.imageLocation = imageLocation;
    }

    @NonNull
    public Uri getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(@NonNull Uri fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Uri getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(Uri imageLocation) {
        this.imageLocation = imageLocation;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}