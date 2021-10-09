package com.semihbkgr.filebench.android.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.semihbkgr.filebench.android.R;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG=CreateActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

}