package com.example.gruppe11_cdio.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import com.example.gruppe11_cdio.Factory.Card;

public class GameBoard {
    private HashMap<Integer, ArrayList<Card>> finSpaces; //key is type: 0: spar; 1: Hjerter; 2: Klør; 3: Ruder;
    private ArrayList<Card> deck;
    private int deckPointer;
    private HashMap<Integer, Pile> spaces;

    private static GameBoard instance = new GameBoard();
    public static GameBoard getInstance(){ return instance; }

    //Singleton
    private GameBoard() {
        setUpGame();
    }

    private void setUpGame(){
        finSpaces = new HashMap<>();
        deck = new ArrayList<>();
        deckPointer = -1;
        spaces = new HashMap<>();

        for (int i = 0; i < 4; i++){
            finSpaces.put(i, new ArrayList<>());
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                Card card = new Card(i, j+1);
                deck.add(card);
            }
        }
        Collections.shuffle(deck, new Random(System.currentTimeMillis()));

        for (int i = 0; i < 7; i++) {
            ArrayList<Card> hidden = new ArrayList<>();
            ArrayList<Card> shown = new ArrayList<>();
            for (int j = 0; j < i + 1; j++) {
                if (j == i){
                    shown.add(deck.get(0));
                } else {
                    hidden.add(deck.get(0));

                }
                deck.remove(0);
            }
            spaces.put(i, new Pile(shown, hidden));
        }
    }

    public boolean draw() {
        deckPointer += 1;
        if (deckPointer == deck.size()) deckPointer = -1;
        return true;
    }

    public boolean move(String xs, String ys) throws Exception {
        int y;
        try {
            y = Integer.parseInt(ys);
            if (y < 0 || 6 < y) throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new Exception("y is not 0-6");
        }

        if (xs.equals("q")){ //Take card from deck
            if (deckPointer == -1) throw new Exception("No card in q");
            ArrayList<Card> fromDeck = new ArrayList<>();
            fromDeck.add(deck.get(deckPointer));
            Pile pile = new Pile(fromDeck, new ArrayList<>());
            if (spaces.get(y).placePile(pile)){
                deck.remove(deckPointer);
                deckPointer -= 1;
                return true;
            }
        } else {
            int x;
            try {
                x = Integer.parseInt(xs);
            } catch (NumberFormatException e){
                throw new Exception("x is not 0-6 or q");
            }

            if (x == y) throw new Exception("x and y is the same");

            Pile movingPile = spaces.get(x);
            Pile endPile = spaces.get(y);

            boolean pointedMoved = true;
            if (movingPile.getHiddenCards().size() == 0 && movingPile.getShownCards().get(0).getValue() == 13) pointedMoved = false;

            if (movingPile.moveToPile(endPile)){
                return pointedMoved;
            }

        }
        throw new Exception("Error in move");
    }

    public boolean move(String xs, String ys, String ns) throws Exception {
        int x;
        try {
            x = Integer.parseInt(xs);
            if (x < 0 || 6 < x) throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new Exception("x is not 0-6");
        }

        int y;
        try {
            y = Integer.parseInt(ys);
            if (y < 0 || 6 < y) throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new Exception("y is not 0-6");
        }

        int n;
        try {
            n = Integer.parseInt(ns);
            if (n < 0 || 13 < n) throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new Exception("n is not 0-13");
        }

        if (x == y) throw new Exception("x and y are the same");

        Pile movingPile = spaces.get(x);
        Pile endPile = spaces.get(y);

        if (movingPile.moveToPile(endPile, n)){
            return true;
        }

        throw new Exception("Error in move");
    }

    public boolean score(String xs) throws Exception{

        if (xs.equals("q")) {
            if (deckPointer == -1) throw new Exception("No card in q");
            Card card = deck.get(deckPointer);
            int nextNeeded = finSpaces.get(card.getType()).size() + 1;
            if (card.getValue() == nextNeeded){
                finSpaces.get(card.getType()).add(card);
                deck.remove(deckPointer);
                deckPointer -= 1;
                return true;
            } else {
                throw new Exception("Card at pos " + xs + " can't be scored yet");
            }

        } else {
            int x;
            try {
                x = Integer.parseInt(xs);
                if (x < 0 || 6 < x) throw new NumberFormatException();
            } catch (NumberFormatException e){
                throw new Exception("x is not 0-6 or q");
            }
            int lastIndex = spaces.get(x).getShownCards().size() - 1;
            Card card = spaces.get(x).getShownCards().get(lastIndex);
            int nextNeeded = finSpaces.get(card.getType()).size() + 1;
            if (card.getValue() == nextNeeded){
                finSpaces.get(card.getType()).add(card);
                spaces.get(x).getShownCards().remove(lastIndex);
                if (spaces.get(x).getShownCards().size() == 0 && spaces.get(x).getHiddenCards().size() != 0){
                    int hiddenLastIndex = spaces.get(x).getHiddenCards().size() - 1;
                    spaces.get(x).getShownCards().add(spaces.get(x).getHiddenCards().get(hiddenLastIndex));
                    spaces.get(x).getHiddenCards().remove(hiddenLastIndex);
                }
                return true;
            } else {
                throw new Exception("Card at pos " + xs + " can't be scored yet");
            }
        }
    }

    public boolean remove(String ks, String ys) throws Exception {
        if (ks.length() != 1 || ys.length() != 1) throw new Exception("Error in input");
        int k = ((int) ks.charAt(0)) - 97;
        if (k < 0 || 3 < k) throw new Exception("k is not a-d");

        int y;
        try {
            y = Integer.parseInt(ys);
            if (y < 0 || 6 < y) throw new NumberFormatException();
        } catch (NumberFormatException e){
            throw new Exception("y is not 0-6");
        }

        int lastFinIndex = finSpaces.get(k).size() - 1;
        if (lastFinIndex == -1) throw new Exception("No cards in pos " + ks);
        Card finCard = finSpaces.get(k).get(lastFinIndex);

        int lastSpaceIndex = spaces.get(y).getShownCards().size() - 1;

        if (finCard.getValue() == 13 && lastSpaceIndex == -1){
            spaces.get(y).getShownCards().add(finCard);
            finSpaces.get(k).remove(lastFinIndex);
            return true;
        }
        if (lastSpaceIndex == -1) throw new Exception("Can't return card from pos " + ks + " to pos " + ys);
        Card spaceCard = spaces.get(y).getShownCards().get(lastSpaceIndex);

        if (finCard.isDifferent(spaceCard) && finCard.getValue() + 1 == spaceCard.getValue()){
            spaces.get(y).getShownCards().add(finCard);
            finSpaces.get(k).remove(lastFinIndex);
            return true;
        } else {
            throw new Exception("Can't return card from pos " + ks + " to pos " + ys);
        }
    }

    public String isWon(){
        boolean win = true;
        for (int key : spaces.keySet()){
            if (spaces.get(key).getHiddenCards().size() != 0){
                win = false;
                break;
            }
        }
        if (win) return " -- SPILLET ER VUNDET! --";

        return "";
    }


    public HashMap<Integer, ArrayList<Card>> getFinSpaces() {
        return finSpaces;
    }

    public void setFinSpaces(HashMap<Integer, ArrayList<Card>> finSpaces) {
        this.finSpaces = finSpaces;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public int getDeckPointer() {
        return deckPointer;
    }

    public void setDeckPointer(int deckPointer) {
        this.deckPointer = deckPointer;
    }

    public HashMap<Integer, Pile> getSpaces() {
        return spaces;
    }

    public void setSpaces(HashMap<Integer, Pile> spaces) {
        this.spaces = spaces;
    }
}
