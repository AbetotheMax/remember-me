package com.example.marc.rememberme.feature;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by Marc on 2/27/2018.
 */

class Card {

    private Suit suit;
    private CardNumber cardNumber;
    private String imageFile;
    private Context context;

    public Card(Context current, Suit suit, CardNumber cardNumber) {

        this.suit = suit;
        this.cardNumber = cardNumber;
        this.context = current;

    }

    public Suit getSuit() {

        return this.suit;

    }

    public CardNumber getCardNumber() {

        return this.cardNumber;

    }

    public Card getCard() {

        return this;

    }

    public int getCardImageId() {

        Resources res = context.getResources();
        String imageName = getCardNumber().toString().toLowerCase() + "_of_" + getSuit().toString().toLowerCase();
        return res.getIdentifier(imageName, "drawable", context.getPackageName());

    }

}
