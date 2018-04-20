package com.example.marc.rememberme.feature;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistoryOverview;

import java.util.List;

/**
 * Created by Marc on 4/20/2018.
 */

public class History extends AppCompatActivity {
    private CardRecallPersistenceManager recallManager;
    private PreviousGamesListViewAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {

        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.previous_games);
        initializePagerView();

    }

    private void initializePagerView() {

        GameHistoryDataPump dataPump = new GameHistoryDataPump(this);
        List<GameHistoryOverview> gameHistoryOverviews = dataPump.loadPriorGameOverviews();

        listView = findViewById(R.id.previousGamesListView);
        adapter = new PreviousGamesListViewAdapter(this, gameHistoryOverviews);
        listView.setAdapter(adapter);

    }

}
