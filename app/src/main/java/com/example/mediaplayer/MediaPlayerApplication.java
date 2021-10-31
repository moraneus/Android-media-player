package com.example.mediaplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class MediaPlayerApplication extends Application {

    public static final String CHANNEL_ID1 = "channel1";
    public static final String CHANNEL_ID2 = "channel2";
    public static final String ACTION_PREVIOUS = "actionprevious";
    public static final String ACTION_NEXT = "actionnext";
    public static final String ACTION_PLAY = "actionplay";


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel notificationChannel1 = new NotificationChannel(
                CHANNEL_ID1,
                "channel1",
                NotificationManager.IMPORTANCE_HIGH
        );
        notificationChannel1.setDescription("Channel 1....");

        NotificationChannel notificationChannel2 = new NotificationChannel(
                CHANNEL_ID2,
                "channel2",
                NotificationManager.IMPORTANCE_HIGH
        );
        notificationChannel1.setDescription("Channel 2....");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel1);
        notificationManager.createNotificationChannel(notificationChannel2);
    }

    public MediaPlayerApplication() {
    }
}

