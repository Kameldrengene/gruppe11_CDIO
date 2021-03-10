package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;


import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class GameActivity extends AppCompatActivity implements Frag_GameControls.Controls, Frag_GameEdit.Controls, Frag_GameAnalyze.Controls{

    RelativeLayout relativeLayout1,relativeLayout2,relativeLayout3,relativeLayout4,relativeLayout5,relativeLayout6,relativeLayout7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        relativeLayout1 = findViewById(R.id.relativeLayout1);
        relativeLayout2 = findViewById(R.id.relativeLayout2);
        relativeLayout3 = findViewById(R.id.relativeLayout3);
        relativeLayout4 = findViewById(R.id.relativeLayout4);
        relativeLayout5 = findViewById(R.id.relativeLayout5);
        relativeLayout6 = findViewById(R.id.relativeLayout6);
        relativeLayout7 = findViewById(R.id.relativeLayout7);

        setrelativelayout(relativeLayout2);
        setrelativelayout(relativeLayout3);
        setrelativelayout(relativeLayout4);
        setrelativelayout(relativeLayout5);
        setrelativelayout(relativeLayout6);
        setrelativelayout(relativeLayout7);
        setrelativelayout(relativeLayout1);


        //Load controls
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new Frag_GameControls())
                .commit();
    }

    @Override
    public void goAnalyze() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new Frag_GameAnalyze())
                .commit();
    }

    @Override
    public void goEdit() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new Frag_GameEdit())
                .commit();
    }

    @Override
    public void goToControls() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new Frag_GameControls())
                .commit();
    }

    @Override
    public void updateImage(Uri uri) {
        System.out.println("HER");
       // im.setImageURI(uri);
    }

    public void setrelativelayout(RelativeLayout relativeLayout){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        int dimensionInPixel = 75;
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());


        ArrayList<ImageView> cards = new ArrayList<>();
        for (int i = 0; i < 10 ; i++) {
            ImageView card = new ImageView(this);
            card.setBackgroundResource(R.drawable.clubs_3);
            card.setId(View.generateViewId());
            cards.add(card);
        }

        for (int i = 0; i < cards.size() ; i++) {
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(width/8, dimensionInDp);
            if(i==0){
                rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }else{
                rp.addRule(RelativeLayout.ALIGN_TOP, cards.get(i-1).getId());
            }
            rp.setMargins(10,40,10,0);
            relativeLayout.addView(cards.get(i),rp);
        }
    }
}