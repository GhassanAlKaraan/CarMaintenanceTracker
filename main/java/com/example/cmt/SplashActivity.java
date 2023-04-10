package com.example.cmt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                if (sp.contains("user_name") && sp.contains("vehicle_name")) {
                    startActivity(new Intent(SplashActivity.this, RecordActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }

                finish();
            }
        }, 1500);
        //
    }
}