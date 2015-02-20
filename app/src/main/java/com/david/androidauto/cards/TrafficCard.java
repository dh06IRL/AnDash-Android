package com.david.androidauto.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.androidauto.R;
import com.david.androidauto.utils.Constants;
import com.squareup.picasso.Picasso;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by davidhodge on 11/23/14.
 */
public class TrafficCard extends Card{

    TextView trafficCount;
    ImageView trafficImage;
    String count;
    String lat;
    String lon;

    public TrafficCard(Context context, String count, String lat, String lon) {
        this(context, R.layout.card_traffic);
        mContext = context;
        this.count = count;
        this.lat = lat;
        this.lon = lon;
    }

    public TrafficCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        trafficCount = (TextView) parent.findViewById(R.id.traffic_est);
        trafficImage = (ImageView) parent.findViewById(R.id.traffic_image);

        trafficCount.setText("Accidents Estimate: " + count);

        String trafficImageUrl = "http://dev.virtualearth.net/REST/V1/Imagery/Map/Road/" + lat + "," + lon + "/13?mapLayer=TrafficFlow&key=" + Constants.BING_KEY;
        Picasso.with(mContext)
                .load(trafficImageUrl)
                .into(trafficImage);

    }
}
