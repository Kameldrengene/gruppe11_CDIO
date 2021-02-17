package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(OpenCVLoader.initDebug()){
            Toast.makeText(this,"open cv installed",Toast.LENGTH_SHORT);
            System.out.println("open cv installed");
        }else
        {
            Toast.makeText(this,"open cv not installed",Toast.LENGTH_SHORT);
            System.out.println("open cv not installed");
        }
    }
}