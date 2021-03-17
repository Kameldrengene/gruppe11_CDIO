package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TestStack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_stack);

        //Find device width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        //Calculate
        double imageRatio = 251/180;
        int height = (int) (width/7*imageRatio)+50;

        //For all 7 rows
        for (int i = 0; i < 7; i++) {

            //Find framelayout
            String frameID = "frame" + i;
            int resID = getResources().getIdentifier(frameID, "id", getPackageName());
            FrameLayout myLayout = findViewById(resID);

            //Create cards
            ArrayList<ImageView> cards = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                ImageView card = new ImageView(this);
                card.setImageResource(R.drawable.clubs_2);
                card.setPadding(10,0, 10,0);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width/7, height);
                params.setMargins(0, 50+j*50, 0, 0);
                card.setLayoutParams(params);
                cards.add(card);
            }

            //Add cards to layout
            for (int j = 0; j < 10; j++) {
                myLayout.addView(cards.get(j));
            }

            //Set column listener
            myLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Pressed " + frameID, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}