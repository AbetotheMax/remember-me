package com.example.marc.rememberme.feature;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marc.rememberme.feature.Persistence.CardRecallPersistenceManager;
import com.example.marc.rememberme.feature.Persistence.GameHistory;
import com.example.marc.rememberme.feature.Persistence.GameSummary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Marc on 3/20/2018.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.SimpleViewHolder>{

    private Context context;
    private View recallView;
    private final List<Card> items;
    private DeckRecallResults results;
    private CardRecallPersistenceManager recallManager;
    private GameHistory lastGameHistoryRecord;


    public GridAdapter(Context context, List<Card> items, Deck recallDeck, ViewPager pager, View view, GameHistory lastGameHistoryRecord) {

        this.context = context;
        this.items = items;
        this.results = DeckRecallResults.getInstance(recallDeck, context, pager, view, lastGameHistoryRecord);
        this.recallView = view;
        this.recallManager = CardRecallPersistenceManager.getInstance(context);
        this.lastGameHistoryRecord = lastGameHistoryRecord;

    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final ImageView expandedListImageView ;

        public SimpleViewHolder(View view) {
            super(view);
            expandedListImageView = (ImageView) view.findViewById(R.id.expandedListItem);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        final Card expandedListCard = getItem(position);
        holder.expandedListImageView.setImageResource(expandedListCard.getCardImageId());
        holder.expandedListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Chronometer chronometer = recallView.findViewById(R.id.recallChronometer);

                if(!results.isSelectionCorrect(expandedListCard, chronometer.getBase())) {

                    results.updateErrorCount();

                }

                results.updateProgress();
                if(results.atEndOfRecallDeck()) {

                    chronometer.stop();
                    lastGameHistoryRecord.setGameState("RECALL");
                    lastGameHistoryRecord.setGameStateStatus("COMPLETED");
                    lastGameHistoryRecord.setLastPosition(results.getRecallPosition());
                    lastGameHistoryRecord.setCumulativeStateDuration(chronometer.getBase());
                    lastGameHistoryRecord.setLastModDateTime(new Date());
                    recallManager.updateGameState(lastGameHistoryRecord);
                    GameSummary gs = recallManager.getGameSummary(lastGameHistoryRecord.getSessionId(), lastGameHistoryRecord.getAttemptId());
                    Log.d("DEBUG", "Completed Recall.  GameSummary = " + gs.toString());

                }
            }
        });
    }
    @Override
    public int getItemCount() {

        return items.size();

    }

    public Card getItem(int position) {

        return items.get(position);

    }

    @Override
    public long getItemId(int position) {

        return position;

    }


}
