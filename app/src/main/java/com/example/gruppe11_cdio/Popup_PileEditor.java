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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gruppe11_cdio.Factory.Card;
import com.example.gruppe11_cdio.Factory.Card_Factory;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_spinner_item;

public class Popup_PileEditor extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private int CARD_SPINNER_ID, VALUE_SPINNER_ID, COLOR_SPINNER_ID;

    private Popup_EditorInterface listener;
    private ArrayList<Card> cards;
    private int CODE, cardWidth, cardHeight, pileIndex;
    private int currentCardIndex;
    private int MAX_SHOWN_CARDS = 13;
    private String pileName;

    private TextView pileNumber, colorText, valueText, cardText, plus0ButtonText, plus1ButtonText, minus1ButtonText, plus2ButtonText, minus2ButtonText;
    private Spinner cardSpinner, colorSpinner, valueSpinner;
    private RelativeLayout layout;
    private Button plus0, plus1, minus1, plus2, minus2, save;

    private String[] values = {"A","2","3","4","5","6","7","8","9","10","11","12","13","X"};
    private String[] colors = {"Spar","Hjerter","Klør","Ruder"};

    private Card_Factory card_factory;
    private ArrayList<ImageView> cardViews;

    public Popup_PileEditor(Popup_EditorInterface listener, ArrayList<Card> cards, String pileName, int pileIndex, int cardWidth, int cardHeight, int CODE) {
        this.listener = listener;

        //Important with deep copy if user decides not to save
        this.cards = deepCopyCards(cards);
        this.CODE = CODE;
        this.pileName = pileName;
        this.pileIndex = pileIndex;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;

        card_factory = new Card_Factory(this.listener);
        currentCardIndex = cards.size() - 1;
    }

    private ArrayList<Card> deepCopyCards(ArrayList<Card> cards){
        ArrayList<Card> out = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++)
            out.add(new Card(cards.get(i).getType(), cards.get(i).getValue()));
        return out;
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

        plus0 = view.findViewById(R.id.button0);
        plus0ButtonText = view.findViewById(R.id.textView11);
        plus1 = view.findViewById(R.id.button4);
        plus1ButtonText = view.findViewById(R.id.textView7);
        minus1 = view.findViewById(R.id.button5);
        minus1ButtonText = view.findViewById(R.id.textView8);
        plus2 = view.findViewById(R.id.button6);
        plus2ButtonText = view.findViewById(R.id.textView9);
        minus2 = view.findViewById(R.id.button7);
        minus2ButtonText = view.findViewById(R.id.textView10);
        save = view.findViewById(R.id.save);

        plus0.setOnClickListener(this);
        plus1.setOnClickListener(this);
        minus1.setOnClickListener(this);
        plus2.setOnClickListener(this);
        minus2.setOnClickListener(this);
        save.setOnClickListener(this);

        pileNumber.setText(pileName + "");

        //Setup card picks
        displayCardSpinner();

        //Setup value picks
        displayValueSpinner();

        //Setup color picks
        displayColorSpinner();

        //Setup pile
        refreshPile();

        builder.setView(view);
        return builder.create();
    }

    private void displayCardSpinner(){
        cardSpinner.setOnItemSelectedListener(this);
        List<String> cardNumbers = new ArrayList<String>();
        for (int i = 0; i < cards.size(); i++) cardNumbers.add(i+1 + "");
        ArrayAdapter<String> cardAdapter = new ArrayAdapter<String>(listener, simple_spinner_item, cardNumbers);
        cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardSpinner.setAdapter(cardAdapter);
        refreshCardSpinner();
    }

    private void refreshCardSpinner(){
        //If the stack is empty, hide this option
        if(cards.get(0).equals(new Card(1,0))){
            cardSpinner.setVisibility(View.GONE);
            cardText.setVisibility(View.GONE);
        } else {
            cardSpinner.setVisibility(View.VISIBLE);
            cardText.setVisibility(View.VISIBLE);
            cardSpinner.setSelection(currentCardIndex);
        }
    }

    private void displayValueSpinner(){
        valueSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> valueAdapter = new ArrayAdapter<String>(listener, simple_spinner_item, values);
        valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueSpinner.setAdapter(valueAdapter);
        refreshValueSpinner();
    }

    private void refreshValueSpinner(){
        //If the stack is empty, hide this option
        if(cards.get(0).equals(new Card(1,0))){
            valueSpinner.setVisibility(View.GONE);
            valueText.setVisibility(View.GONE);
        } else {
            valueSpinner.setVisibility(View.VISIBLE);
            valueText.setVisibility(View.VISIBLE);
            valueSpinner.setSelection(cards.get(currentCardIndex).getValue()-1);
        }
    }

    private void displayColorSpinner(){
        colorSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(listener, simple_spinner_item, colors);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);
        refreshColorSpinner();
    }

    private void refreshColorSpinner(){
        //If the stack is empty or the "back card" is chosen, hide this option
        if(cards.get(0).equals(new Card(1,0)) || cards.get(currentCardIndex).getValue() == 14){
            colorSpinner.setVisibility(View.GONE);
            colorText.setVisibility(View.GONE);
        } else {
            colorSpinner.setVisibility(View.VISIBLE);
            colorText.setVisibility(View.VISIBLE);
            colorSpinner.setSelection(cards.get(currentCardIndex).getType());
        }
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
        refreshButtons();
    }

    private void refreshButtons(){
        if(cards.get(0).equals(new Card(1,0))){
            plus0.setVisibility(View.VISIBLE);
            plus0ButtonText.setVisibility(View.VISIBLE);
            plus1.setVisibility(View.GONE);
            plus1ButtonText.setVisibility(View.GONE);
            minus1.setVisibility(View.GONE);
            minus1ButtonText.setVisibility(View.GONE);
            plus2.setVisibility(View.GONE);
            plus2ButtonText.setVisibility(View.GONE);
            minus2.setVisibility(View.GONE);
            minus2ButtonText.setVisibility(View.GONE);
        } else {
            plus0.setVisibility(View.GONE);
            plus0ButtonText.setVisibility(View.GONE);
            plus1.setVisibility(View.VISIBLE);
            plus1ButtonText.setVisibility(View.VISIBLE);
            minus1.setVisibility(View.VISIBLE);
            minus1ButtonText.setVisibility(View.VISIBLE);
            plus2.setVisibility(View.VISIBLE);
            plus2ButtonText.setVisibility(View.VISIBLE);
            minus2.setVisibility(View.VISIBLE);
            minus2ButtonText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        if(v == save){
            listener.onSave(cards, CODE);
            dismiss();
            return;
        }

        if(v == plus0){
            cards.remove(0);
            currentCardIndex = 0;
            cards.add(0, new Card(0,1));
        }

        if(v == plus1){
            Card CardToAdd = null;

           //Is there room for more cards?
            if(cards.size() < pileIndex + MAX_SHOWN_CARDS){

                //Does the new card *have* to be hidden?
                if(cards.get(0).getValue() == 14)

                    //Is it legal to add another hidden card?
                    if(getNumberOfHiddenCards() < pileIndex)
                        CardToAdd = new Card(0,14);
                    else illegalChange("For mange skjulte kort");

                //Is it legal to add another shown card?
                else if(getNumberOfShownCards() < MAX_SHOWN_CARDS)
                    CardToAdd = new Card(0,1);

                //Is it legal to add another hidden card?
                else if(getNumberOfHiddenCards() < pileIndex)
                    CardToAdd = new Card(0,14);

                else illegalChange("For mange viste kort");

            } else illegalChange("For mange kort");

            if(CardToAdd != null){
                currentCardIndex = 0;
                cards.add(0, CardToAdd);
            }
        }

        if(v == plus2) {
            currentCardIndex = cards.size() - 1;
            cards.add(new Card(0,1));
        }

        if(v == minus1){
            cards.remove(0);
            currentCardIndex = 0;
        }

        if(v == minus2){
            cards.remove(cards.size()-1);
            currentCardIndex = cards.size() - 1;
        }

        if(v == minus1 || v == minus2){
            //If the stack is empty add placeholder card
            if(cards.size() == 0)
                cards.add(new Card(1,0));
        }

        refreshPile();
        refreshColorSpinner();
        refreshValueSpinner();
        displayCardSpinner();
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
            refreshColorSpinner();
            refreshPile();
        }

        if(parent.getId() == COLOR_SPINNER_ID){
            cards.get(currentCardIndex).setType(position);
            refreshPile();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void illegalChange(String message){
        if(message == null) message = "Ulovlig ændring";
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private int getNumberOfHiddenCards(){
        int total = 0;
        for (int i = 0; i < cards.size(); i++)
            if(cards.get(i).getValue() == 14)
                total++;
        return total;
    }

    private int getNumberOfShownCards(){
        int total = 0;
        for (int i = 0; i < cards.size(); i++)
            if(cards.get(i).getValue() != 14)
                total++;
        return total;
    }

}