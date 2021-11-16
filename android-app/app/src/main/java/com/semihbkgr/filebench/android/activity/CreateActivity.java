package com.semihbkgr.filebench.android.activity;

import android.content.Intent;
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

import java.util.Locale;

import static com.semihbkgr.filebench.android.AppContext.Constants.INTENT_EXTRA_BENCH;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = CreateActivity.class.getName();

    private EditText nameEditText;
    private Spinner expirationTimeSpinner;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        nameEditText = findViewById(R.id.nameEditText);
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
        Durations duration = (Durations) expirationTimeSpinner.getSelectedItem();
        Bench bench = new Bench();
        bench.setName(name);
        bench.setExpirationDurationMs(duration.getMillis());
        AppContext.INSTANCE.benchClient.createBench(bench, new ClientCallback<Bench>() {
            @Override
            public void success(Bench data) {
                Log.i(TAG, "success: bench has been created successfully");
                Intent intent = new Intent(CreateActivity.this, UploadActivity.class);
                intent.putExtra(INTENT_EXTRA_BENCH, data);
                runOnUiThread(() -> {
                    startActivity(intent);
                    createButton.setClickable(true);
                });
            }

            @Override
            public void error(ErrorModel errorModel) {
                Log.w(TAG, "error: error while creating bench, message: " + errorModel.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(CreateActivity.this, errorModel.getMessage(), Toast.LENGTH_SHORT).show();
                    createButton.setClickable(true);
                });
            }

            @Override
            public void fail(Throwable t) {
                Log.e(TAG, "fail: fail while creating bench");
                t.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(CreateActivity.this, "Fail while create bench", Toast.LENGTH_SHORT).show();
                    createButton.setClickable(true);
                });
            }
        });
    }

    private enum Durations {

        H1(1),
        H3(3),
        H9(9);

        public final int hour;

        Durations(int hour) {
            this.hour = hour;
        }

        public long getMillis() {
            return hour * 3_600_000L;
        }

        @Override
        public String toString() {
            if (hour == 1)
                return String.format(Locale.getDefault(), "%d Hour", hour);
            return String.format(Locale.getDefault(), "%d Hours", hour);
        }

    }

}