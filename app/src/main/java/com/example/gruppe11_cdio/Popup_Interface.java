package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;
import com.example.gruppe11_cdio.Factory.Card;
import java.util.ArrayList;

/*
Mikkel Danielsen, s183913
Frederik Koefoed, s195463
Muhammad Talha, s195475
Volkan Isik, s180103
Lasse Strunge, s19548
Mark Mortensen, s174881
 */

public abstract class Popup_Interface extends AppCompatActivity{
    public abstract void onSave(Card card, int CODE);
    public abstract void onSave(ArrayList<Card> arrayOfCards, int CODE);
}
