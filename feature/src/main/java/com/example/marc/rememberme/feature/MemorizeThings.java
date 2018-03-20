package com.example.marc.rememberme.feature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static com.example.marc.rememberme.feature.R.*;

/**
 * Created by Marc on 3/11/2018.
 */

public class MemorizeThings extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.memorize);
    }

    public void mapCards(View view) {

        Deck deck;
        deck = new Deck(this);
        setContentView(layout.map_cards);
        initializePagerView(deck);

    }

    private void initializePagerView(Deck deck) {

        ViewPager  viewPager = findViewById(R.id.pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(deck, this);
        viewPager.setAdapter(adapter);

    }

    public void learnNewDeck(View view) {

        Intent intent = new Intent(this, LearnNewDeck.class);
        startActivity(intent);

    }
}


