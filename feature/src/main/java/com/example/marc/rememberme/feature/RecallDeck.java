package com.example.marc.rememberme.feature;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        expandableListAdapter = new CardSuitsExpandableListAdapter(this, expandableListTitle, expandableListDetail, deckToRecall, pager);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                Log.d("STATE","expanding group");

            }

        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                return false;

            }
        });

    }

}
