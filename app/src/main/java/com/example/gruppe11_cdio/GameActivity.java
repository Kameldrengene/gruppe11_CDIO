package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    Button spil,highscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        spil = findViewById(R.id.spil_button);
        highscore = findViewById(R.id.hightscore_button);

        spil.setOnClickListener(this);
        highscore.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==spil){
            Intent intent = new Intent(this,GameActivity.class);
            startActivity(intent);
            finish();
        }

        if(v==highscore){
            Intent intent = new Intent(this,HighScoreActivity.class);
            startActivity(intent);
            finish();
        }
    }
}