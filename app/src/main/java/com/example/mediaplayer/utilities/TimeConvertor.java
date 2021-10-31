package com.example.mediaplayer.utilities;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

public class TimeConvertor {

    @SuppressLint("DefaultLocale")
    static public String LongMillisecondsToDuration(Long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    @SuppressLint("DefaultLocale")
    static public String IntSecondsToDuration(int seconds) {
        return String.format("%02d:%02d",
                TimeUnit.SECONDS.toMinutes(seconds),
                TimeUnit.SECONDS.toSeconds(seconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds))
        );
    }
}
