package com.semihbg.filebench.android;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends AppCompatActivity {

    private EditText benchIdEditText;

    private Button getBenchButton;
    private Button createBenchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Find Views
        getBenchButton=findViewById(R.id.getBenchButton);

        getBenchButton.setOnClickListener(this::onGetBenchButtonClicked);

    }


    private void onGetBenchButtonClicked(View view){

    }

}