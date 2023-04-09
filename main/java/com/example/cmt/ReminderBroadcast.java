package com.example.cmt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    String title;



    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("title");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "cartracker")
                .setSmallIcon(R.drawable.car_icon)
                .setContentTitle(title)
                .setContentText("Remember to check this record!")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);



        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        long notificationId = System.currentTimeMillis(); // generate a unique notification ID
        notificationManager.notify((int) notificationId, builder.build()); // cast long to int for notification ID
    }
}
