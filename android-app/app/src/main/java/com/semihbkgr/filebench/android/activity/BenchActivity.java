package com.semihbkgr.filebench.android.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.semihbkgr.filebench.android.model.File;
import com.semihbkgr.filebench.android.net.BenchClient;
import com.semihbkgr.filebench.android.net.ClientCallback;
import com.semihbkgr.filebench.android.net.ErrorModel;

import java.io.*;
import java.util.Date;
import java.util.List;

public class BenchActivity extends AppCompatActivity {

    private static final String TAG = BenchActivity.class.getName();

    private TextView idTextView;
    private TextView nameTextView;
    private TextView creationTimeTextView;
    private TextView expirationTimeTextView;
    private GridView gridView;

    private Bench bench;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bench);
        idTextView = findViewById(R.id.idTextView);
        nameTextView = findViewById(R.id.nameTextView);
        creationTimeTextView = findViewById(R.id.creationTimeTextView);
        expirationTimeTextView = findViewById(R.id.expirationTimeTextView);
        gridView = findViewById(R.id.imageGrid);

        Bench bench = getIntent().getParcelableExtra(AppContext.Constants.INTENT_EXTRA_BENCH);
        if (bench == null) {
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, BenchActivity.class);
            startActivity(intent);
        }
        idTextView.setText(bench.getId());
        nameTextView.setText(bench.getName() != null && !bench.getName().isEmpty() ? bench.getName() : "<empty>");
        creationTimeTextView.setText(AppContext.INSTANCE.dateFormat.format(new Date(bench.getCreationTimeMs())));
        expirationTimeTextView.setText(AppContext.INSTANCE.dateFormat.format(new Date(bench.getCreationTimeMs() + bench.getExpirationDurationMs())));

        ArrayAdapter<File> adapter = new FileListViewAdapter(this, bench.getFiles(), bench, AppContext.INSTANCE.benchClient, AppContext.INSTANCE.handler);
        gridView.setAdapter(adapter);

    }


    private static class FileListViewAdapter extends ArrayAdapter<File> {

        private final Bench bench;
        private final BenchClient benchClient;
        private final Handler handler;

        public FileListViewAdapter(@NonNull Context context, @NonNull List<File> objects,
                                   Bench bench, BenchClient benchClient, Handler handler) {
            super(context, R.layout.item_file, objects);
            this.bench = bench;
            this.benchClient = benchClient;
            this.handler = handler;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            File file = getItem(position);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_file, parent, false);
            ImageView imageView = convertView.findViewById(R.id.imageView);
            benchClient.getFile(bench.getId(), file.getId(), new ClientCallback<InputStream>() {
                @Override
                public void success(InputStream data) {
                    Log.i(TAG, "success: image loaded successfully");
                    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                    byte[] buffer = new byte[2048];
                    int i;
                    try {
                        while ((i = data.read(buffer)) != -1)
                            byteBuffer.write(buffer, 0, i);
                        imageView.post(() -> {
                            byte[] b = byteBuffer.toByteArray();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            imageView.setImageBitmap(bitmap);
                            System.out.println(imageView.getWidth());
                            imageView.setOnClickListener(view -> {
                                Log.i(TAG, "getView: image clicked");
                                Dialog dialog = new Dialog(getContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.dialog_image);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                ImageView fullImageview = dialog.findViewById(R.id.imageView);
                                fullImageview.setImageBitmap(bitmap);
                                fullImageview.setOnClickListener(v -> dialog.dismiss());
                                dialog.show();
                            });
                        });
                        imageView.setOnLongClickListener(v -> {
                            handler.post(() -> {
                                byte[] b = byteBuffer.toByteArray();
                                java.io.File photo = new java.io.File(getContext().getExternalFilesDir("filebench"), file.getName());
                                try (FileOutputStream fos = new FileOutputStream(photo.getPath())) {
                                    fos.write(b);
                                    fos.flush();
                                    imageView.post(() -> Toast.makeText(getContext(), photo.getName(), Toast.LENGTH_SHORT).show());
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            return true;
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(ErrorModel errorModel) {
                    Log.w(TAG, "error: error while loading image");
                }

                @Override
                public void fail(Throwable t) {
                    Log.e(TAG, "fail: fail while loading file", t);
                }
            });
            return convertView;
        }

    }

}