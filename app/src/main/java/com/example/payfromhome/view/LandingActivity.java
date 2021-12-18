package com.example.payfromhome.view;

import static com.example.payfromhome.App.CHANNEL_1_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.payfromhome.R;
import com.example.payfromhome.receiver.NotificationReceiver;

public class LandingActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;

    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        notificationManager = NotificationManagerCompat.from(this);

        String title = getString(R.string.judul_notif);
        String message = getString(R.string.isi_notif);

        sendNotification(title, message);

        btnOk = findViewById(R.id.button_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, HistoryActivity.class));
                finish();
            }
        });
    }

    private void sendNotification(String title, String message) {

        Intent intent = new Intent(this, LandingActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("message", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.img_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, title, actionIntent)
                .build();

        notificationManager.notify(1, notification);
    }
}