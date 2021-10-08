package com.semihbkgr.filebench.android;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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