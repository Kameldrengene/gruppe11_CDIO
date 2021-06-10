package com.example.gruppe11_cdio;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;
    private TextView loading;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    void startLoadingDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_load_screen, null);
        loading = view.findViewById(R.id.textView);
        if(message != null) loading.setText(message);
        builder.setView(view);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }
}
