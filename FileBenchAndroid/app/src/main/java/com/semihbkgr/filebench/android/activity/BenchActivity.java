package com.semihbkgr.filebench.android.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.semihbkgr.filebench.android.R;

public class BenchActivity extends AppCompatActivity {

    private static final String TAG=BenchActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bench);
    }
}