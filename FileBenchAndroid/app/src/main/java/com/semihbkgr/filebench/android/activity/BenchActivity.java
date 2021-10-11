package com.semihbkgr.filebench.android.activity;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.semihbkgr.filebench.android.AppContext;
import com.semihbkgr.filebench.android.R;
import com.semihbkgr.filebench.android.model.Bench;

public class BenchActivity extends AppCompatActivity {

    private static final String TAG=BenchActivity.class.getName();

    private ListView fileListView;
    private TextView benchNameTextView;
    private TextView benchDescriptionTextView;
    private TextView benchCreationTimeTextView;
    private TextView benchExpirationTimeTextView;
    private TextView benchViewCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bench);

        fileListView=findViewById(R.id.fileListView);
        benchNameTextView=findViewById(R.id.benchNameTextView);
        benchDescriptionTextView=findViewById(R.id.benchDescriptionTextView);
        benchCreationTimeTextView =findViewById(R.id.benchCreationTimeTextView);
        benchExpirationTimeTextView =findViewById(R.id.benchExpirationTimeTextView);
        benchViewCountTextView=findViewById(R.id.benchViewCountTextView);

        Bench bench=getIntent().getParcelableExtra(AppContext.Constants.INTENT_EXTRA_BENCH);
        if(bench==null){
            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,MenuActivity.class);
            startActivity(intent);
        } else
            loadBench(bench);

    }

    private void loadBench(@NonNull Bench bench){
        benchNameTextView.setText(bench.getName());
        benchDescriptionTextView.setText(bench.getDescription());
        benchCreationTimeTextView.setText(String.valueOf(bench.getCreationTimeMs()));
        benchExpirationTimeTextView.setText(String.valueOf(bench.getExpirationTimeMs()));
        benchViewCountTextView.setText(String.valueOf(bench.getViewCount()));
    }

}