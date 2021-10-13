package com.semihbkgr.filebench.android.activity;

import android.content.Intent;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.semihbkgr.filebench.android.AppContext;
import com.semihbkgr.filebench.android.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG=MainActivity.class.getName();

    private ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoImageView=findViewById(R.id.logoImageView);
        AppContext.initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new LinearInterpolator());
        fadeIn.setDuration(1000);
        logoImageView.setAnimation(fadeIn);
        new Timer("StartAppTimerThread",true).schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()-> startActivity(new Intent(MainActivity.this,MenuActivity.class)));
            }
        },AppContext.Constants.MAIN_ACTIVITY_LOGO_TIME_MS);
    }

}