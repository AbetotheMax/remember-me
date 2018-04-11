package com.example.marc.rememberme.feature;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;

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

    private DeckRecallResults(Deck recallDeck, Context context, ViewPager pager, View view) {

            this.recallDeck = recallDeck;
            this.numCards = recallDeck.getCards().size();
            this.recallView = view;
            this.context = context;
            this.recallPager = pager;
            this.recallManager = CardRecallPersistenceManager.getInstance(context);
            workingRecallDeck = new Deck();
            initializePagerView(workingRecallDeck);

    }

    public static DeckRecallResults getInstance(Deck recallDeck, Context context, ViewPager pager, View view) {

        if (instance == null || !instance.recallDeck.getCards().equals(recallDeck.getCards())) {

            instance = new DeckRecallResults(recallDeck, context, pager, view);

        }

        return instance;

    }


    private void initializePagerView(Deck deck) {

        adapter = new ImagePagerAdapter(deck, context);
        recallPager.setAdapter(adapter);

    }

    public boolean isSelectionCorrect(Card selectedCard, long duration) {

        boolean cardsMatch = true;

        if (recallPosition < recallDeck.getCards().size()) {

            if(!selectedCard.equals(recallDeck.getCard(recallPosition))) {

                cardsMatch = false;
                recallManager.saveNewError(recallDeck, recallPosition, selectedCard.getCardNumberAsString(), selectedCard.getSuitAsString(), duration);

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
        textView.setText(Integer.toString(errorCount));

    }

    public int updateProgress() {

        String currentPositionText = recallPosition + " / " + numCards;
        TextView textView = (TextView) recallView.findViewById(R.id.currentPositionText);
        textView.setText(currentPositionText);
        return recallPosition;

    }

    public int getRecallPosition() {

        return this.recallPosition;

    }

    public boolean atEndOfRecallDeck() {

        return recallPosition >= recallDeck.getCards().size();

    }

}
