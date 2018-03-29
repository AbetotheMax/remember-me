package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Marc on 3/29/2018.
 */

@Dao
public interface DecksDao {

    @Query("SELECT * FROM DECKS WHERE DECK_ID = :deckId")
    List<Decks> loadAllCardsFromDeck(int deckId);

}
