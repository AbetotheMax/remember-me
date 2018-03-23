package com.example.marc.rememberme.feature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Marc on 3/11/2018.
 */

public class LearnNewDeck extends AppCompatActivity {

    private Deck deck;
    private ViewPager viewPager;
    private ImagePagerAdapter adapter;

    public static final String LEARNED_DECK = "com.example.marc.rememberme.feature.DECK";

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {

        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.learn_new_deck);
        deck = new Deck(this);
        deck.shuffle();
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

        String currentPosition = position + " / " + numCards;
        TextView textView = findViewById(R.id.currentPositionText);
        textView.setText(currentPosition);

    }

    public void restartDeck(View view) {

        viewPager.setCurrentItem(0);

    }

    public void cancel(View view) {

        Intent intent = new Intent(this, MemorizeThings.class);
        startActivity(intent);

    }

    public void recallDeck(View view) {

        Intent intent = new Intent(this, RecallDeck.class);
        intent.putExtra(LEARNED_DECK, deck);
        startActivity(intent);

    }

    private class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {

            int numCards = adapter.getCount();
            setCurrentPositionText(position + 1, numCards);

        }

    }


}
