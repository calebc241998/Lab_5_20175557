package com.example.lab_5_20175557;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

// NotificationHelper.java
public class NotificationHelper extends ContextWrapper {
    public static final String CHANNEL_HIGH_ID = "channel_high";
    public static final String CHANNEL_DEFAULT_ID = "channel_default";
    public static final String CHANNEL_LOW_ID = "channel_low";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel highChannel = new NotificationChannel(
                    CHANNEL_HIGH_ID,
                    "High Importance Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            highChannel.setDescription("This is the high importance channel");

            NotificationChannel defaultChannel = new NotificationChannel(
                    CHANNEL_DEFAULT_ID,
                    "Default Importance Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            defaultChannel.setDescription("This is the default importance channel");

            NotificationChannel lowChannel = new NotificationChannel(
                    CHANNEL_LOW_ID,
                    "Low Importance Channel",
                    NotificationManager.IMPORTANCE_LOW);
            lowChannel.setDescription("This is the low importance channel");

            getManager().createNotificationChannel(highChannel);
            getManager().createNotificationChannel(defaultChannel);
            getManager().createNotificationChannel(lowChannel);
        }
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message, int importance) {
        String channelId;
        switch (importance) {
            case NotificationManager.IMPORTANCE_HIGH:
                channelId = CHANNEL_HIGH_ID;
                break;
            case NotificationManager.IMPORTANCE_LOW:
                channelId = CHANNEL_LOW_ID;
                break;
            case NotificationManager.IMPORTANCE_DEFAULT:
            default:
                channelId = CHANNEL_DEFAULT_ID;
        }

        return new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(importance);
    }
}

