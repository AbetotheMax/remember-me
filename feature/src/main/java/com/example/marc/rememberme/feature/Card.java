package com.example.marc.rememberme.feature;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

/**
 * Created by Marc on 2/27/2018.
 */

class Card implements Parcelable{

    private Suit suit;
    private CardNumber cardNumber;
    private int imageId;
    private Context context;

    public Card(Context current, Suit suit, CardNumber cardNumber) {

        this.suit = suit;
        this.cardNumber = cardNumber;
        this.context = current;
        this.imageId = -1;

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

        if(imageId > -1) {

            return imageId;

        }

        Resources res = context.getResources();
        String imageName = getCardNumber().toString().toLowerCase() + "_of_" + getSuit().toString().toLowerCase();
        return res.getIdentifier(imageName, "drawable", context.getPackageName());

    }

    public Card(Parcel in) {

        this.suit = Suit.valueOf(in.readInt());
        this.cardNumber = CardNumber.valueOf(in.readInt());
        this.imageId = in.readInt();

    }


    @Override
    public int describeContents() {

        return 0;

    }

    @Override
    public void writeToParcel(Parcel dest,int flags) {

        dest.writeInt(this.suit.getValue());
        dest.writeInt(this.cardNumber.getValue());
        dest.writeInt(getCardImageId());

    }

    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {

        public Card createFromParcel(Parcel in) {

            return new Card(in);

        }

        public Card[] newArray(int size) {

            return new Card[size];

        }

    };

}
