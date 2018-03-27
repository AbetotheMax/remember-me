package com.example.marc.rememberme.feature;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Marc on 3/26/2018.
 */

public class DeckRecallResults {

    private final Deck recallDeck;
    private Deck workingRecallDeck;
    private int recallPosition = 0;
    private int errorCount = 0;
    private int currentPosition = 0;
    private int numCards = 0;
    private Context context;
    public ViewPager recallPager;
    public ImagePagerAdapter adapter;
    private View recallView;
    private static DeckRecallResults instance;

    private DeckRecallResults(Deck recallDeck, Context context, ViewPager pager, View view) {

            this.recallDeck = recallDeck;
            this.numCards = recallDeck.getCards().size();
            this.recallView = view;
            this.context = context;
            this.recallPager = pager;
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

    public boolean isSelectionCorrect(Card selectedCard) {

        boolean cardsMatch = false;

        if (recallPosition < recallDeck.getCards().size()) {

            if(selectedCard.equals(recallDeck.getCard(recallPosition))) {

                cardsMatch = true;

            }

            workingRecallDeck.add(recallDeck.getCard(recallPosition));
            adapter.notifyDataSetChanged();
            recallPager.setCurrentItem(recallPosition);
            recallPosition++;

        }

        return cardsMatch;

    }

    public void updateErrors() {

        errorCount++;
        TextView textView = (TextView) recallView.findViewById(R.id.errorCountText);
        textView.setText(Integer.toString(errorCount));

    }

    public void updateProgress() {

        currentPosition++;
        String currentPositionText = currentPosition + " / " + numCards;
        TextView textView = (TextView) recallView.findViewById(R.id.currentPositionText);
        textView.setText(currentPositionText);

    }

}
