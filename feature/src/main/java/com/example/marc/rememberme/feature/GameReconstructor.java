package com.example.marc.rememberme.feature;

import android.content.Context;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistoryOverview;

/**
 * Created by Marc on 4/24/2018.
 */

public class GameReconstructor {

    private static CardRecallPersistenceManager recallManager;

    public static void reconstructGame(Context context, GameHistoryOverview overview) {

        recallManager = CardRecallPersistenceManager.getInstance(context);
        Deck deck = recallManager.loadDeckForId(overview.getDeckId());

    }

}
