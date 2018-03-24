package com.example.marc.rememberme.feature;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 3/20/2018.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.SimpleViewHolder>{

    private Context context;
    private final List<Card> items;
    private final Deck recallDeck;
    private Deck workingRecallDeck;
    private int recallPosition = 0;
    public ViewPager recallPager;
    public ImagePagerAdapter adapter;


    public GridAdapter(Context context, List<Card> items, Deck recallDeck, ViewPager pager) {

        this.context = context;
        this.items = items;
        this.recallDeck = recallDeck;
        this.recallPager = pager;
        workingRecallDeck = new Deck();
        initializePagerView(workingRecallDeck);

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
        final View view = LayoutInflater.from(this.context).inflate(R.layout.list_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        Card expandedListCard = getItem(position);
        holder.expandedListImageView.setImageResource(expandedListCard.getCardImageId());
        holder.expandedListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResult();
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

    private void initializePagerView(Deck deck) {

        adapter = new ImagePagerAdapter(deck, context);
        recallPager.setAdapter(adapter);

    }

    private void showResult() {

        if (recallPosition < recallDeck.getCards().size()) {

            workingRecallDeck.add(recallDeck.getCard(recallPosition));
            adapter.notifyDataSetChanged();
            recallPager.setCurrentItem(recallPosition);
            recallPosition++;

        }

    }

}
