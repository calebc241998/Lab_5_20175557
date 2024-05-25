package com.example.lab_5_20175557;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("notificationId", 0);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        int importance = intent.getIntExtra("importance", NotificationManager.IMPORTANCE_DEFAULT);

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder builder = notificationHelper.getChannelNotification(title, message, importance);
        notificationHelper.getManager().notify(id, builder.build());
    }
}

