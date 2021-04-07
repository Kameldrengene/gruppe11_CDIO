package com.example.gruppe11_cdio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gruppe11_cdio.Factory.Card;
import com.example.gruppe11_cdio.Factory.Card_Factory;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_spinner_item;

public class Popup_PileEditor extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private int CARD_SPINNER_ID, VALUE_SPINNER_ID, COLOR_SPINNER_ID;

    private AppCompatActivity listener;
    private ArrayList<Card> cards;
    private int CODE, cardWidth, cardHeight;
    private int currentCardIndex;
    private String pileName;

    private TextView pileNumber, colorText, valueText, cardText;
    private Spinner cardSpinner, colorSpinner, valueSpinner;
    private RelativeLayout layout;
    private Button plus1, minus1, plus2, minus2;

    private String[] values = {"A","2","3","4","5","6","7","8","9","10","11","12","13"};
    private String[] colors = {"Spar","Hjerter","Klør","Ruder"};

    private Card_Factory card_factory;
    private ArrayList<ImageView> cardViews;

    public interface PileEditorDialog {
        void onSave(ArrayList<Card> arrayOfCards, int CODE);
    }

    public Popup_PileEditor(PileEditorDialog listener, ArrayList<Card> cards, String pileName, int cardWidth, int cardHeight, int CODE) {
        this.listener = (AppCompatActivity) listener;
        this.cards = cards;
        this.CODE = CODE;
        this.pileName = pileName;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;

        card_factory = new Card_Factory(this.listener);
        currentCardIndex = cards.size() - 1;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_pileeditor, null);

        CARD_SPINNER_ID = R.id.spinnerPickCardNumber;
        VALUE_SPINNER_ID = R.id.spinnerPickValue;
        COLOR_SPINNER_ID = R.id.spinnerPickColor;

        pileNumber = view.findViewById(R.id.pileNumber);
        cardSpinner = view.findViewById(CARD_SPINNER_ID);
        colorSpinner = view.findViewById(COLOR_SPINNER_ID);
        valueSpinner = view.findViewById(VALUE_SPINNER_ID);
        layout = view.findViewById(R.id.pile);
        colorText = view.findViewById(R.id.textView13);
        valueText = view.findViewById(R.id.textView14);
        cardText = view.findViewById(R.id.textView15);

        plus1 = view.findViewById(R.id.button4);
        minus1 = view.findViewById(R.id.button5);
        plus2 = view.findViewById(R.id.button6);
        minus2 = view.findViewById(R.id.button7);
        plus1.setOnClickListener(this);
        minus1.setOnClickListener(this);
        plus2.setOnClickListener(this);
        minus2.setOnClickListener(this);

        pileNumber.setText(pileName + "");

        //Setup card picks
        refreshCardSpinner();

        //Setup value picks
        displayValueSpinner();

        //Setup color picks
        displayColorSpinner();

        //Setup pile
        refreshPile();

        builder.setView(view);
        return builder.create();
    }

    private void refreshCardSpinner(){
        //If the stack is empty, the "holder" card is shown (1,0)
        if(cards.get(0).equals(new Card(1,0))){
            cardSpinner.setVisibility(View.GONE);
            cardText.setVisibility(View.GONE);
        } else {
            cardSpinner.setVisibility(View.VISIBLE);
            cardText.setVisibility(View.VISIBLE);

            cardSpinner.setOnItemSelectedListener(this);
            List<String> cardNumbers = new ArrayList<String>();
            for (int i = 0; i < cards.size(); i++) cardNumbers.add(i+1 + "");
            ArrayAdapter<String> cardAdapter = new ArrayAdapter<String>(listener, simple_spinner_item, cardNumbers);
            cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cardSpinner.setAdapter(cardAdapter);
            cardSpinner.setSelection(currentCardIndex);
        }
    }

    private void refreshValueSpinner(){
        //If the stack is empty, the "holder" card is shown (1,0)
        if(cards.get(0).equals(new Card(1,0))){
            valueSpinner.setVisibility(View.GONE);
            valueText.setVisibility(View.GONE);
        } else {
            valueSpinner.setVisibility(View.VISIBLE);
            valueText.setVisibility(View.VISIBLE);
            valueSpinner.setSelection(cards.get(currentCardIndex).getValue()-1);
        }
    }

    private void displayValueSpinner(){
        valueSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> valueAdapter = new ArrayAdapter<String>(listener, simple_spinner_item, values);
        valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueSpinner.setAdapter(valueAdapter);
        refreshValueSpinner();
    }

    private void refreshColorSpinner(){
        //If the stack is empty, the "holder" card is shown (1,0)
        if(cards.get(0).equals(new Card(1,0))){
            colorSpinner.setVisibility(View.GONE);
            colorText.setVisibility(View.GONE);
        } else {
            colorSpinner.setVisibility(View.VISIBLE);
            colorText.setVisibility(View.VISIBLE);
            colorSpinner.setSelection(cards.get(currentCardIndex).getType());
        }
    }

    private void displayColorSpinner(){
        colorSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(listener, simple_spinner_item, colors);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);
        refreshColorSpinner();
    }

    private void refreshPile(){
        cardViews = new ArrayList<>();

        for (int i = 0; i < cards.size(); i++) {
            cardViews.add(card_factory.createCard(cards.get(i)));
        }

        layout.removeAllViews();
        for (int i = 0; i < cardViews.size() ; i++) {
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(cardWidth/8, cardHeight);
            if(i==0) rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            else rp.addRule(RelativeLayout.ALIGN_TOP, cardViews.get(i-1).getId());
            rp.setMargins(10,60,10,0);
            layout.addView(cardViews.get(i),rp);
        }
    }

    @Override
    public void onClick(View v) {

        if(v == plus1){

            //If the stack is empty, the "holder" card is shown (1,0)
            if(cards.get(0).equals(new Card(1,0)))
                cards.remove(0);

            cards.add(0, new Card(0,1));
            currentCardIndex = 0;
            refreshPile();
            refreshColorSpinner();
            refreshValueSpinner();
            refreshCardSpinner();
        }

        if(v == minus1){
            cards.remove(0);

            //If the stack is empty add placeholder card
            if(cards.size() == 0)
                cards.add(new Card(1,0));

            currentCardIndex = 0;
            refreshPile();
            refreshColorSpinner();
            refreshValueSpinner();
            refreshCardSpinner();
        }

        if(v == plus2){

            //If the stack is empty, the "holder" card is shown (1,0)
            if(cards.get(0).equals(new Card(1,0)))
                cards.remove(0);

            cards.add(new Card(0,1));
            currentCardIndex = cards.size() - 1;
            refreshPile();
            refreshColorSpinner();
            refreshValueSpinner();
            refreshCardSpinner();
        }

        if(v == minus2){
            cards.remove(cards.size()-1);

            //If the stack is empty add placeholder card
            if(cards.size() == 0)
                cards.add(new Card(1,0));

            currentCardIndex = cards.size() - 1;
            refreshPile();
            refreshColorSpinner();
            refreshValueSpinner();
            refreshCardSpinner();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == CARD_SPINNER_ID){
            currentCardIndex = position;
            valueSpinner.setSelection(cards.get(currentCardIndex).getValue()-1);
            colorSpinner.setSelection(cards.get(currentCardIndex).getType());
        }

        if(parent.getId() == VALUE_SPINNER_ID){
            cards.get(currentCardIndex).setValue(position+1);
            refreshPile();
        }

        if(parent.getId() == COLOR_SPINNER_ID){
            cards.get(currentCardIndex).setType(position);
            refreshPile();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}