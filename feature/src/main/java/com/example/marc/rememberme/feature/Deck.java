package com.example.marc.rememberme.feature;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Marc on 2/27/2018.
 */

public class Deck implements Parcelable{

    private List<Card> cards;
    private int numDecks;

    public Deck() {

        this.cards = new ArrayList<>();

    }

    public Deck(Context current) {

        this(current, 1);

    }

    public Deck(Context current, int numDecks) {

        this.numDecks = numDecks;
        this.cards = makeDeck(current);
    }

    private List<Card> makeDeck(Context current) {

        if(cards != null) {

            return cards;

        }

        List<Card> newCards = new ArrayList<>();

        for(int deckIndex = 0; deckIndex < numDecks; deckIndex++) {
            for (int i = 0; i < 4; i++) {

                for (int j = 1; j < 14; j++) {

                    Card nextCard = new Card(current, Suit.valueOf(i), CardNumber.valueOf(j));
                    newCards.add(nextCard);

                }

            }
        }

        return newCards;

    }

    public List<Card> getCards() {

        return this.cards;

    }

    public Card getCard(int index) {

        return cards.get(index);

    }

    public boolean add(Card card) {

        return this.cards.add(card);

    }

    public void shuffle() {

        Random rnd = ThreadLocalRandom.current();

        for (int i = cards.size() - 1; i > 0; i--) {

            int index = rnd.nextInt(i + 1);
            Card tempCard = cards.get(index);
            cards.set(index, cards.get(i));
            cards.set(i, tempCard);

        }

    }

    public int getNumDecks() {

        return this.numDecks;

    }

    public Deck(Parcel in) {

        this.cards = new ArrayList<>();
        in.readTypedList(cards, Card.CREATOR);

    }


    @Override

    public int describeContents() {

        return 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeTypedList(cards);

    }

    public static final Parcelable.Creator<Deck> CREATOR = new Parcelable.Creator<Deck>() {

        public Deck createFromParcel(Parcel in) {

            return new Deck(in);

        }

        public Deck[] newArray(int size) {

            return new Deck[size];

        }

    };

}
