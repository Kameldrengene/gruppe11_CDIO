package com.example.gruppe11_cdio.Objects;

import java.util.ArrayList;
import com.example.gruppe11_cdio.Factory.Card;

public class Pile {
    private ArrayList<Card> shownCards;
    private ArrayList<Card> hiddenCards;

    public Pile(ArrayList<Card> shownCards, ArrayList<Card> hiddenCards){
        this.shownCards  = shownCards;
        this.hiddenCards = hiddenCards;
    }

    public boolean placePile(Pile pile) throws Exception {
        //If empty
        if (this.shownCards.size() == 0 && this.hiddenCards.size() == 0){
            if (pile.getShownCards().get(0).getValue() == 13){
                this.shownCards.addAll(pile.getShownCards());
                return true;
            }
            throw new Exception("Only kings may be moved to a free spot");
        }

        //If cards allready here
        if (this.shownCards.size() != 0){
            Card firstCard = this.shownCards.get(this.shownCards.size()-1); //first card already in pile
            Card newCard = pile.getShownCards().get(0);                     //new card being put on first card

            if (firstCard.isDifferent(newCard) &&                           //If different color
                    firstCard.getValue() == newCard.getValue() + 1){            //And if correct values
                this.shownCards.addAll(pile.getShownCards());
                return true;
            }
            throw new Exception("The cards can't be placed in that position");
        }

        throw new Exception("Error in placePile");
    }

    public boolean moveToPile(Pile pile) throws Exception {
        if (this.shownCards.size() == 0) throw new Exception("No cards in pile"); //no cards to move

        if (pile.placePile(this)){
            this.shownCards.clear();
            if (this.hiddenCards.size() != 0){
                int lastHiddenPos = this.hiddenCards.size()-1;
                this.shownCards.add(this.hiddenCards.get(lastHiddenPos));
                this.hiddenCards.remove(lastHiddenPos);
            }
            return true;
        }
        throw new Exception("Error in moveToPile"); //Shouldn't happen
    }

    public boolean moveToPile(Pile pile, int n) throws Exception {
        if (this.shownCards.size() < n) throw new Exception("Not enough cards in pile"); //no cards to move

        ArrayList<Card> subShownList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int lastIndex = this.shownCards.size() - 1;
            subShownList.add(0, this.shownCards.get(lastIndex));
            this.shownCards.remove(lastIndex);
        }
        ArrayList<Card> subCopy = new ArrayList<>();
        for (Card card : subShownList){
            subCopy.add(new Card(card.getType(), card.getValue()));
        }

        Pile subPile = new Pile(subShownList, new ArrayList<>());
        try {
            if (pile.placePile(subPile)) {
                if (this.hiddenCards.size() != 0 && this.shownCards.size() == 0) {
                    int lastHiddenPos = this.hiddenCards.size() - 1;
                    this.shownCards.add(this.hiddenCards.get(lastHiddenPos));
                    this.hiddenCards.remove(lastHiddenPos);
                }
                return true;
            }
            throw new Exception("Can't move that subpile there.");
        } catch (Exception e){
            this.shownCards.addAll(subCopy);
            throw e;
        }
    }


    public ArrayList<Card> getShownCards() {
        return shownCards;
    }

    public void setShownCards(ArrayList<Card> shownCards) {
        this.shownCards = shownCards;
    }

    public ArrayList<Card> getHiddenCards() {
        return hiddenCards;
    }

    public void setHiddenCards(ArrayList<Card> hiddenCards) {
        this.hiddenCards = hiddenCards;
    }

    public int getNumberOfCards(){
        return shownCards.size() + hiddenCards.size();
    }

    public ArrayList<Card> getCardsInSequence(){
        ArrayList<Card> out = new ArrayList<>();
        out.addAll(hiddenCards);
        out.addAll(shownCards);
        return out;
    }

    public void setCardsInSequence(ArrayList<Card> cards){
        clearCards();

        for (int i = 0; i < cards.size(); i++) {
            if(cards.get(i).equals(new Card(1,0)))
                hiddenCards.add(cards.get(i));
            else shownCards.add(cards.get(i));
        }

    }

    public void clearCards(){
        shownCards.clear();
        hiddenCards.clear();
    }
}
