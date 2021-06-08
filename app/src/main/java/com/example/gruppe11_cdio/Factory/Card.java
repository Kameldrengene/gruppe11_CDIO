package com.example.gruppe11_cdio.Factory;

import androidx.annotation.Nullable;

public class Card {
    private int     type;       //0: spar; 1: Hjerter; 2: Klør; 3: Ruder;
    private int     value;      //1: A; 2: 2; ... 13: King;

    public Card(int type, int value){
        this.type = type;
        this.value = value;
    }

    public boolean isRed(){
        return 1 == this.type % 2;
    }

    public boolean isBlack(){
        return 0 == this.type % 2;
    }

    public boolean isDifferent(Card newCard){
        return newCard.getType() % 2 != this.type % 2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean equals(Card obj) {
        if(this.getValue() == obj.getValue() && this.getType() == obj.getType())
            return true;
        else
            return false;
    }

    public Card deepCopy() {
        Card card = new Card(this.type, this.value);
        return card;
    }

    public String getValueStr(){
        if(value == 1) return "es";
        if(value < 11) return String.valueOf(value);

        switch(value){
            case 11: return "knægt";
            case 12: return "dronning";
            case 13: return "konge";
        }
        return null;
    }

    public String getTypeStr(){
        switch(type){
            case 0: return "Spar";
            case 1: return "Hjerter";
            case 2: return "Klør";
            case 3: return "Ruder";
        }
        return null;
    }

}