package com.example.gruppe11_cdio;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.os.Handler;
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


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//todo man skal kunne ændre i alle piles (ikke kun sequences)
//todo hvordan skal setCardsInSequence fungerer?
//todo tiden og turen på spilskærmen skal opdates
//todo rettigheder mht. kamera
//todo rediger skal kun virke når man har trykket på rediger
//todo der mangler en tilbageknap under rediger
//todo gameboard skal være singleton
//todo skal man kunne start nyt spil eller er det altid det samme?
//todo register arbejdstid (jira)
//todo hvad er maks kort der kan være i piles?

//Card(?,14) == card back
//Card(1,0) == empty card
public class GameActivity extends Popup_EditorInterface implements Frag_GameControls.Controls, Frag_GameEdit.Controls, Frag_GameAnalyze.Controls, View.OnClickListener {

    final int NUMBER_OF_SPACES = 7;
    final int NUMBER_OF_FINISH_PLACES = 4;
    final int NUMBER_OF_LAYOUTS = NUMBER_OF_SPACES + NUMBER_OF_FINISH_PLACES + 2;
    final int CARD_HEIGHT_IN_DP = 75;
    final int EDIT_PILE_CODE = 0;
    final int EDIT_FINISH_CODE = 1;
    final int EDIT_DECK_CODE = 2;
    int onClickLayoutIndex;

    int width;
    int dimensionInDp;

    Executor bgThread;
    Handler uiThread;

    LayoutHolder layouts[] = new LayoutHolder[NUMBER_OF_LAYOUTS];
    Card_Factory card_factory;
    GameBoard gameBoard = GameBoard.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bgThread = Executors.newSingleThreadExecutor();
        uiThread = new Handler();

