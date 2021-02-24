package com.example.gruppe11_cdio.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.gruppe11_cdio.R;

public class KabaleDialog extends Dialog {
    View view;
    Button button1,button2;
    TextView title,body;

    public KabaleDialog(@NonNull Context context, int buttonsCount) {
        super(context);
        if(buttonsCount==1) {
            this.view = getLayoutInflater().inflate(R.layout.popup1button, null);
            title=view.findViewById(R.id.alertTitle);
            body=view.findViewById(R.id.alertBody);
            button1 = view.findViewById(R.id.alertButton);
            button1.setOnClickListener((View.OnClickListener) context);
        }
        else if(buttonsCount==2) {
            this.view = getLayoutInflater().inflate(R.layout.popup2button, null);
            title=view.findViewById(R.id.popup2Title);
            body=view.findViewById(R.id.popup2Body);
            button1 = view.findViewById(R.id.popup2button);
            button1.setOnClickListener((View.OnClickListener) context);
            button2 = view.findViewById(R.id.popup2button1);
            button2.setOnClickListener((View.OnClickListener) context);
        }

        this.setContentView(view);
    }

    public void setTitle(String text){
        title.setText(text);
    }
    public void setBody(String text){
        body.setText(text);
    }

    public Button getbutton1(){
        return button1;
    }

    public Button getbutton2(){
        if(button2!=null)
        return button2;
        else
            return null;
    }
}
