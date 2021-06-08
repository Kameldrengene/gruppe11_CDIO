package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

public class LoadScreen extends AppCompatActivity {

    //  This static variable is used to close this activity from another
    public static Activity load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);
        load = this;
    }
}