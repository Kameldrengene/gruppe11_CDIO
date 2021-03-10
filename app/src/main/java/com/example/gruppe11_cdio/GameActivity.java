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

    RelativeLayout relativeLayout1,relativeLayout2,relativeLayout3,relativeLayout4,relativeLayout5,relativeLayout6,relativeLayout7,holder1,holder2,holder3
,holder4,pile,open;
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
        holder1 = findViewById(R.id.holder1);
        holder2 = findViewById(R.id.holder2);
        holder3 = findViewById(R.id.holder3);
        holder4 = findViewById(R.id.holder4);
        pile = findViewById(R.id.pile);
        open = findViewById(R.id.open);

        setrelativelayout(open,1);
        setrelativelayout(pile,1);
        setrelativelayout(holder1,1);
        setrelativelayout(holder2,1);
        setrelativelayout(holder3,1);
        setrelativelayout(holder4,1);
        setrelativelayout(relativeLayout2,10);
        setrelativelayout(relativeLayout3,10);
        setrelativelayout(relativeLayout4,10);
        setrelativelayout(relativeLayout5,10);
        setrelativelayout(relativeLayout6,10);
        setrelativelayout(relativeLayout7,10);
        setrelativelayout(relativeLayout1,10);


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

    public void setrelativelayout(RelativeLayout relativeLayout,int size){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        int dimensionInPixel = 75;
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());


        ArrayList<ImageView> cards = new ArrayList<>();


        for (int i = 0; i < size ; i++) {
            ImageView card = new ImageView(this);
            if(size == 1){
                if(relativeLayout == pile){
                    card.setBackgroundResource(R.drawable.card_back);
                }else if(relativeLayout == open){
                    card.setBackgroundResource(R.drawable.clubs_a);
                }
                else{
                    card.setBackgroundResource(R.drawable.holder);
                }
                card.setId(View.generateViewId());
                cards.add(card);
            }else {
                card.setBackgroundResource(R.drawable.hearts_2);
                card.setId(View.generateViewId());
                cards.add(card);
            }
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