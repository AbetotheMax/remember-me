package com.example.marc.rememberme.feature;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Marc on 2/27/2018.
 */

public class Deck {

    private List<Card> cards;

    public Deck(Context current) {

        this.cards = makeDeck(current);

    }

    private List<Card> makeDeck(Context current) {

        if(cards != null) {

            return cards;

        }

        List<Card> newCards = new ArrayList<>();

        for(int i = 0; i < 4; i++) {

            for (int j = 1; j < 14; j++) {

                Card nextCard = new Card(current, Suit.valueOf(i), CardNumber.valueOf(j));
                newCards.add(nextCard);

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

    public void shuffle() {

        Random rnd = ThreadLocalRandom.current();

        for (int i = cards.size() - 1; i > 0; i--) {

            int index = rnd.nextInt(i + 1);
            Card tempCard = cards.get(index);
            cards.set(index, cards.get(i));
            cards.set(i, tempCard);

        }

    }

}
