package com.example.marc.rememberme.feature;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
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
    private RememberMeDb db;
    private GameHistory lastGameHistoryRecord;
    private Chronometer chronometer;

    public static final String LEARNED_DECK = "com.example.marc.rememberme.feature.DECK";
    public static final String LAST_GAME_HISTORY = "com.example.marc.rememberme.feature.Persistence.GAMEHISTORY";

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {

        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.learn_new_deck);
        recallManager = CardRecallPersistenceManager.getInstance(this);
        setUpDeckAndHistoryRecord();
        setCurrentPositionText(lastGameHistoryRecord.getLastPosition() + 1, deck.getCards().size());
        initializePagerView(deck);
        startChronometer();

    }

    private void setUpDeckAndHistoryRecord() {

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

            deck = bundle.getParcelable(LEARNED_DECK);
            lastGameHistoryRecord = bundle.getParcelable(LAST_GAME_HISTORY);

        } else {

            deck = new Deck(this);
            deck.shuffle();
            lastGameHistoryRecord = recallManager.saveNewGame(deck);

        }

    }

    private void initializePagerView(Deck deck) {

        viewPager = findViewById(R.id.newDeckPager);
        adapter = new ImagePagerAdapter(deck, this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new DetailOnPageChangeListener());

        if(lastGameHistoryRecord.getLastPosition() != 0) {

            viewPager.setCurrentItem(lastGameHistoryRecord.getLastPosition());

        }

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
        lastGameHistoryRecord.setCumulativeStateDuration(getChronometerDuration());
        lastGameHistoryRecord.setLastModDateTime(new Date());
        recallManager.updateGameState(lastGameHistoryRecord);

        //for debugging
        GameSummary gs = recallManager.getGameSummary(lastGameHistoryRecord.getSessionId(), lastGameHistoryRecord.getAttemptId());
        Log.d("DEBUG", "In LearnNewDeck > restartDeck.  GameSummary = " + gs.toString());
    }

    public void cancel(View view) {

        stopChronometer();
        lastGameHistoryRecord.setGameState("LEARNING");
        lastGameHistoryRecord.setGameStateStatus("CANCELLED");
        lastGameHistoryRecord.setLastPosition(currentPosition);
        lastGameHistoryRecord.setCumulativeStateDuration(getChronometerDuration());
        lastGameHistoryRecord.setLastModDateTime(new Date());
        recallManager.updateGameState(lastGameHistoryRecord);

        //for debugging
        GameSummary gs = recallManager.getGameSummary(lastGameHistoryRecord.getSessionId(), lastGameHistoryRecord.getAttemptId());
        Log.d("DEBUG", "In LearnNewDeck > cancel.  Game summary = " + gs.toString());
        //end debugging
        Intent intent = new Intent(this, MemorizeThings.class);
        startActivity(intent);

    }

    public void recallDeck(View view) {

        stopChronometer();
        Intent intent = new Intent(this, RecallDeck.class);
        intent.putExtra(LEARNED_DECK, deck);
        intent.putExtra(LAST_GAME_HISTORY, lastGameHistoryRecord);
        startActivity(intent);

    }

    private void startChronometer() {

        if(chronometer == null) {

            chronometer = (Chronometer) findViewById(R.id.learningChronometer);

        }

        chronometer.setBase(SystemClock.elapsedRealtime() - lastGameHistoryRecord.getCumulativeStateDuration());
        chronometer.start();

    }

    private void stopChronometer() {

        chronometer.stop();

    }

    private long getChronometerDuration() {

        return SystemClock.elapsedRealtime() - chronometer.getBase();

    }

    private class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {

            int numCards = adapter.getCount();
            setCurrentPositionText(position + 1, numCards);

        }

    }


}
