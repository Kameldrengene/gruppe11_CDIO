package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;


import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gruppe11_cdio.Factory.Card;
import com.example.gruppe11_cdio.Factory.Card_Factory;
import com.example.gruppe11_cdio.Objects.GameBoard;
import com.example.gruppe11_cdio.Objects.Pile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements Frag_GameControls.Controls, Frag_GameEdit.Controls, Frag_GameAnalyze.Controls, View.OnClickListener {

    int NUMBER_OF_SPACES = 7;
    int NUMBER_OF_FINISH_PLACES = 4;
    int NUMBER_OF_LAYOUTS = 13;
    int CARD_HEIGHT_IN_DP = 75;

    int width;
    int dimensionInDp;

    RelativeLayout layouts[] = new RelativeLayout[NUMBER_OF_LAYOUTS];
    Card_Factory card_factory;
    GameBoard gameBoard = new GameBoard();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Calculate width of cards for current display
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        int cardHeightInDp = CARD_HEIGHT_IN_DP;
        dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cardHeightInDp, getResources().getDisplayMetrics());

        card_factory = new Card_Factory(this);

        //todo skal fjernes når vi får et rigtgit obekt!
        gameBoard.draw();

        //Add layouts to array
        layouts[0] =  findViewById(R.id.relativeLayout1);
        layouts[1] =  findViewById(R.id.relativeLayout2);
        layouts[2] = findViewById(R.id.relativeLayout3);
        layouts[3] = findViewById(R.id.relativeLayout4);
        layouts[4] = findViewById(R.id.relativeLayout5);
        layouts[5] = findViewById(R.id.relativeLayout6);
        layouts[6] = findViewById(R.id.relativeLayout7);
        layouts[7] = findViewById(R.id.holder1);
        layouts[8] = findViewById(R.id.holder2);
        layouts[9] = findViewById(R.id.holder3);
        layouts[10] = findViewById(R.id.holder4);
        layouts[11] = findViewById(R.id.pile);
        layouts[12] = findViewById(R.id.open);

        for (int i = 0; i < NUMBER_OF_LAYOUTS; i++) {
            layouts[i].setOnClickListener(this);
        }

        displayBoard();

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

    private void displayBoard(){

        ArrayList<Card> currentArrayOfCards;

        //First we set the spaces
        HashMap<Integer, Pile> spaces = gameBoard.getSpaces();

        for (int i = 0; i < NUMBER_OF_SPACES; i++) {
            displayPile(spaces.get(i).getCardsInSequence(), layouts[i]);
        }

        //Then the finish spaces
        HashMap<Integer, ArrayList<Card>> finishSpaces = gameBoard.getFinSpaces();

        for (int i = 0; i < NUMBER_OF_FINISH_PLACES; i++) {
            currentArrayOfCards = finishSpaces.get(i);

            if(currentArrayOfCards.size() == 0)
                displayPile(new Card(1,0), layouts[NUMBER_OF_SPACES+i]);
            else
                displayPile(finishSpaces.get(i).get(i), layouts[NUMBER_OF_SPACES+i]);
        }

        //And then deck and open card
        Card openCard = gameBoard.getDeck().get(gameBoard.getDeckPointer());
        displayPile(new Card(0,0), layouts[NUMBER_OF_SPACES+NUMBER_OF_FINISH_PLACES]);
        displayPile(openCard, layouts[NUMBER_OF_SPACES+NUMBER_OF_FINISH_PLACES+1]);

    }

    private void displayPile(ArrayList<Card> arrayOfCards, RelativeLayout layout){
        ArrayList<ImageView> cardViews = new ArrayList<>();

        for (int i = 0; i < arrayOfCards.size(); i++) {
            cardViews.add(card_factory.createCard(arrayOfCards.get(i)));
        }

        for (int i = 0; i < cardViews.size() ; i++) {
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(width/8, dimensionInDp);
            if(i==0){
                rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }else{
                rp.addRule(RelativeLayout.ALIGN_TOP, cardViews.get(i-1).getId());
            }
            rp.setMargins(10,40,10,0);
            layout.addView(cardViews.get(i),rp);
        }
    }

    private void displayPile(Card card, RelativeLayout layout){

        ImageView cardView = card_factory.createCard(card);

        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(width/8, dimensionInDp);
        rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        rp.setMargins(10,40,10,0);
        layout.addView(cardView,rp);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        System.out.println("CLICKED");
    }
}