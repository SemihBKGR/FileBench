package com.semihbkgr.filebench.android.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.semihbkgr.filebench.android.AppContext;
import com.semihbkgr.filebench.android.R;
import com.semihbkgr.filebench.android.model.Bench;
import com.semihbkgr.filebench.android.model.File;

import java.util.Date;
import java.util.List;

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
        fileListView=findViewById(R.id.fileListView);
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
        benchCreationTimeTextView.setText(AppContext.instance.dateFormat.format(new Date(bench.getCreationTimeMs())));
        benchExpirationTimeTextView.setText(AppContext.instance.dateFormat.format(new Date(bench.getExpirationTimeMs())));
        benchViewCountTextView.setText(String.valueOf(bench.getViewCount()));
        if(bench.getFiles()!=null)
            fileListView.setAdapter(new FileListAdapter(this,bench.getFiles()));
    }

    private static class FileListAdapter extends ArrayAdapter<File>{

        public FileListAdapter(@NonNull Context context, @NonNull List<File> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(getContext()).inflate(R.layout.one_line_file,parent,false);
            File file=getItem(position);
            listItem.<TextView>findViewById(R.id.fileNameTextView).setText(file.getName());
            listItem.<TextView>findViewById(R.id.fileDescriptionTextView).setText(file.getDescription());
            listItem.<TextView>findViewById(R.id.fileLabelTextView).setText(file.getLabel());
            listItem.<TextView>findViewById(R.id.fileSizeTextView).setText(String.valueOf(file.getSize()));
            return listItem;
        }

    }

}