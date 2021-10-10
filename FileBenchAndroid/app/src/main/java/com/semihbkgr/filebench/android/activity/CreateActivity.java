package com.semihbkgr.filebench.android.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.semihbkgr.filebench.android.AppContext;
import com.semihbkgr.filebench.android.R;
import com.semihbkgr.filebench.android.model.Bench;
import com.semihbkgr.filebench.android.net.ClientCallback;
import com.semihbkgr.filebench.android.net.ErrorModel;
import com.semihbkgr.filebench.android.net.dto.BenchCreateDto;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = CreateActivity.class.getName();

    private EditText nameEditText;
    private EditText descriptionEditText;
    private Spinner expirationTimeSpinner;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        expirationTimeSpinner = findViewById(R.id.expirationTimeSpinner);
        createButton = findViewById(R.id.createButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        expirationTimeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Durations.values()));
        createButton.setOnClickListener(this::onCreateButtonClicked);
    }

    private void onCreateButtonClicked(View v) {
        Log.i(TAG, "onCreateButtonClicked");
        createButton.setClickable(false);
        String name = nameEditText.getEditableText().toString();
        String description = descriptionEditText.getEditableText().toString();
        Durations duration = (Durations) expirationTimeSpinner.getSelectedItem();
        BenchCreateDto benchCreateDto=new BenchCreateDto(name,description,duration.getMillis());
        AppContext.instance.benchClient.createBench(benchCreateDto, new ClientCallback<Bench>() {
            @Override
            public void success(Bench data) {
                runOnUiThread(() -> Toast.makeText(CreateActivity.this, "Success", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void error(ErrorModel errorModel) {
                runOnUiThread(() -> Toast.makeText(CreateActivity.this, "Error", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void fail(Throwable t) {
                runOnUiThread(() -> Toast.makeText(CreateActivity.this, "Fail", Toast.LENGTH_SHORT).show());
                t.printStackTrace();
            }
        });
    }

    private enum Durations{

        _1H(1),
        _3H(3),
        _9H(9),
        _12H(12),
        _24H(24);

        public final int hour;

        Durations(int hour) {
            this.hour=hour;
        }

        public long getMillis(){
            return hour* 3_600_000L;
        }

        @Override
        public String toString() {
            if(hour==1){
                return String.format("%d Hour",hour);
            return String.format("%d Hours",hour);
        }

    }

}