package com.example.marc.rememberme.feature;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Marc on 3/20/2018.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.SimpleViewHolder>{

    private Context context;
    private final List<Card> items;

    public GridAdapter(Context context, List<Card> items) {

        this.context = context;
        this.items = items;

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
