package com.semihbkgr.filebench.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.semihbkgr.filebench.android.AppContext;
import com.semihbkgr.filebench.android.R;
import com.semihbkgr.filebench.android.model.Bench;

import java.util.Date;

public class ManageActivity extends AppCompatActivity {

    private static final String TAG = ManageActivity.class.getName();

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView creationTimeTextView;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        nameTextView = findViewById(R.id.tvName);
        descriptionTextView = findViewById(R.id.tvDescription);
        creationTimeTextView = findViewById(R.id.tvCreationTime);
        addButton=findViewById(R.id.addButton);
        addButton.setOnClickListener(this::onAddButtonClick);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bench bench = getIntent().getParcelableExtra(AppContext.Constants.INTENT_EXTRA_BENCH);
        if (bench == null) {
            Log.w(TAG, "onStart: bench is null");
            Intent intent = new Intent(this, CreateActivity.class);
            startActivity(intent);
        }
        Log.i(TAG, "onStart: bench: " + bench);
        nameTextView.setText(bench.getName());
        descriptionTextView.setText(bench.getDescription());
        creationTimeTextView.setText(AppContext.instance.dateFormat.format(new Date(bench.getCreationTimeMs())));

    }

    private void onAddButtonClick(View v){
        Log.i(TAG, "onAddButtonClick: button clicked");

    }


}