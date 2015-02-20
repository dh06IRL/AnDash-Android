package com.david.androidauto.cards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.androidauto.R;
import com.david.androidauto.models.places.Places;
import com.google.gson.Gson;

import java.util.Random;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by davidhodge on 11/23/14.
 */
public class NearbyCard extends Card {

    TextView placeName;
    TextView placeType;
    String result;

    TextView placeOpen;
    TextView placeMore;
    Places places;

    public NearbyCard(Context context, Places places) {
        this(context, R.layout.card_places);
        mContext = context;
        this.places = places;
    }

    public NearbyCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        placeName = (TextView) parent.findViewById(R.id.place_name);
        placeType = (TextView) parent.findViewById(R.id.place_type);
        placeOpen = (TextView) parent.findViewById(R.id.place_open);
        placeMore = (TextView) parent.findViewById(R.id.place_more);

        if(places.getResponse().getVenues().size() > 0) {
            int min = 0;
            int max = places.getResponse().getVenues().size();
            Random r = new Random();
            final int i1 = r.nextInt(max - min) + min;

            placeName.setText(places.getResponse().getVenues().get(i1).getName());
            try {
                placeType.setText(places.getResponse().getVenues().get(i1).getCategories().get(0).getName());
            } catch (Exception e) {
                Log.e("auto", e.toString());
                placeType.setText("Type unavailable");
            }

            placeOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://foursquare.com/v/" + places.getResponse().getVenues().get(i1).getId();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            });
        }
    }
}
