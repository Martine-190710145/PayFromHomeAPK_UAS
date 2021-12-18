package com.example.payfromhome.service;

import static com.example.payfromhome.App.CHANNEL_1_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.payfromhome.R;
import com.example.payfromhome.view.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;
import java.util.Random;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Intent intent = new Intent(this, MainActivity.class);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.img_logo)
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(notificationId, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        String channelName = getString(R.string.channel_name);
        String channelDescription = getString(R.string.channel_desc);

        NotificationChannel adminChannel = new NotificationChannel(CHANNEL_1_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(channelDescription);
        adminChannel.enableLights(true);
        adminChannel.enableVibration(true);
        notificationManager.createNotificationChannel(adminChannel);
    }
}