package com.example.gruppe11_cdio;

import androidx.appcompat.app.AppCompatActivity;
import com.example.gruppe11_cdio.Factory.Card;
import java.util.ArrayList;

public abstract class Popup_EditorInterface extends AppCompatActivity{
    public abstract void onSave(Card card, int CODE);
    public abstract void onSave(ArrayList<Card> arrayOfCards, int CODE);
}
