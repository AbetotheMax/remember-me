package com.example.marc.rememberme.feature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Marc on 5/9/2018.
 */

public class PlayByPlayListViewAdapter extends ArrayAdapter<Map<Card, Card>> {

    private final Context context;
    private final List<Map<Card, Card>> playByPlayList;

    public PlayByPlayListViewAdapter(Context context, List<Map<Card, Card>> playByPlay) {
        super(context, -1, playByPlay);
        this.context = context;
        this.playByPlayList = playByPlay;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            convertView = layoutInflater.inflate(R.layout.recall_details_play_by_play, null);

        }

        TextView positionText = (TextView) convertView.findViewById(R.id.positionText);
        positionText.setText("Card " + (position + 1) + " / " + playByPlayList.size());

        TextView cardChosenText = (TextView) convertView.findViewById(R.id.cardChosen);
        TextView actualCardText = (TextView) convertView.findViewById(R.id.actualCard);

        Map<Card, Card> playByPlayEntry = (Map<Card, Card>) playByPlayList.get(position);

        for(Map.Entry<Card, Card> entry : playByPlayEntry.entrySet()) {

            cardChosenText.setText("Card chosen: " + entry.getValue().getCardNumber() + " of " + entry.getValue().getSuit());
            actualCardText.setText("Actual card: " + entry.getKey().getCardNumber() + " of " + entry.getKey().getSuit());

        }


        return convertView;


    }


}
