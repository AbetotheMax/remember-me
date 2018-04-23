package com.example.marc.rememberme.feature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marc.rememberme.feature.Persistence.GameHistoryOverview;

import java.util.List;
import java.util.Map;

/**
 * Created by Marc on 4/19/2018.
 */

public class PreviousGamesListViewAdapter extends ArrayAdapter<GameHistoryOverview> {

    private final Context context;
    private final List<GameHistoryOverview> previousGames;

    public PreviousGamesListViewAdapter(Context context, List<GameHistoryOverview> previousGames) {
        super(context, -1, previousGames);
        this.context = context;
        this.previousGames = previousGames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.previous_game_list_item, parent, false);
        TextView gameDate = (TextView) rowView.findViewById(R.id.gameDate);
        TextView gameState = (TextView) rowView.findViewById(R.id.gameState);
        TextView errorCount = (TextView) rowView.findViewById(R.id.errorCount);
        TextView accuracy = (TextView) rowView.findViewById(R.id.accuracy);
        ProgressBar progressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
        Button playButton = (Button) rowView.findViewById(R.id.playButton);

        gameDate.setText(previousGames.get(position).getGameDate());
        String state = previousGames.get(position).getGameState();
        gameState.setText("Game State: " + state.toLowerCase());
        int progress = previousGames.get(position).getProgress();

        if("RECALL".equals(state)) {
            String errorCountText = Integer.toString(previousGames.get(position).getErrorCount());
            errorCount.setText("Errors: " + errorCountText );
            accuracy.setText("Accuracy: " + previousGames.get(position).getAccuracy() + "%");

            if(progress == 100) {

                playButton.setText("Replay");

            }else {

                playButton.setText("Resume");

            }

        } else {

            errorCount.setVisibility(View.INVISIBLE);
            accuracy.setVisibility(View.INVISIBLE);
            playButton.setText("Resume");

        }

        progressBar.setProgress(progress);

        return rowView;

    }

}
