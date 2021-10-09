package com.semihbkgr.filebench.android.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.semihbkgr.filebench.android.R;

public class MenuActivity extends AppCompatActivity {

    private EditText benchIdEditText;
    private Button getBenchButton;
    private Button createBenchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        benchIdEditText=findViewById(R.id.benchIdEditText);
        getBenchButton =findViewById(R.id.getBenchButton);
        createBenchButton =findViewById(R.id.createBenchButton);

        getBenchButton.setOnClickListener(this::onGetBenchButtonClicked);
        createBenchButton.setOnClickListener(this::onCreateBenchButtonClicked);

    }

    private void onGetBenchButtonClicked(View view){

    }

    private void onCreateBenchButtonClicked(View view){

    }

}