package com.f17.csci5539.team5.ceitmajorselectionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        intent = new Intent(this, LoginActivity.class);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // buffer
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
