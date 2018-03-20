package com.example.marc.rememberme.feature;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marc on 3/15/2018.
 */

public class CardSuitsExpandableListDataPump {

    public static HashMap<Suit, List<Card>> getData(Context context) {

        HashMap<Suit, List<Card>> expandableListDetail = new HashMap<>();

        expandableListDetail.put(Suit.CLUBS, loadCards(context, Suit.CLUBS));
        expandableListDetail.put(Suit.SPADES, loadCards(context, Suit.SPADES));
        expandableListDetail.put(Suit.DIAMONDS, loadCards(context, Suit.DIAMONDS));
        expandableListDetail.put(Suit.HEARTS, loadCards(context, Suit.HEARTS));

        return expandableListDetail;

    }

    private static List<Card> loadCards(Context context, Suit suit) {

        List<Card> cards = new ArrayList<>();

        for(int i = 1; i < 14; i++) {

            cards.add(new Card(context, suit, CardNumber.valueOf(i)));

        }

        return cards;

    }

}

