package com.example.payfromhome.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.payfromhome.R;
import com.example.payfromhome.helper.SharedPreferenceHelper;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferenceHelper preferenceHelper = new SharedPreferenceHelper(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferenceHelper.getSessionId().equals("") || preferenceHelper.getSessionId().equals(null)) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}