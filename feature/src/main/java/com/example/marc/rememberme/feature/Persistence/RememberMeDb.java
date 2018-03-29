package com.example.marc.rememberme.feature.Persistence;

import android.arch.persistence.room.Database;

/**
 * Created by Marc on 3/29/2018.
 */

@Database(
        entities = {
                Games.class,
                GameStates.class,
                GameHistory.class,
                Decks.class,
                CardRecallErrors.class
        },
        version = 1
)
public class RememberMeDb {



}
