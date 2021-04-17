package com.example.gruppe11_cdio.Factory;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.example.gruppe11_cdio.R;


public class Card_Factory {
    int [][] cardMap =  new int[4][15];
    Context context;

    public Card_Factory(Context context) {
        this.context = context;
        cardMap[0][0] = R.drawable.card_back;
        cardMap[0][1] = R.drawable.spades_a;
        cardMap[0][2] = R.drawable.spades_2;
        cardMap[0][3] = R.drawable.spades_3;
        cardMap[0][4] = R.drawable.spades_4;
        cardMap[0][5] = R.drawable.spades_5;
        cardMap[0][6] = R.drawable.spades_6;
        cardMap[0][7] = R.drawable.spades_7;
        cardMap[0][8] = R.drawable.spades_8;
        cardMap[0][9] = R.drawable.spades_9;
        cardMap[0][10] = R.drawable.spades_10;
        cardMap[0][11] = R.drawable.spades_j;
        cardMap[0][12] = R.drawable.spades_q;
        cardMap[0][13] = R.drawable.spades_k;
        cardMap[1][0] = R.drawable.holder;
        cardMap[1][1] = R.drawable.hearts_a;
        cardMap[1][2] = R.drawable.hearts_2;
        cardMap[1][3] = R.drawable.hearts_3;
        cardMap[1][4] = R.drawable.hearts_4;
        cardMap[1][5] = R.drawable.hearts_5;
        cardMap[1][6] = R.drawable.hearts_6;
        cardMap[1][7] = R.drawable.hearts_7;
        cardMap[1][8] = R.drawable.hearts_8;
        cardMap[1][9] = R.drawable.hearts_9;
        cardMap[1][10] = R.drawable.hearts_10;
        cardMap[1][11] = R.drawable.hearts_j;
        cardMap[1][12] = R.drawable.hearts_q;
        cardMap[1][13] = R.drawable.hearts_k;
        cardMap[2][0] = R.drawable.card_back;
        cardMap[2][1] = R.drawable.clubs_a;
        cardMap[2][2] = R.drawable.clubs_2;
        cardMap[2][3] = R.drawable.clubs_3;
        cardMap[2][4] = R.drawable.clubs_4;
        cardMap[2][5] = R.drawable.clubs_5;
        cardMap[2][6] = R.drawable.clubs_6;
        cardMap[2][7] = R.drawable.clubs_7;
        cardMap[2][8] = R.drawable.clubs_8;
        cardMap[2][9] = R.drawable.clubs_9;
        cardMap[2][10] = R.drawable.clubs_10;
        cardMap[2][11] = R.drawable.clubs_j;
        cardMap[2][12] = R.drawable.clubs_q;
        cardMap[2][13] = R.drawable.clubs_k;
        cardMap[3][0] = R.drawable.card_back;
        cardMap[3][1] = R.drawable.diamonds_a;
        cardMap[3][2] = R.drawable.diamonds_2;
        cardMap[3][3] = R.drawable.diamonds_3;
        cardMap[3][4] = R.drawable.diamonds_4;
        cardMap[3][5] = R.drawable.diamonds_5;
        cardMap[3][6] = R.drawable.diamonds_6;
        cardMap[3][7] = R.drawable.diamonds_7;
        cardMap[3][8] = R.drawable.diamonds_8;
        cardMap[3][9] = R.drawable.diamonds_9;
        cardMap[3][10] = R.drawable.diamonds_10;
        cardMap[3][11] = R.drawable.diamonds_j;
        cardMap[3][12] = R.drawable.diamonds_q;
        cardMap[3][13] = R.drawable.diamonds_k;
        cardMap[0][14] = R.drawable.card_back;
        cardMap[1][14] = R.drawable.card_back;
        cardMap[2][14] = R.drawable.card_back;
        cardMap[3][14] = R.drawable.card_back;
    }

    public ImageView createCard(Card card){
        ImageView cardView = new ImageView(context);
        cardView.setBackgroundResource(cardMap[card.getType()][card.getValue()]);
        cardView.setId(View.generateViewId());
        return cardView;
    }
}
