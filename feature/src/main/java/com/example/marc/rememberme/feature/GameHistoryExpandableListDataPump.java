package com.example.marc.rememberme.feature;

import android.content.Context;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistoryOverview;
import com.example.marc.rememberme.feature.Persistence.GameSummary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marc on 4/16/2018.
 */

public class GameHistoryExpandableListDataPump {

    private CardRecallPersistenceManager recallManager;
    private Context context;

    public GameHistoryExpandableListDataPump(Context context) {
        this.context = context;
        this.recallManager = CardRecallPersistenceManager.getInstance(context);
    }

    public Map<String, List<GameHistoryOverview>> loadPriorGameOverviews() {
        Map<String, List<GameHistoryOverview>> priorGames = new HashMap<>();
        List<String> gameDates = recallManager.getDatesOfPriorGames();

        for(String gameDate : gameDates) {

            List<GameHistoryOverview> gameOverview = recallManager.loadGameOverviewsForDate(gameDate);
            priorGames.put(gameDate, gameOverview);

        }
        
        return priorGames;
    }

}
