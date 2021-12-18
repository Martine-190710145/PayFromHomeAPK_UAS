package com.example.payfromhome;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessaging;

public class App extends Application {
    public static final String CHANNEL_1_ID = "channel1";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().subscribeToTopic("success_payment");
    }
}
