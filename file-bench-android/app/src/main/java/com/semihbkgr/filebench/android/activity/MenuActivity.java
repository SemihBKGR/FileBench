package com.semihbkgr.filebench.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.semihbkgr.filebench.android.AppContext;
import com.semihbkgr.filebench.android.R;
import com.semihbkgr.filebench.android.activity.dialog.LoadingDialog;
import com.semihbkgr.filebench.android.model.Bench;
import com.semihbkgr.filebench.android.net.ClientCallback;
import com.semihbkgr.filebench.android.net.ErrorModel;
import com.semihbkgr.filebench.android.util.TextWatcherAdapter;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = MenuActivity.class.getName();

    private EditText benchIdEditText;
    private ImageButton downloadBenchButton;
    private ImageButton uploadBenchButton;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        benchIdEditText = findViewById(R.id.benchIdEditText);
        downloadBenchButton = findViewById(R.id.downloadBenchButton);
        uploadBenchButton = findViewById(R.id.uploadBenchButton);
        downloadBenchButton.setOnClickListener(this::onDownloadBenchButtonClicked);
        uploadBenchButton.setOnClickListener(this::onUploadBenchButtonClicked);
        benchIdEditText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                setDownloadButtonActivation(s.length() == AppContext.Constants.BENCH_ID_LENGTH);
            }
        });
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDownloadButtonActivation(false);
    }

    private void onDownloadBenchButtonClicked(View view) {
        String benchId = benchIdEditText.getEditableText().toString();
        if (benchId.length() == AppContext.Constants.BENCH_ID_LENGTH) {
            loadingDialog.startLoading();
            AppContext.instance.benchClient.getBench(benchId, new ClientCallback<Bench>() {
                @Override
                public void success(Bench data) {
                    runOnUiThread(() -> {
                        Log.i(TAG, "success: Getting bench is successful, " + data);
                        loadingDialog.getDialog().setCancelable(true);
                        loadingDialog.getProgressBar().setVisibility(View.GONE);
                        loadingDialog.getLoadButton().setVisibility(View.VISIBLE);
                        loadingDialog.getMessageTextView().setTextColor(getColor(R.color.green));
                        loadingDialog.getMessageTextView().setText(data.getName());
                        loadingDialog.getLoadButton().setOnClickListener(v->{
                            Intent intent = new Intent(MenuActivity.this, BenchActivity.class);
                            intent.putExtra(AppContext.Constants.INTENT_EXTRA_BENCH, data);
                            startActivity(intent);
                        });
                    });
                }

                @Override
                public void error(ErrorModel errorModel) {
                    runOnUiThread(() -> {
                        Log.i(TAG, "error: Error while getting bench, " + errorModel);
                        loadingDialog.getDialog().setCancelable(true);
                        loadingDialog.getProgressBar().setVisibility(View.GONE);
                        loadingDialog.getMessageTextView().setText(R.string.connection_error_message);
                        loadingDialog.getMessageTextView().setTextColor(getColor(R.color.red));
                    });
                }

                @Override
                public void fail(Throwable t) {
                    runOnUiThread(() -> {
                        Log.w(TAG, "fail: Fail while getting bench", t);
                        loadingDialog.getDialog().setCancelable(true);
                        loadingDialog.getProgressBar().setVisibility(View.GONE);
                        loadingDialog.getMessageTextView().setText(R.string.connection_fail_message);
                        loadingDialog.getMessageTextView().setTextColor(getColor(R.color.red));
                    });
                }
            });
        } else
            Toast.makeText(this, "Bench id length must be " + AppContext.Constants.BENCH_ID_LENGTH, Toast.LENGTH_SHORT).show();
    }

    private void onUploadBenchButtonClicked(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    private void setDownloadButtonActivation(boolean activated) {
        if (activated) {
            Log.i(TAG, "setDownloadButtonActivation: downloadButton has been activated");
            downloadBenchButton.setClickable(true);
            downloadBenchButton.setImageDrawable(getDrawable(R.drawable.download_active));
        } else {
            Log.i(TAG, "setDownloadButtonActivation: downloadButton has been inactivated");
            downloadBenchButton.setClickable(false);
            downloadBenchButton.setImageDrawable(getDrawable(R.drawable.download_inactive));
        }
    }


}