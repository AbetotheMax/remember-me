package com.example.marc.rememberme.feature;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.marc.rememberme.feature.Persistence.CardRecallErrors;
import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistory;
import com.example.marc.rememberme.feature.Persistence.GameSummary;

import java.util.Date;

/**
 * Created by Marc on 3/26/2018.
 */

public class DeckRecallResults {

    private final Deck recallDeck;
    private Deck workingRecallDeck;
    private int recallPosition = 0;
    private int errorCount = 0;
    private int numCards = 0;
    private Context context;
    public ViewPager recallPager;
    public ImagePagerAdapter adapter;
    private View recallView;
    private static DeckRecallResults instance;
    private CardRecallPersistenceManager recallManager;
    private GameHistory lastGameHistoryRecord;

    private DeckRecallResults(Deck recallDeck, Context context, ViewPager pager, View view, GameHistory lastGameHistoryRecord) {

            this.recallDeck = recallDeck;
            this.numCards = recallDeck.getCards().size();
            this.recallView = view;
            this.context = context;
            this.recallPager = pager;
            this.recallManager = CardRecallPersistenceManager.getInstance(context);
            this.lastGameHistoryRecord = lastGameHistoryRecord;
            workingRecallDeck = new Deck();
            initializePagerView(workingRecallDeck);

    }

    public static DeckRecallResults getInstance(Deck recallDeck, Context context, ViewPager pager, View view, GameHistory lastGameHistoryRecord) {

        if (instance == null || !instance.recallDeck.getCards().equals(recallDeck.getCards())) {

            instance = new DeckRecallResults(recallDeck, context, pager, view, lastGameHistoryRecord);

        }

        return instance;

    }


    private void initializePagerView(Deck deck) {

        adapter = new ImagePagerAdapter(deck, context);
        recallPager.setAdapter(adapter);

        if(lastGameHistoryRecord.getLastPosition() != 0) {

            recallPager.setCurrentItem(lastGameHistoryRecord.getLastPosition());
            updateProgress();
            errorCount = recallManager.getNumErrorsForAttempt(lastGameHistoryRecord.getSessionId(), lastGameHistoryRecord.getAttemptId()) - 1;
            updateErrorCount();

        }

    }

    public boolean isSelectionCorrect(Card selectedCard, long duration) {

        boolean cardsMatch = true;

        if (recallPosition < recallDeck.getCards().size()) {

            if(!selectedCard.equals(recallDeck.getCard(recallPosition))) {

                cardsMatch = false;
                lastGameHistoryRecord.setCumulativeStateDuration(lastGameHistoryRecord.getCumulativeStateDuration() + duration);
                lastGameHistoryRecord.setLastModDateTime(new Date());
                recallManager.saveNewError(lastGameHistoryRecord, recallPosition, selectedCard.getCardNumberAsString(), selectedCard.getSuitAsString());
                CardRecallErrors lastError = recallManager.loadLastErrorForAttempt(lastGameHistoryRecord.getSessionId(), lastGameHistoryRecord.getAttemptId());
                Log.d("DEBUG", "Added new error.  Error added = {recallPosition: " + recallPosition + ", selected card number: " +
                        selectedCard.getCardNumberAsString() + ", selected card suit: " + selectedCard.getSuitAsString());
                Log.d("DEBUG", "Error in database = " + lastError.toString());

            }

            workingRecallDeck.add(recallDeck.getCard(recallPosition));
            adapter.notifyDataSetChanged();
            recallPager.setCurrentItem(recallPosition);
            recallPosition++;

        }

        return cardsMatch;

    }

    public void updateErrorCount() {

        errorCount++;
        TextView textView = (TextView) recallView.findViewById(R.id.errorCountText);
        textView.setText("ERRORS: " + Integer.toString(errorCount));

    }

    public int updateProgress() {

        String currentPositionText = recallPosition + " / " + numCards;
        TextView textView = (TextView) recallView.findViewById(R.id.currentPositionText);
        textView.setText("CARD: " + currentPositionText);
        return recallPosition;

    }

    public int getRecallPosition() {

        return this.recallPosition;

    }

    public boolean atEndOfRecallDeck() {

        return recallPosition >= recallDeck.getCards().size();

    }

}
