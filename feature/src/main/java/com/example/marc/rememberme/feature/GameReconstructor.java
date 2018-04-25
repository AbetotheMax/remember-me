package com.example.marc.rememberme.feature;

import android.content.Context;
import android.content.Intent;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistory;
import com.example.marc.rememberme.feature.Persistence.GameHistoryOverview;
import com.example.marc.rememberme.feature.Persistence.GameSummary;

import static com.example.marc.rememberme.feature.LearnNewDeck.LAST_GAME_HISTORY;

/**
 * Created by Marc on 4/24/2018.
 */

public class GameReconstructor {

    public static final String LEARNED_DECK = "com.example.marc.rememberme.feature.DECK";
    public static final String GAME_SUMMARY = "com.example.marc.rememberme.feature.Persistence.GAMESUMMARY";

    private static CardRecallPersistenceManager recallManager;

    public static void reconstructGame(Context context, GameHistoryOverview overview) {

        recallManager = CardRecallPersistenceManager.getInstance(context);
        Deck deck = recallManager.loadDeckForId(overview.getDeckId());
        GameSummary gameSummary = recallManager.getLastGameSummaryForComponent(overview.getDeckId());

        if(overview.getProgress() == 100) {

            GameHistory lastGameHistory = recallManager.saveNewGame(deck, overview.getDeckId());

            Intent intent = new Intent(context, LearnNewDeck.class);
            intent.putExtra(LEARNED_DECK, deck);
            intent.putExtra(LAST_GAME_HISTORY, lastGameHistory);
            startActivity(intent);

            }


        }

    }

}
