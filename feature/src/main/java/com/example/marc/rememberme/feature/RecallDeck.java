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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private ViewPager pager;
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recall_deck);

        Bundle bundle = getIntent().getExtras();
        deckToRecall = bundle.getParcelable(LEARNED_DECK);
        pager = (ViewPager) findViewById(R.id.recallDeckPager);


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = CardSuitsExpandableListDataPump.getData(this);
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CardSuitsExpandableListAdapter(this, expandableListTitle, expandableListDetail, deckToRecall, pager, findViewById(android.R.id.content));
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

    }

}
