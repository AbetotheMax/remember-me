package com.example.marc.rememberme.feature;

import android.content.Context;
import android.support.v4.view.ViewPager;

/**
 * Created by Marc on 3/26/2018.
 */

public class DeckRecallResults {

    private final Deck recallDeck;
    private Deck workingRecallDeck;
    private int recallPosition = 0;
    private Context context;
    public ViewPager recallPager;
    public ImagePagerAdapter adapter;
    private static DeckRecallResults instance;

    private DeckRecallResults(Deck recallDeck, Context context, ViewPager pager) {

            this.recallDeck = recallDeck;
            this.context = context;
            this.recallPager = pager;
            workingRecallDeck = new Deck();
            initializePagerView(workingRecallDeck);

    }

    public static DeckRecallResults getInstance(Deck recallDeck, Context context, ViewPager pager) {

        if (instance == null) {

            instance = new DeckRecallResults(recallDeck, context, pager);

        }

        return instance;

    }


    private void initializePagerView(Deck deck) {

        adapter = new ImagePagerAdapter(deck, context);
        recallPager.setAdapter(adapter);

    }

    public void showResult() {

        if (recallPosition < recallDeck.getCards().size()) {

            workingRecallDeck.add(recallDeck.getCard(recallPosition));
            adapter.notifyDataSetChanged();
            recallPager.setCurrentItem(recallPosition);
            recallPosition++;

        }

    }

}
