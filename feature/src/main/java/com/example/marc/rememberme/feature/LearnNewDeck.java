package com.example.marc.rememberme.feature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameStates;
import com.example.marc.rememberme.feature.Persistence.GameSummary;
import com.example.marc.rememberme.feature.Persistence.RememberMeDb;

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

    public static final String LEARNED_DECK = "com.example.marc.rememberme.feature.DECK";

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {

        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.learn_new_deck);
        deck = new Deck(this);
        deck.shuffle();
        recallManager = CardRecallPersistenceManager.getInstance(this);
        recallManager.saveNewGame(deck);
        // begin debugging
        maxDeckId = recallManager.getDeckId();
        Log.d("DEBUG", "Deck id = " + maxDeckId);
        Log.d("DEBUG", "First card number = " + db.decksDao().getCardnumber(maxDeckId, 0));
        Log.d("DEBUG", "First card suit = " + db.decksDao().getCardSuit(maxDeckId, 0));
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
        recallManager.updateGameState(false, "LEARNING", "RESTARTED", currentPosition, 0);
        GameSummary gs = db.gameSummaryDao().getGameSummary(db.gameHistoryDao().getMaxSessionId("CARD RECALL"), 1);
        //Log.d("DEBUG", "Session id = " + gs.getSessionId() + "; Game State = " + gs.getGameState() + "; game state status = " + gs.getGameStateStatus() +
          //  "; errors")
        Log.d("DEBUG", "In restartDeck.  Game summary = " + gs);

    }

    public void cancel(View view) {

        recallManager.updateGameState(false, "LEARNING", "CANCELLED", currentPosition, 0);
        Intent intent = new Intent(this, MemorizeThings.class);
        startActivity(intent);
        GameSummary gs = db.gameSummaryDao().getGameSummary(db.gameHistoryDao().getMaxSessionId("CARD RECALL"), 1);
        Log.d("DEBUG", "In cancel.  Game summary = " + gs);

    }

    public void recallDeck(View view) {

        Intent intent = new Intent(this, RecallDeck.class);
        intent.putExtra(LEARNED_DECK, deck);
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
