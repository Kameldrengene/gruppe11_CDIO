package com.example.gruppe11_cdio.Factory;

import androidx.annotation.Nullable;

/*
Mikkel Danielsen, s183913
Frederik Koefoed, s195463
Muhammad Talha, s195475
Volkan Isik, s180103
Lasse Strunge, s19548
Mark Mortensen, s174881
 */

public class Card {
    private int     type;       //0: spar; 1: Hjerter; 2: Klør; 3: Ruder;
    private int     value;      //1: A; 2: 2; ... 13: King;
    private int     lastValue = -1;

    public Card(int type, int value){
        this.type = type;
        this.value = value;
    }

    public int getLastValue() {
        return lastValue;
    }

    public void setLastValue(int lastValue) {
        this.lastValue = lastValue;
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
        else return false;
    }

    public boolean isEmpty() {
        return(type == 1 && value == 0);
    }

    public Card deepCopy() {
        Card card = new Card(this.type, this.value);
        return card;
    }
}