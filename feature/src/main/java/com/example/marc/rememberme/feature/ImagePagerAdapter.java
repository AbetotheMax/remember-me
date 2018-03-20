package com.example.marc.rememberme.feature;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.widget.ImageView;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Marc on 3/11/2018.
 */

public class ImagePagerAdapter extends PagerAdapter {

    private Deck deck;
    private Context context;

    public ImagePagerAdapter(Deck deck, Context context) {

        super();
        this.deck = deck;
        this.context = context;

    }
    @Override
    public int getCount() {

        return deck.getCards().size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView;
        imageView = new ImageView(context);
        container.addView(imageView, 0);
        imageView.setImageResource(deck.getCard(position).getCardImageId());
        return imageView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((ImageView) object);

    }

}

