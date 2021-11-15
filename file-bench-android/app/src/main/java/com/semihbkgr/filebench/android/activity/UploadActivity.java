package com.semihbkgr.filebench.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.semihbkgr.filebench.android.AppContext;
import com.semihbkgr.filebench.android.R;
import com.semihbkgr.filebench.android.model.Bench;

import java.util.*;

public class UploadActivity extends AppCompatActivity {

    private static final int SELECT_PICTURES = 1;
    private static final String TAG = UploadActivity.class.getName();

    private TextView nameTextView;
    private TextView creationTimeTextView;
    private TextView expirationTimeTextView;
    private Button addButton;
    private Button uploadButton;
    private GridView gridView;

    private Bench bench;
    private Set<Uri> uriSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Log.i(TAG, "onCreate");

        //find components
        nameTextView = findViewById(R.id.nameTextView);
        creationTimeTextView = findViewById(R.id.creationTimeTextView);
        expirationTimeTextView = findViewById(R.id.expirationTimeTextView);
        addButton = findViewById(R.id.addButton);
        uploadButton = findViewById(R.id.uploadButton);
        gridView = findViewById(R.id.imageGrid);

        //get bench
        this.bench = getIntent().getParcelableExtra(AppContext.Constants.INTENT_EXTRA_BENCH);
        if (bench == null) {
            Intent intent = new Intent(this, CreateActivity.class);
            startActivity(intent);
        }
        nameTextView.setText(bench.getName());
        creationTimeTextView.setText(AppContext.instance.dateFormat.format(new Date(bench.getCreationTimeMs())));
        expirationTimeTextView.setText(AppContext.instance.dateFormat.format(new Date(bench.getCreationTimeMs() + bench.getExpirationDurationMs())));

        this.uriSet = new HashSet<>();

        //set listeners
        addButton.setOnClickListener(this::onAddButtonClick);
        uploadButton.setOnClickListener(this::onUploadButtonClick);
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
                List<Uri> uriList = new ArrayList<>();
                if (resultData.getClipData() != null) {
                    int count = resultData.getClipData().getItemCount();
                    int currentItem = 0;
                    while (currentItem < count) {
                        Uri uri = resultData.getClipData().getItemAt(currentItem).getUri();
                        uriList.add(uri);
                        currentItem++;
                    }
                } else if (resultData.getData() != null) {
                    Uri uri = resultData.getData();
                    uriList.add(uri);
                }
                uriSet.addAll(uriList);
                FileListCreateViewAdapter adapter = (FileListCreateViewAdapter) gridView.getAdapter();
                if (adapter == null) {
                    adapter = new FileListCreateViewAdapter(this, new ArrayList<>(uriSet));
                    gridView.setAdapter(adapter);
                }else{
                    uriList.removeAll(uriSet);
                    adapter.addAll(uriList.toArray(new Uri[0]));
                }
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
            ImageView imageView = convertView.findViewById(R.id.imageView);
            imageView.setImageURI(uri);
            imageView.setOnLongClickListener(v -> {
                remove(uri);
                return false;
            });
            imageView.setOnClickListener(view -> {
                Log.i(TAG, "getView: image clicked");
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_image);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                ImageView fullImageview = dialog.findViewById(R.id.imageView);
                fullImageview.setImageURI(uri);
                fullImageview.setOnClickListener(v -> dialog.dismiss());
                dialog.show();
            });
            return convertView;
        }

    }

}