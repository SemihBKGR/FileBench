package com.semihbkgr.filebench.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.semihbkgr.filebench.android.AppContext;
import com.semihbkgr.filebench.android.R;
import com.semihbkgr.filebench.android.model.Bench;
import com.semihbkgr.filebench.android.net.ClientCallback;
import com.semihbkgr.filebench.android.net.ErrorModel;

public class MenuActivity extends AppCompatActivity {

    private EditText benchIdEditText;
    private ImageButton downloadBenchButton;
    private ImageButton uploadBenchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        benchIdEditText = findViewById(R.id.benchIdEditText);
        downloadBenchButton = findViewById(R.id.downloadBenchButton);
        uploadBenchButton = findViewById(R.id.uploadBenchButton);
        downloadBenchButton.setOnClickListener(this::onDownloadBenchButtonClicked);
        uploadBenchButton.setOnClickListener(this::onUploadBenchButtonClicked);
    }

    private void onDownloadBenchButtonClicked(View view) {
        String benchId = benchIdEditText.getEditableText().toString();
        if (benchId.length() == AppContext.Constants.BENCH_ID_LENGTH) {
            AppContext.instance.benchClient.getBench(benchId, new ClientCallback<Bench>() {
                @Override
                public void success(Bench data) {
                    runOnUiThread(() -> {
                        Toast.makeText(MenuActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MenuActivity.this, BenchActivity.class);
                        intent.putExtra(AppContext.Constants.INTENT_EXTRA_BENCH, data);
                        startActivity(intent);
                    });
                }

                @Override
                public void error(ErrorModel errorModel) {
                    runOnUiThread(() -> Toast.makeText(MenuActivity.this, "Error", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void fail(Throwable t) {
                    runOnUiThread(() -> Toast.makeText(MenuActivity.this, "Fail", Toast.LENGTH_SHORT).show());
                    t.printStackTrace();
                }
            });
        } else
            Toast.makeText(this, "Bench id length must be 7", Toast.LENGTH_SHORT).show();
    }

    private void onUploadBenchButtonClicked(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

}