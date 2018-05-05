package com.example.marc.rememberme.feature;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.marc.rememberme.feature.Persistence.GameHistory;

import java.util.List;
import java.util.Map;

/**
 * Created by Marc on 5/3/2018.
 */

public class PlayByPlayExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private String expandableListTitle;
    private List<Map<Card, Card>> expandableListDetail;
    private int callCount = 0;

    public PlayByPlayExpandableListAdapter(Context context, String expandableListTitle, List<Map<Card, Card>> expandableListDetail) {

        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;

    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {

        return this.expandableListDetail.get(expandedListPosition);

    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {

        return expandedListPosition;

    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ++callCount;
        if(convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            convertView = layoutInflater.inflate(R.layout.recall_details_play_by_play, null);

        }

        TextView positionText = (TextView) convertView.findViewById(R.id.positionText);
        positionText.setText("Card " + (expandedListPosition + 1) + " / " + expandableListDetail.size());

        TextView cardChosenText = (TextView) convertView.findViewById(R.id.cardChosen);
        TextView actualCardText = (TextView) convertView.findViewById(R.id.actualCard);

        Map<Card, Card> playByPlayEntry = (Map<Card, Card>) getChild(listPosition, expandedListPosition);

        for(Map.Entry<Card, Card> entry : playByPlayEntry.entrySet()) {

            cardChosenText.setText("Card chosen: " + entry.getValue().getCardNumber() + " of " + entry.getValue().getSuit());
            actualCardText.setText("Actual card: " + entry.getKey().getCardNumber() + " of " + entry.getKey().getSuit());

        }


        return convertView;

    }

    @Override
    public int getChildrenCount(int listPosition) {

        return expandableListDetail.size();

    }

    @Override
    public Object getGroup(int listPosition) {

        return this.expandableListTitle;

    }

    @Override
    public int getGroupCount() {

        Log.d("STATE", "in getgroupcount");
        return 1;

    }

    @Override
    public long getGroupId(int listPosition) {

        return listPosition;

    }

    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            convertView = layoutInflater.inflate(R.layout.list_group, null);

        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText("Play by Play");

        return convertView;

    }

    @Override
    public boolean hasStableIds() {

        return false;

    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {

        return true;

    }

}
