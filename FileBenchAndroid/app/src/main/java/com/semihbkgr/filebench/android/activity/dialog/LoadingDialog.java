package com.semihbkgr.filebench.android.activity.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.semihbkgr.filebench.android.R;

public class LoadingDialog {

    private final Activity activity;
    private AlertDialog dialog;

    private ProgressBar progressBar;
    private TextView messageTextView;
    private Button loadButton;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v=inflater.inflate(R.layout.dialog_loading, null);
        builder.setView(v);
        builder.setCancelable(false);
        this.dialog = builder.create();
        this.progressBar = v.findViewById(R.id.progressBar);
        this.messageTextView = v.findViewById(R.id.messageTextView);
        this.loadButton=v.findViewById(R.id.loadButton);
        loadButton.setVisibility(View.GONE);
        dialog.setOnCancelListener(DialogInterface::dismiss);
        dialog.show();
    }

    public void dismissLoading() {
        dialog.dismiss();
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public Button getLoadButton(){
        return loadButton;
    }

}
