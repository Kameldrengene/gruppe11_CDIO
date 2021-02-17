package com.example.gruppe11_cdio.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

public class KabaleDialog extends Dialog {
    View view;
    public KabaleDialog(@NonNull Context context, View view) {
        super(context);
        this.view = view;
    }


}
