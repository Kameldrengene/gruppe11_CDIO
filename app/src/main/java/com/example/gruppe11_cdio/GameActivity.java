package com.example.gruppe11_cdio;

import java.text.SimpleDateFormat;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gruppe11_cdio.Factory.Card;
import com.example.gruppe11_cdio.Factory.Card_Factory;
import com.example.gruppe11_cdio.Objects.GameBoard;
import com.example.gruppe11_cdio.Objects.MoveDTO;
import com.example.gruppe11_cdio.Objects.Pile;
import com.google.gson.Gson;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import static java.lang.Thread.sleep;

//Card(type, value)
//Card(?,14) == card back
//Card(1,0) == empty card
public class GameActivity extends Popup_Interface implements Frag_GameControls.Controls, Frag_GameEdit.Controls, Frag_GameAnalyze.Controls, View.OnClickListener {

    final int NUMBER_OF_SPACES = 7;
    final int NUMBER_OF_FINISH_PLACES = 4;
    final int NUMBER_OF_LAYOUTS = NUMBER_OF_SPACES + NUMBER_OF_FINISH_PLACES + 2;
    final int CARD_HEIGHT_IN_DP = 75;
    final int EDIT_PILE_CODE = 0;
    final int EDIT_FINISH_CODE = 1;
    final int EDIT_DECK_CODE = 2;

    public static String nextMove = "";
    public static boolean fromMain = false;

    boolean enableEdit = false;
    boolean firstPicture;
    int onClickLayoutIndex;

    int cardWidth;
    int cardHeight;

    Executor bgThread;
    Handler uiThread;
    Dialog_Loading loadingDialog;

