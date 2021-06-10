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
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gruppe11_cdio.Factory.Card;
import com.example.gruppe11_cdio.Factory.Card_Factory;

import static android.R.layout.simple_spinner_item;

public class Popup_CardEditor extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private int VALUE_SPINNER_ID, COLOR_SPINNER_ID;

    private Popup_Interface listener;
    private Card card;
    private int CODE, cardWidth, cardHeight;
    private String pileName;

    private Button save, plus, minus;
    private TextView pileNumber, colorText, valueText, cardText, plusButtonText, minusButtonText;
    private Spinner colorSpinner, valueSpinner;
    private RelativeLayout layout;

    private String[] valuesAll = {"A","2","3","4","5","6","7","8","9","10","11","12","13"};
    private String[] colors = {"Spar","Hjerter","Klør","Ruder"};

    private Card_Factory card_factory;
    private ImageView cardView;

    public Popup_CardEditor(Popup_Interface listener, Card card, String pileName, int cardWidth, int cardHeight, int CODE) {
        this.listener = listener;

        //Best practice with deep copy if user decides not to save
        this.card = new Card(card.getType(), card.getValue());
        this.CODE = CODE;
        this.pileName = pileName;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        card_factory = new Card_Factory(this.listener);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_cardeditor, null);

        VALUE_SPINNER_ID = R.id.spinnerPickValue;
        COLOR_SPINNER_ID = R.id.spinnerPickColor;

        pileNumber = view.findViewById(R.id.pileNumber);
        colorSpinner = view.findViewById(COLOR_SPINNER_ID);
        valueSpinner = view.findViewById(VALUE_SPINNER_ID);
        layout = view.findViewById(R.id.pile);
        colorText = view.findViewById(R.id.textView13);
        valueText = view.findViewById(R.id.textView14);
        cardText = view.findViewById(R.id.textView15);
        plusButtonText = view.findViewById(R.id.textView9);
        minusButtonText = view.findViewById(R.id.textView10);

        save = view.findViewById(R.id.save);
        plus = view.findViewById(R.id.plus);
        minus = view.findViewById(R.id.minus);
        save.setOnClickListener(this);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);

        pileNumber.setText(pileName + "");

        //Setup value picks
        displayValueSpinner();

        //Setup color picks
        displayColorSpinner();

        //Setup card
        refreshCard();

        builder.setView(view);
        return builder.create();
    }

    private void displayValueSpinner(){
        valueSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> valueAdapter = new ArrayAdapter<String>(listener, simple_spinner_item, valuesAll);
        valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueSpinner.setAdapter(valueAdapter);
        refreshValueSpinner();
    }

    private void refreshValueSpinner(){
        //If the stack is empty, hide this option
        if(card.equals(new Card(1,0))){
            valueSpinner.setVisibility(View.GONE);
            valueText.setVisibility(View.GONE);
        } else {
            valueSpinner.setVisibility(View.VISIBLE);
            valueText.setVisibility(View.VISIBLE);
            valueSpinner.setSelection(card.getValue()-1);
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
        if(card.equals(new Card(1,0)) || card.getValue() == 14){
            colorSpinner.setVisibility(View.GONE);
            colorText.setVisibility(View.GONE);
        } else {
            colorSpinner.setVisibility(View.VISIBLE);
            colorText.setVisibility(View.VISIBLE);
            colorSpinner.setSelection(card.getType()-1);
        }
    }

    private void refreshCard(){
        cardView = card_factory.createCard(card);
        layout.removeAllViews();
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(cardWidth/8, cardHeight);
        rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rp.setMargins(10,60,10,0);
        layout.addView(cardView,rp);
        refreshButtons();
    }

    private void refreshButtons(){
        if(card.equals(new Card(1,0))){
            plus.setVisibility(View.VISIBLE);
            plusButtonText.setVisibility(View.VISIBLE);
            minus.setVisibility(View.GONE);
            minusButtonText.setVisibility(View.GONE);
        } else {
            plus.setVisibility(View.GONE);
            plusButtonText.setVisibility(View.GONE);
            minus.setVisibility(View.VISIBLE);
            minusButtonText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        if(v == plus){
            card = new Card(0,1);
            refreshCard();
            refreshColorSpinner();
            refreshValueSpinner();
        }

        if(v == minus){
            card = new Card(1,0);
            refreshCard();
            refreshColorSpinner();
            refreshValueSpinner();
        }

        if(v == save){
            listener.onSave(card, CODE);
            dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == VALUE_SPINNER_ID){
            card.setValue(position+1);
            refreshColorSpinner();
            refreshCard();
        }

        if(parent.getId() == COLOR_SPINNER_ID){
            card.setType(position);
            refreshCard();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

}
