package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gruppe11_cdio.Factory.Card;
import com.example.gruppe11_cdio.Factory.Card_Factory;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GameActivity extends AppCompatActivity implements Frag_GameControls.Controls, Frag_GameEdit.Controls, Frag_GameAnalyze.Controls{
    Executor bgThread;
    Handler uiThread;
    RelativeLayout relativeLayout1,relativeLayout2,relativeLayout3,relativeLayout4,relativeLayout5,relativeLayout6,relativeLayout7,holder1,holder2,holder3
,holder4,pile,open;
    Card_Factory card_factory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bgThread = Executors.newSingleThreadExecutor();
        uiThread = new Handler();

        card_factory = new Card_Factory(this);
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
        File finalFile = new File(getRealPathFromURI(uri));
        // Sender billede til vores backend i nodejs
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("myfile","android.png",
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        finalFile))
                        .build();
                Request request = new Request.Builder()
                        .url("http://130.225.170.68:8081/upload")
                        .method("POST", body)
                        .build();
                bgThread.execute(()->{

                    try {
                        Response response = client.newCall(request).execute();
                        String responseMsg = response.body().string();

                        uiThread.post(()->{
                            Toast.makeText(this,responseMsg,Toast.LENGTH_LONG).show();
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

    }

    public void setrelativelayout(RelativeLayout relativeLayout,int size){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        int dimensionInPixel = 75;
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());


        ArrayList<ImageView> cards = new ArrayList<>();


        for (int i = 0; i < size ; i++) {
            if(size == 1){
                if(relativeLayout == pile){
                    cards.add(card_factory.createCard(new Card(0,0)));
                }else if(relativeLayout == open){
                    cards.add(card_factory.createCard(new Card(2,4)));
                }
                else{
                    cards.add(card_factory.createCard(new Card(1,0)));
                }
            }else {
                cards.add(card_factory.createCard(new Card(new Random().nextInt(4),new Random().nextInt(13)+1)));
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

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


}