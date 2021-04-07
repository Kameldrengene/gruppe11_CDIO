package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
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

public class GameActivity extends AppCompatActivity implements Frag_GameControls.Controls, Frag_GameEdit.Controls, Frag_GameAnalyze.Controls, View.OnClickListener, Popup_PileEditor.PileEditorDialog {

    int NUMBER_OF_SPACES = 7;
    int NUMBER_OF_FINISH_PLACES = 4;
    int NUMBER_OF_LAYOUTS = 13;
    int CARD_HEIGHT_IN_DP = 75;
    int EDIT_PILE_CODE = 0;
    int onClickLayoutIndex;

    int width;
    int dimensionInDp;

    Executor bgThread;
    Handler uiThread;

    LayoutHolder layouts[] = new LayoutHolder[NUMBER_OF_LAYOUTS];
    Card_Factory card_factory;
    GameBoard gameBoard = new GameBoard();

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

        //todo skal fjernes når vi får et rigtgit obekt!
        gameBoard.draw();

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
        layouts[7].setName("Holder 1");
        layouts[8].setLayout(findViewById(R.id.holder2));
        layouts[8].setName("Holder 2");
        layouts[9].setLayout(findViewById(R.id.holder3));
        layouts[9].setName("Holder 3");
        layouts[10].setLayout(findViewById(R.id.holder4));
        layouts[10].setName("Holder 4");
        layouts[11].setLayout(findViewById(R.id.pile));
        layouts[11].setName("Nyeste kort");
        layouts[12].setLayout(findViewById(R.id.open));
        layouts[12].setName("Bunken");

        for (int i = 0; i < NUMBER_OF_LAYOUTS; i++) {
            layouts[i].getLayout().setOnClickListener(this);
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

    private void displayPile(ArrayList<Card> arrayOfCards, LayoutHolder layout){
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
            layout.getLayout().addView(cardViews.get(i),rp);
        }
    }

    private void displayPile(Card card, LayoutHolder layout){

        ImageView cardView = card_factory.createCard(card);

        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(width/8, dimensionInDp);
        rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        rp.setMargins(10,40,10,0);
        layout.getLayout().addView(cardView,rp);
    }

    @Override
    public void onClick(View v) {

        //Find out pile/layout was pressed
        onClickLayoutIndex = 0;
        for (int i = 0; i < layouts.length; i++) {
            if(v == layouts[i].getLayout()){
                onClickLayoutIndex = i;
                break;
            }
        }

        Popup_PileEditor pileEditor = new Popup_PileEditor(this, gameBoard.getSpaces().get(onClickLayoutIndex).getCardsInSequence(), layouts[onClickLayoutIndex].getName(), width, dimensionInDp, EDIT_PILE_CODE);
        pileEditor.show(this.getSupportFragmentManager(), null);
    }

    @Override
    public void onSave(ArrayList<Card> arrayOfCards, int CODE) {

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

//Holds a relevative layout along with other relevant information about the layout
class LayoutHolder{

    RelativeLayout layout;
    String name;

    public RelativeLayout getLayout() { return layout; }
    public void setLayout(RelativeLayout layout) { this.layout = layout; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}