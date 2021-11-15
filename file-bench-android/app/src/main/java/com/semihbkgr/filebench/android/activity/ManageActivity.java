package com.semihbkgr.filebench.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.semihbkgr.filebench.android.AppContext;
import com.semihbkgr.filebench.android.R;
import com.semihbkgr.filebench.android.model.Bench;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageActivity extends AppCompatActivity {

    private static final int SELECT_PICTURES = 1;
    private static final String TAG = ManageActivity.class.getName();

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView creationTimeTextView;
    private Button addButton;
    private Button uploadButton;
    private GridView gridView;

    private Bench bench;
    private Set<Uri> uriSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        Log.i(TAG, "onCreate");

        //find components
        nameTextView = findViewById(R.id.tvName);
        descriptionTextView = findViewById(R.id.tvDescription);
        creationTimeTextView = findViewById(R.id.tvCreationTime);
        addButton = findViewById(R.id.addButton);
        uploadButton = findViewById(R.id.uploadButton);
        //fileListView = findViewById(R.id.fileListView);
        gridView=findViewById(R.id.imageGrid);

        //get bench
        this.bench = getIntent().getParcelableExtra(AppContext.Constants.INTENT_EXTRA_BENCH);
        if (bench == null) {
            Intent intent = new Intent(this, CreateActivity.class);
            startActivity(intent);
        }
        this.uriSet = new HashSet<>();

        //set listeners
        addButton.setOnClickListener(this::onAddButtonClick);
        uploadButton.setOnClickListener(this::onUploadButtonClick);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @SuppressWarnings("deprecation")
    private void onAddButtonClick(View v) {
        Log.i(TAG, "onAddButtonClick: button clicked");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
    }

    private void onUploadButtonClick(View v) {
        Log.i(TAG, "onUploadButtonClick: button clicked");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        Log.i(TAG, "onActivityResult: resultCode: " + resultCode + ", requestCode: " + requestCode);
        if (requestCode == SELECT_PICTURES) {
            if (resultCode == Activity.RESULT_OK) {
                if (resultData.getClipData() != null) {
                    List<Uri> uriList = new ArrayList<>();
                    int count = resultData.getClipData().getItemCount();
                    int currentItem = 0;
                    while (currentItem < count) {
                        Uri uri = resultData.getClipData().getItemAt(currentItem).getUri();
                        uriList.add(uri);
                        currentItem++;
                    }
                    uriSet.addAll(uriList);
                } else if (resultData.getData() != null) {
                    Uri uri = resultData.getData();
                    uriSet.add(uri);
                }
                FileListCreateViewAdapter adapter = new FileListCreateViewAdapter(this, new ArrayList<>(uriSet));
                gridView.setAdapter(adapter);

            }
        }
    }

    private static class FileListCreateViewAdapter extends ArrayAdapter<Uri> {

        public FileListCreateViewAdapter(@NonNull Context context, @NonNull List<Uri> objects) {
            super(context, R.layout.one_line_file_create, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Uri uri = getItem(position);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.one_line_file_create, parent, false);
            convertView.<ImageView>findViewById(R.id.imageView).setImageURI(uri);
            return convertView;
        }

    }

}