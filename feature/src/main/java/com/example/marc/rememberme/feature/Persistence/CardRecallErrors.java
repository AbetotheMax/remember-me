package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Marc on 3/29/2018.
 */

@Entity(
        tableName = "CARD_RECALL_ERRORS",
        foreignKeys = {
                @ForeignKey(entity = GameSummary.class, parentColumns = {"SESSION_ID", "ATTEMPT_ID"}, childColumns = {"SESSION_ID", "ATTEMPT_ID"}),
                @ForeignKey(entity = Decks.class, parentColumns = {"DECK_ID", "CARD_INDEX"}, childColumns = {"DECK_ID", "CARD_INDEX"})
            }
        )
public class CardRecallErrors {

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ERROR_ID")
    private int errorId;

    @ColumnInfo(name = "SESSION_ID")
    private int sessionId;

    @ColumnInfo(name = "ATTEMPT_ID")
    private int attemptId;

    @ColumnInfo(name = "DECK_ID")
    private int deckId;

    @ColumnInfo(name = "CARD_INDEX")
    private int cardIndex;

    @ColumnInfo(name = "CARD_NUMBER")
    private String cardNumber;

    @ColumnInfo(name = "CARD_SUIT")
    private String cardSuit;

    @ColumnInfo(name = "CARD_NUMBER_GUESSED")
    private String cardNumberGuessed;

    @ColumnInfo(name = "CARD_SUIT_GUESSED")
    private String cardSuitGuessed;

    public void setErrorId(int id) {
        this.errorId = id;
    }

    public int getErrorId() {
        return this.errorId;
    }

    public void setSessionId(int id) {
        this.sessionId = id;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public void setAttemptId(int id) {
        this.attemptId = id;
    }

    public int getAttemptId() {
        return this.attemptId;
    }

    public void setDeckId(int id) {
        this.deckId = id;
    }

    public int getDeckId() {
        return this.deckId;
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

    public void setCardNumberGuessed(String numberGuessed) {
        this.cardNumberGuessed = numberGuessed;
    }

    public String getCardNumberGuessed() {
        return this.cardNumberGuessed;
    }

    public void setCardSuitGuessed(String suitGuessed) {
        this.cardSuitGuessed = suitGuessed;
    }

    public String getCardSuitGuessed() {
        return this.cardSuitGuessed;
    }

    @Override
    public String toString() {
        return "Error ID: " + getErrorId() + ", Session ID: " + getSessionId() + ", Attempt ID: " + getAttemptId() +
                ", Deck ID: " + getDeckId() + ", Card Index: " + getCardIndex() + ", Card Number: " + getCardNumber() +
                ", Card Suit: " + getCardSuit() + ", Card Number Guessed: " + getCardNumberGuessed() +
                ", Card Suit Guessed: " + getCardSuitGuessed() + ".";
    }
}
