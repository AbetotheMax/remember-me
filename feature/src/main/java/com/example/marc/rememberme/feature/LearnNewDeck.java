package com.example.marc.rememberme.feature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistory;
import com.example.marc.rememberme.feature.Persistence.GameStates;
import com.example.marc.rememberme.feature.Persistence.GameSummary;
import com.example.marc.rememberme.feature.Persistence.RememberMeDb;

import java.util.Date;

/**
 * Created by Marc on 3/11/2018.
 */

public class LearnNewDeck extends AppCompatActivity {

    private Deck deck;
    private ViewPager viewPager;
    private ImagePagerAdapter adapter;
    private CardRecallPersistenceManager recallManager;
    private int currentPosition;
    // for debugging
    private RememberMeDb db;
    private int maxDeckId;
    private GameHistory lastGameHistoryRecord;

    public static final String LEARNED_DECK = "com.example.marc.rememberme.feature.DECK";
    public static final String LAST_GAME_HISTORY = "com.example.marc.rememberme.feature.Persistence.GAMEHISTORY";

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {

        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.learn_new_deck);
        deck = new Deck(this);
        deck.shuffle();
        recallManager = CardRecallPersistenceManager.getInstance(this);
        lastGameHistoryRecord = recallManager.saveNewGame(deck);
        // begin debugging
        maxDeckId = recallManager.getMaxDeckId();
        Log.d("DEBUG", "Deck id = " + maxDeckId);
        Log.d("DEBUG", "First card number = " + recallManager.getCardNumber(maxDeckId, 0));
        Log.d("DEBUG", "First card suit = " + recallManager.getCardSuit(maxDeckId, 0));
        //end debugging
        setCurrentPositionText(1, deck.getCards().size());
        initializePagerView(deck);

    }

    private void initializePagerView(Deck deck) {

        viewPager = findViewById(R.id.newDeckPager);
        adapter = new ImagePagerAdapter(deck, this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new DetailOnPageChangeListener());

    }

    private void setCurrentPositionText(int position, int numCards) {

        currentPosition = position;
        String currentPositionText = currentPosition + " / " + numCards;
        TextView textView = findViewById(R.id.currentPositionText);
        textView.setText(currentPositionText);

    }

    public void restartDeck(View view) {

        viewPager.setCurrentItem(0);
        lastGameHistoryRecord.setGameState("LEARNING");
        lastGameHistoryRecord.setGameStateStatus("RESTARTED");
        lastGameHistoryRecord.setLastPosition(currentPosition);
        // TODO: Add logic to update duration.  Setting to 0 here as a placeholder
        lastGameHistoryRecord.setCumulativeStateDuration(0);
        lastGameHistoryRecord.setLastModDateTime(new Date());
        recallManager.updateGameState(lastGameHistoryRecord);
        GameSummary gs = recallManager.getGameSummary(lastGameHistoryRecord.getSessionId(), lastGameHistoryRecord.getAttemptId());
        Log.d("DEBUG", "Session id = " + gs.getSessionId() + "; Game State = " + gs.getGameState() + "; game state status = " + gs.getGameStateStatus() +
            "; errors");
    }

    public void cancel(View view) {
        lastGameHistoryRecord.setGameState("LEARNING");
        lastGameHistoryRecord.setGameStateStatus("CANCELLED");
        lastGameHistoryRecord.setLastPosition(currentPosition);
        // TODO: Add logic to update duration.  Setting to 0 here as a placeholder
        lastGameHistoryRecord.setCumulativeStateDuration(0);
        lastGameHistoryRecord.setLastModDateTime(new Date());
        recallManager.updateGameState(lastGameHistoryRecord);
        Intent intent = new Intent(this, MemorizeThings.class);
        startActivity(intent);
        GameSummary gs = db.gameSummaryDao().getGameSummary(db.gameHistoryDao().getMaxSessionId("CARD RECALL"), 1);
        Log.d("DEBUG", "In cancel.  Game summary = " + gs);

    }

    public void recallDeck(View view) {

        Intent intent = new Intent(this, RecallDeck.class);
        intent.putExtra(LEARNED_DECK, deck);
        intent.putExtra(LAST_GAME_HISTORY, lastGameHistoryRecord);
        startActivity(intent);

    }

    private class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {

            int numCards = adapter.getCount();
            setCurrentPositionText(position + 1, numCards);

        }

    }


}