    final LayoutHolder layouts[] = new LayoutHolder[NUMBER_OF_LAYOUTS];
    Card_Factory card_factory;
    GameBoard gameBoard = new GameBoard();
    Animation shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bgThread = Executors.newSingleThreadExecutor();
        uiThread = new Handler();
        loadingDialog = new Dialog_Loading(this);

        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        //Calculate width of cards for current display
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        cardWidth = displayMetrics.widthPixels;
        cardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CARD_HEIGHT_IN_DP, getResources().getDisplayMetrics());

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

        //Load buttons
        goToControls();

        displayBoard();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(fromMain) {
            String path = getIntent().getExtras().getString("photo");
            if(path != null) updateImage(path);
            fromMain = false;
        }
    }

    @Override
    public void updateImage(String path) {

        loadingDialog.startLoadingDialog("Afkoder billede...");

        File finalFile = new File(path);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.GERMANY);
        Date now = new Date();
        String fileName = formatter.format(now) + ".jpg";

        // Sender billede til vores backend i Flask
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
                .addHeader("margin", String.valueOf(dpToPx(15f))) //Append independent device margin
                .build();
        bgThread.execute(()->{
            try {
                sleep(2000);
                Response response = client.newCall(request).execute();
                String responseMsg = response.body().string();

                //If success
                uiThread.post(()->{

                    //Update gameboard internally and display
                    gameBoard = new Gson().fromJson(responseMsg, GameBoard.class);
                    loadingDialog.dismissDialog();
                    displayBoard();
                });
            } catch (IOException e) {
                e.printStackTrace();

                uiThread.post(() -> {
                    Toast.makeText(this, "Fejl ved kontakt af server", Toast.LENGTH_LONG).show();
                    loadingDialog.dismissDialog();
                });

            } catch (Exception e) {
                e.printStackTrace();

                uiThread.post(() -> {
                    Toast.makeText(this, "Fejl ved afkodning af billede", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                });
            }
        });
    }

    @Override
    public void goAnalyze() {

        loadingDialog.startLoadingDialog("Analyserer spil...");
        Gson g = new Gson();

        // Sender GameBoard til vores backend i Flask
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .build();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, g.toJson(gameBoard));

        Request request = new Request.Builder()
                .url("http://cdio.isik.dk:5000/turn/")
                .method("POST", body)
                .build();

        bgThread.execute(()->{
            try {
                sleep(2000);
                Response response = client.newCall(request).execute();
                MoveDTO responseMsg = g.fromJson(response.body().string(), MoveDTO.class);

                //If success
                uiThread.post(()->{

                    //Display message
                    if(responseMsg.isCorrect()){
                        nextMove = responseMsg.getMsg();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.framelayout, new Frag_GameAnalyze())
                                .commitAllowingStateLoss();
                    } else
                        Toast.makeText(this, responseMsg.getMsg(), Toast.LENGTH_LONG).show();
                    loadingDialog.dismissDialog();
                });

            } catch (IOException e) {
                e.printStackTrace();
                uiThread.post(() -> {
                    Toast.makeText(this, "Fejl ved kontakt af server", Toast.LENGTH_LONG).show();
                    loadingDialog.dismissDialog();
                });

            } catch (Exception e) {
                e.printStackTrace();
                uiThread.post(() -> {
                    Toast.makeText(this, "Fejl ved analyse af billede", Toast.LENGTH_LONG).show();
                    loadingDialog.dismissDialog();
                });
            }
        });
    }

    @Override
    public void goEdit() {
        enableEdit = true;
        startShake();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new Frag_GameEdit())
                .commit();
    }

    private void startShake(){
        for (int i = 0; i < layouts.length; i++) {
            layouts[i].getLayout().startAnimation(shake);
        }
    }

    @Override
    public void goToControls() {
        enableEdit = false;
        stopShake();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new Frag_GameControls())
                .commit();
    }

    private void stopShake() {
        for (int i = 0; i < layouts.length; i++) {
            layouts[i].getLayout().clearAnimation();
        }
    }

    private void displayBoard(){

        //First we set the spaces
        for (int i = 0; i < NUMBER_OF_SPACES; i++)
            displayPile(getCardsFromPile(i), layouts[i]);

        //Then the finish spaces
        for (int i = 0; i < NUMBER_OF_FINISH_PLACES; i++)
            displayPile(getTopCardFromFinishSpace(i), layouts[NUMBER_OF_SPACES + i]);

        //Then the open card
        displayPile(getTopCardFromDeck(), layouts[NUMBER_OF_SPACES + NUMBER_OF_FINISH_PLACES]);

        //And then the deck
        if(gameBoard.getDeckPointer() > 0) //If there are two cards
            displayPile(new Card(0,14), layouts[NUMBER_OF_SPACES + NUMBER_OF_FINISH_PLACES + 1]); //Closed card
        else
            displayPile(new Card(1,0), layouts[NUMBER_OF_SPACES + NUMBER_OF_FINISH_PLACES + 1]); //Empty card
    }

    private void displayPile(ArrayList<Card> arrayOfCards, LayoutHolder layout){
        ArrayList<ImageView> cardViews = new ArrayList<>();

        layout.getLayout().removeAllViews();

        for (int i = 0; i < arrayOfCards.size(); i++)
            cardViews.add(card_factory.createCard(arrayOfCards.get(i)));

        for (int i = 0; i < cardViews.size() ; i++) {
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(cardWidth /8, cardHeight);
            if(i==0) rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            else rp.addRule(RelativeLayout.ALIGN_TOP, cardViews.get(i-1).getId());

            rp.setMargins(10,40,10,0);
            layout.getLayout().addView(cardViews.get(i),rp);
        }
    }

    private void displayPile(Card card, LayoutHolder layout){
        ImageView cardView = card_factory.createCard(card);

        layout.getLayout().removeAllViews();

        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(cardWidth /8, cardHeight);
        rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        rp.setMargins(10,40,10,0);
        layout.getLayout().addView(cardView,rp);
    }

    @Override
    public void onClick(View v) {

        if(enableEdit){
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
                Popup_PileEditor pileEditor = new Popup_PileEditor(this, cardsToShow, layouts[onClickLayoutIndex].getName(), onClickLayoutIndex, cardWidth, cardHeight, EDIT_PILE_CODE);
                pileEditor.show(this.getSupportFragmentManager(), null);

            //If a finish space was clicked
            } else if(onClickLayoutIndex <= 10) {
                Card cardToShow  = getTopCardFromFinishSpace(onClickLayoutIndex - NUMBER_OF_SPACES);
                Popup_CardEditor cardEditor = new Popup_CardEditor(this, cardToShow, layouts[onClickLayoutIndex].getName(), cardWidth, cardHeight, EDIT_FINISH_CODE);
                cardEditor.show(this.getSupportFragmentManager(), null);

            //If the deck was clicked
            } else if(onClickLayoutIndex == 11){
                Card cardToShow = getTopCardFromDeck();
                Popup_CardEditor cardEditor = new Popup_CardEditor(this, cardToShow, layouts[onClickLayoutIndex].getName(), cardWidth, cardHeight, EDIT_DECK_CODE);
                cardEditor.show(this.getSupportFragmentManager(), null);
            } else if(onClickLayoutIndex == 12){

                //Flip the card if appropriate
                if(gameBoard.getDeckPointer() > 0){
                    //If the card is closed
                    gameBoard.setDeckPointer(0);
                }
                else { //Else the card is empty
                    if(gameBoard.getDeckPointer() == 0)
                        gameBoard.setDeckPointer(1);
                    else {
                        gameBoard.setDeckPointer(0);
                        if(gameBoard.getDeck().size() == 0) gameBoard.getDeck().add(new Card(1,1));
                        else gameBoard.getDeck().set(0, new Card(1,1));
                    }
                }
                displayBoard();
            }
        }
    }

    @Override
    //Will be called if one of the piles were edited
    public void onSave(ArrayList<Card> arrayOfCards, int CODE) {
        if(CODE == EDIT_PILE_CODE){
            Pile pile = gameBoard.getSpaces().get(onClickLayoutIndex);

            //If the new array is empty
            if(arrayOfCards.get(0).equals(new Card(1,0))) pile.clearCards();
            else pile.setCardsInSequence(arrayOfCards);
        }
        displayBoard();
    }

    @Override
    //Will be called if the deck or a finish space was edited
    public void onSave(Card card, int CODE) {

        //Check is the new card is empty
        Boolean cardIsEmpty = false;
        if(card.equals(new Card(1,0))) cardIsEmpty = true;

        //If a finish space was edited
        if(CODE == EDIT_FINISH_CODE) {
            ArrayList<Card> cards = gameBoard.getFinSpaces().get(onClickLayoutIndex - NUMBER_OF_SPACES);
            if(cards.size() == 0) cards.add(new Card(1,0));
            if(cardIsEmpty) cards.clear();
            else {
                cards.get(0).setType(card.getType());
                cards.get(0).setValue(card.getValue());
            }

        //Else the deck was edited
        } else {

            int deckPointer = gameBoard.getDeckPointer();
            System.out.println("DECK BEFORE : " + gameBoard.getDeckPointer());

            //If the new card is empty
            if(card.isEmpty()) {
                if(gameBoard.getDeckPointer() > -1)
                    gameBoard.getDeck().set(0, card);
                gameBoard.setDeckPointer(-1);
            }
            else{
                if(deckPointer > -1) //If there was a card previously
                    gameBoard.getDeck().set(0, card);
                else {
                    gameBoard.setDeckPointer(0);
                    if(gameBoard.getDeck().size() == 0) gameBoard.getDeck().add(card);
                    else gameBoard.getDeck().set(0, card);
                }
            }
        }

        System.out.println("DECK AFTER : " + gameBoard.getDeckPointer());
        displayBoard();
    }

    //Protects against null. Returns deep copy
    private Card getTopCardFromDeck(){
        if(gameBoard.getDeckPointer() == -1) return new Card(1,0);
        else return gameBoard.getDeck().get(0).deepCopy();
    }

    //Protects against null. Returns deep copy
    private Card getTopCardFromFinishSpace(int i){
        ArrayList<Card> cards = gameBoard.getFinSpaces().get(i);
        if(cards.size() == 0) return new Card(1,0);
        else return cards.get(cards.size() - 1).deepCopy();
    }

    //Protects against null. Returns deep copy
    private ArrayList<Card> getCardsFromPile(int i){
        ArrayList<Card> cards = gameBoard.getSpaces().get(i).getCardsInSequence();
        if(cards.size() == 0){
            cards = new ArrayList<>();
            cards.add(new Card(1,0));
            return cards;
        }
        else return deepCopyCards(cards);
    }

    private ArrayList<Card> deepCopyCards(ArrayList<Card> cards){
        ArrayList<Card> out = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++)
            out.add(new Card(cards.get(i).getType(), cards.get(i).getValue()));
        return out;
    }

    private float dpToPx(float dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        return px;
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