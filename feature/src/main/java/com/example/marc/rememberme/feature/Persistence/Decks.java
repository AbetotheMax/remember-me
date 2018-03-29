package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Marc on 3/29/2018.
 */

@Entity(tableName = "DECKS", primaryKeys = {"DECK_ID", "CARD_INDEX"})
public class Decks {

    @ColumnInfo(name = "DECK_ID")
    private int deckId;

    @ColumnInfo(name = "NUM_DECKS")
    private int numDecks;

    @ColumnInfo(name = "CARD_INDEX")
    private int cardIndex;

    @ColumnInfo(name = "CARD_NUMBER")
    private String cardNumber;

    @ColumnInfo(name = "CARD_SUIT")
    private String cardSuit;

    public void setDeckId(int id) {
        this.deckId = id;
    }

    public int getDeckId() {
        return this.deckId;
    }

    public void setNumDecks(int numDecks) {
        this.numDecks = numDecks;
    }

    public int getNumDecks() {
        return this.numDecks;
    }

    public void setCardIndex(int index) {
        this.cardIndex = index;
    }

    public int getCardIndex() {
        return this.cardIndex;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardSuit(String suit) {
        this.cardSuit = suit;
    }

    public String getCardSuit() {
        return this.cardSuit;
    }

}
