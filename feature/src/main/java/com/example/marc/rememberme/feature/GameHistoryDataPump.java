package com.example.marc.rememberme.feature;

import android.content.Context;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistoryOverview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marc on 4/19/2018.
 */

public class GameHistoryDataPump {

    private CardRecallPersistenceManager recallManager;
    private Context context;

    public GameHistoryDataPump (Context context) {
        this.context = context;
        this.recallManager = CardRecallPersistenceManager.getInstance(context);
    }

    public List<GameHistoryOverview> loadPriorGameOverviews() {

        List<GameHistoryOverview> priorGames = recallManager.loadGameOverviews();
        return priorGames;

    }
}
