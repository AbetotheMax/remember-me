package com.example.marc.rememberme.feature;

import android.content.Context;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
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

    public Map<String, List<GameSummary>> loadPriorGameSummaries() {
        Map<String, List<GameSummary>> priorGames = new HashMap<>();
        List<String> gameDates = recallManager.getDatesOfPriorGames();

        for(String gameDate : gameDates) {

            List<GameSummary> gameSummary = recallManager.loadGameSummariesForDate(gameDate);
            priorGames.put(gameDate, gameSummary);

        }
        
        return priorGames;
    }

}
