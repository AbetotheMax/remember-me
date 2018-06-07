package com.example.marc.rememberme.feature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import static com.example.marc.rememberme.feature.R.*;

/**
 * Created by Marc on 3/11/2018.
 */

public class MemorizeThings extends AppCompatActivity{

    private final int PICK_IMAGE_REQUEST = 1;
    private ViewPager viewPager;
    private Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.memorize);
    }

    public void mapCards(View view) {

        deck = new Deck(this);
        setContentView(layout.map_cards);
        initializePagerView(deck);

    }

    private void initializePagerView(Deck deck) {

        viewPager = findViewById(R.id.pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(deck, this);
        viewPager.setAdapter(adapter);

    }

    public void mapCardToObject(View view) {

        Card currentCard = deck.getCard(viewPager.getCurrentItem());
        setContentView(layout.create_card_mapping);
        TextView cardToBeMapped = (TextView) findViewById(id.cardToBeMapped);
        cardToBeMapped.setText(currentCard.getCardNumberAsString() + " of " + currentCard.getSuitAsString());

    }

    public void selectPhoto(View view) {

        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView imageView = (ImageView) findViewById(R.id.selectedPhoto);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelMapping(View view) {

        setContentView(layout.map_cards);

    }

    public void learnNewDeck(View view) {

        Intent intent = new Intent(this, LearnNewDeck.class);
        startActivity(intent);

    }
}


