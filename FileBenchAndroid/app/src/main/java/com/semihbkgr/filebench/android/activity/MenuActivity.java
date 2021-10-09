package com.semihbkgr.filebench.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.semihbkgr.filebench.android.AppContext;
import com.semihbkgr.filebench.android.R;
import com.semihbkgr.filebench.android.model.Bench;
import com.semihbkgr.filebench.android.net.ClientCallback;
import com.semihbkgr.filebench.android.net.ErrorModel;

public class MenuActivity extends AppCompatActivity {

    private EditText benchIdEditText;
    private Button getBenchButton;
    private Button createBenchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        benchIdEditText = findViewById(R.id.benchIdEditText);
        getBenchButton = findViewById(R.id.getBenchButton);
        createBenchButton = findViewById(R.id.createBenchButton);

        getBenchButton.setOnClickListener(this::onGetBenchButtonClicked);
        createBenchButton.setOnClickListener(this::onCreateBenchButtonClicked);

    }

    private void onGetBenchButtonClicked(View view) {
        String benchId = benchIdEditText.getEditableText().toString();
        if (benchId.length() == AppContext.Constants.BENCH_ID_LENGTH) {
            Toast.makeText(this, "Bench id length must be 7", Toast.LENGTH_SHORT).show();
            AppContext.instance.benchClient.getBench(benchId, new ClientCallback<Bench>() {
                @Override
                public void success(Bench data) {
                    runOnUiThread(() -> Toast.makeText(MenuActivity.this, "Success", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void error(ErrorModel errorModel) {
                    runOnUiThread(() -> Toast.makeText(MenuActivity.this, "Error", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void fail(Throwable t) {
                    runOnUiThread(() -> Toast.makeText(MenuActivity.this, "Fail", Toast.LENGTH_SHORT).show());
                }
            });
        } else
            Toast.makeText(this, "Bench id length must be 7", Toast.LENGTH_SHORT).show();
    }

    private void onCreateBenchButtonClicked(View view) {

    }

}