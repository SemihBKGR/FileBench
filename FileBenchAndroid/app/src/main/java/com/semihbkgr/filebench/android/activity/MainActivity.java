package com.semihbkgr.filebench.android.activity;

import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.semihbkgr.filebench.android.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG=MainActivity.class.getName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Timer("StartAppTimerThread",true).schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()->{
                    Intent intent=new Intent(MainActivity.this,MenuActivity.class);
                    startActivity(intent);
                    Log.i(TAG, "run: MenuActivity has been started");
                });
            }
        }, 1000L);
        
    }

}