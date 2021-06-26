package com.matianyi.accountingapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.matianyi.accountingapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class StartScreenActivity extends Activity {

    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        init();
    }

    private void init() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                toMainActivity();
            }
        }, 2 * 1000);
    }

    private void toMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
