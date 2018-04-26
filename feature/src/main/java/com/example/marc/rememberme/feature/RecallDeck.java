package com.example.marc.rememberme.feature;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistory;
import com.example.marc.rememberme.feature.Persistence.GameSummary;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.marc.rememberme.feature.LearnNewDeck.LAST_GAME_HISTORY;
import static com.example.marc.rememberme.feature.LearnNewDeck.LEARNED_DECK;

/**
 * Created by Marc on 3/15/2018.
 */

public class RecallDeck extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<Suit> expandableListTitle;
    HashMap<Suit, List<Card>> expandableListDetail;
    Deck deckToRecall;
    GameHistory lastGameHistoryRecord;
    private ViewPager pager;
    private Chronometer chronometer;
    private CardRecallPersistenceManager recallManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recall_deck);

        Bundle bundle = getIntent().getExtras();
        deckToRecall = bundle.getParcelable(LEARNED_DECK);
        lastGameHistoryRecord = bundle.getParcelable(LAST_GAME_HISTORY);
        pager = (ViewPager) findViewById(R.id.recallDeckPager);
        recallManager = CardRecallPersistenceManager.getInstance(this);

        if(lastGameHistoryRecord.getGameState().equals("LEARNING")) {

            lastGameHistoryRecord.setGameState("RECALL");
            lastGameHistoryRecord.setGameStateStatus("STARTED");
            lastGameHistoryRecord.setLastPosition(0);
            lastGameHistoryRecord.setCumulativeStateDuration(0);

        } else {

            lastGameHistoryRecord.setGameStateStatus("RESUMED");

        }

        lastGameHistoryRecord.setLastModDateTime(new Date());
        recallManager.updateGameState(lastGameHistoryRecord);
        GameSummary gs = recallManager.getGameSummary(lastGameHistoryRecord.getSessionId(), lastGameHistoryRecord.getAttemptId());
        Log.d("DEBUG", "Starting Recall.  GameSummary = " + gs.toString());


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = CardSuitsExpandableListDataPump.getData(this);
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CardSuitsExpandableListAdapter(this, expandableListTitle, expandableListDetail, deckToRecall, pager, findViewById(android.R.id.content), lastGameHistoryRecord);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                return false;

            }
        });

        chronometer = (Chronometer) findViewById(R.id.recallChronometer);
        chronometer.start();

    }

    public void cancel(View view) {

        chronometer.stop();
        DeckRecallResults results = DeckRecallResults.getInstance(deckToRecall, this, pager, findViewById(android.R.id.content), lastGameHistoryRecord);
        lastGameHistoryRecord.setGameState("RECALL");
        lastGameHistoryRecord.setGameStateStatus("CANCELLED");
        lastGameHistoryRecord.setLastPosition(results.getRecallPosition());
        lastGameHistoryRecord.setCumulativeStateDuration(chronometer.getBase());
        lastGameHistoryRecord.setLastModDateTime(new Date());
        recallManager.updateGameState(lastGameHistoryRecord);
        GameSummary gs = recallManager.getGameSummary(lastGameHistoryRecord.getSessionId(), lastGameHistoryRecord.getAttemptId());
        Log.d("DEBUG", "Cancelling Recall.  GameSummary = " + gs.toString());

    }

}