        //Calculate width of cards for current display
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        int cardHeightInDp = CARD_HEIGHT_IN_DP;
        dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cardHeightInDp, getResources().getDisplayMetrics());

        card_factory = new Card_Factory(this);

        for (int i = 0; i < NUMBER_OF_LAYOUTS; i++) {
            layouts[i] = new LayoutHolder();
        }

        //Add layouts to array
        layouts[0].setLayout(findViewById(R.id.relativeLayout1));
        layouts[0].setName("Bunke 1");
        layouts[1].setLayout(findViewById(R.id.relativeLayout2));
        layouts[1].setName("Bunke 2");
        layouts[2].setLayout(findViewById(R.id.relativeLayout3));
        layouts[2].setName("Bunke 3");
        layouts[3].setLayout(findViewById(R.id.relativeLayout4));
        layouts[3].setName("Bunke 4");
        layouts[4].setLayout(findViewById(R.id.relativeLayout5));
        layouts[4].setName("Bunke 5");
        layouts[5].setLayout(findViewById(R.id.relativeLayout6));
        layouts[5].setName("Bunke 6");
        layouts[6].setLayout(findViewById(R.id.relativeLayout7));
        layouts[6].setName("Bunke 7");
        layouts[7].setLayout(findViewById(R.id.holder1));
        layouts[7].setName("Holder 4");
        layouts[8].setLayout(findViewById(R.id.holder2));
        layouts[8].setName("Holder 3");
        layouts[9].setLayout(findViewById(R.id.holder3));
        layouts[9].setName("Holder 2");
        layouts[10].setLayout(findViewById(R.id.holder4));
        layouts[10].setName("Holder 1");
        layouts[11].setLayout(findViewById(R.id.open));
        layouts[11].setName("Bunken");
        layouts[12].setLayout(findViewById(R.id.pile));
        layouts[12].setName("Bunken");

        for (int i = 0; i < NUMBER_OF_LAYOUTS; i++)
            layouts[i].getLayout().setOnClickListener(this);

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
    public void updateImage(String path) {
        File finalFile = new File(path);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.GERMANY);
        Date now = new Date();
        String fileName = formatter.format(now) + ".jpg";

        // Sender billede til vores backend i nodejs
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file",fileName,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                finalFile))
                .build();
        Request request = new Request.Builder()
                .url("http://cdio.isik.dk:5000")
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

    private void displayBoard(){

        //First we set the spaces
        for (int i = 0; i < NUMBER_OF_SPACES; i++)
            displayPile(getCardsFromPile(i), layouts[i]);

        //Then the finish spaces
        for (int i = 0; i < NUMBER_OF_FINISH_PLACES; i++)
            displayPile(getTopCardFromFinishSpace(i), layouts[NUMBER_OF_SPACES + i]);

        //And then the deck and open card
        displayPile(getTopCardFromDeck(), layouts[NUMBER_OF_SPACES + NUMBER_OF_FINISH_PLACES]);
        displayPile(new Card(0,0), layouts[NUMBER_OF_SPACES + NUMBER_OF_FINISH_PLACES + 1]);
    }

    private void displayPile(ArrayList<Card> arrayOfCards, LayoutHolder layout){
        ArrayList<ImageView> cardViews = new ArrayList<>();

        layout.getLayout().removeAllViews();

        for (int i = 0; i < arrayOfCards.size(); i++)
            cardViews.add(card_factory.createCard(arrayOfCards.get(i)));

        for (int i = 0; i < cardViews.size() ; i++) {
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(width/8, dimensionInDp);
            if(i==0) rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            else rp.addRule(RelativeLayout.ALIGN_TOP, cardViews.get(i-1).getId());

            rp.setMargins(10,40,10,0);
            layout.getLayout().addView(cardViews.get(i),rp);
        }
    }

    private void displayPile(Card card, LayoutHolder layout){
        ImageView cardView = card_factory.createCard(card);

        layout.getLayout().removeAllViews();

        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(width/8, dimensionInDp);
        rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        rp.setMargins(10,40,10,0);
        layout.getLayout().addView(cardView,rp);
    }

    @Override
    public void onClick(View v) {

        //Find out what pile/layout was pressed
        onClickLayoutIndex = 0;
        for (int i = 0; i < layouts.length; i++) {
            if(v == layouts[i].getLayout()){
                onClickLayoutIndex = i;
                break;
            }
        }

        //If a pile was clicked
        if(onClickLayoutIndex <= 6){
            ArrayList<Card> cardsToShow = getCardsFromPile(onClickLayoutIndex);
            Popup_PileEditor pileEditor = new Popup_PileEditor(this, cardsToShow, layouts[onClickLayoutIndex].getName(), width, dimensionInDp, EDIT_PILE_CODE);
            pileEditor.show(this.getSupportFragmentManager(), null);

        //If a finish space was clicked
        } else if(onClickLayoutIndex <= 10) {
            Card cardToShow  = getTopCardFromFinishSpace(onClickLayoutIndex - NUMBER_OF_SPACES);
            Popup_CardEditor cardEditor = new Popup_CardEditor(this, cardToShow, layouts[onClickLayoutIndex].getName(), width, dimensionInDp, EDIT_FINISH_CODE);
            cardEditor.show(this.getSupportFragmentManager(), null);

        //If the deck was clicked
        } else if(onClickLayoutIndex == 11){
            Card cardToShow = getTopCardFromDeck();
            Popup_CardEditor cardEditor = new Popup_CardEditor(this, cardToShow, layouts[onClickLayoutIndex].getName(), width, dimensionInDp, EDIT_DECK_CODE);
            cardEditor.show(this.getSupportFragmentManager(), null);
        }
    }

    @Override
    //Will be called if one of the piles were edited
    public void onSave(ArrayList<Card> arrayOfCards, int CODE) {
        if(CODE == EDIT_PILE_CODE)
            gameBoard.getSpaces().get(onClickLayoutIndex).setCardsInSequence(arrayOfCards);
        displayBoard();
    }

    @Override
    //Will be called if the deck or finish spaces was edited
    public void onSave(Card card, int CODE) {
        Card cardToUpdate;
        if(CODE == EDIT_FINISH_CODE) cardToUpdate = getTopCardFromFinishSpace(onClickLayoutIndex - NUMBER_OF_SPACES);
        else cardToUpdate = getTopCardFromDeck();

        cardToUpdate.setType(card.getType());
        cardToUpdate.setValue(card.getValue());
        displayBoard();
    }

    //Protects against null
    private Card getTopCardFromDeck(){
        if(gameBoard.getDeckPointer() == -1){
            gameBoard.getDeck().add(new Card(1,0));
            gameBoard.setDeckPointer(0);
        }
        return gameBoard.getDeck().get(gameBoard.getDeckPointer());
    }

    //Protects against null
    private Card getTopCardFromFinishSpace(int i){
        ArrayList<Card> cards = gameBoard.getFinSpaces().get(i);
        //todo er det ok ift. algoritmen der beregner næste træk?
        if(cards.size() == 0) cards.add(new Card(1,0));
        return cards.get(0);
    }

    //Protects against null
    private ArrayList<Card> getCardsFromPile(int i){
        ArrayList<Card> cards = gameBoard.getSpaces().get(i).getCardsInSequence();
        //todo er det ok ift. algoritmen der beregner næste træk?
        if(cards.size() == 0) cards.add(new Card(1,0));
        return cards;
    }
}

//Holds a relevative layout along with other relevant information about the layout
class LayoutHolder{
    RelativeLayout layout;
    String name;

    public RelativeLayout getLayout() { return layout; }
    public void setLayout(RelativeLayout layout) { this.layout = layout; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}