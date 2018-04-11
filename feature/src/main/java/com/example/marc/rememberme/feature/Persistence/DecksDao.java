package com.example.marc.rememberme.feature.Persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Marc on 3/29/2018.
 */

@Dao
public interface DecksDao {

    @Query("SELECT * FROM DECKS WHERE DECK_ID = :deckId")
    public List<Decks> loadAllCardsFromDeck(int deckId);

    @Query("SELECT CARD_NUMBER FROM DECKS WHERE DECK_ID = :deckId AND CARD_INDEX = :cardIndex")
    public String getCardNumber(int deckId, int cardIndex);

    @Query("SELECT CARD_SUIT FROM DECKS WHERE DECK_ID = :deckId AND CARD_INDEX = :cardIndex")
    public String getCardSuit(int deckId, int cardIndex);

    @Query("SELECT MAX(DECK_ID) FROM DECKS")
    public int getMaxDeckId();

    @Insert
    public void insertDeckRecord(Decks decks);

    @Insert
    public void insertDeck(List<Decks> decks);

}
