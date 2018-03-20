package com.example.marc.rememberme.feature;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Marc on 3/15/2018.
 */

public class CardSuitsExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Suit> expandableListTitle;
    private HashMap<Suit, List<Card>> expandableListDetail;

    public CardSuitsExpandableListAdapter(Context context, List<Suit> expandableListTitle, HashMap<Suit, List<Card>> expandableListDetail) {

        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;

    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {

        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);

    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {

        return expandedListPosition;

    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Card expandedListCard = (Card) getChild(listPosition, expandedListPosition);

        if(convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            convertView = layoutInflater.inflate(R.layout.list_item, null);

        }

        ImageView expandedListImageView = (ImageView) convertView.findViewById(R.id.expandedListItem);
        expandedListImageView.setImageResource(expandedListCard.getCardImageId());
        return convertView;

    }

    @Override
    public int getChildrenCount(int listPosition) {

        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();

    }

    @Override
    public Object getGroup(int listPosition) {

        return this.expandableListTitle.get(listPosition);

    }

    @Override
    public int getGroupCount() {

       Log.d("STATE", "in getgroupcount");
        return this.expandableListTitle.size();

    }

    @Override
    public long getGroupId(int listPosition) {

        return listPosition;

    }

    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Suit listSuit = (Suit) getGroup(listPosition);

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            convertView = layoutInflater.inflate(R.layout.list_group, null);

        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listSuit.name());

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
