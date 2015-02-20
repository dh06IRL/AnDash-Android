package com.david.androidauto.cards;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.androidauto.R;
import com.david.androidauto.models.gas.Main;
import com.google.gson.Gson;

import java.util.Random;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by davidhodge on 11/23/14.
 */
public class GasCard extends Card{
    String result;

    TextView gasAdd;
    TextView gasBrand;
    TextView gasReg;
    TextView gasPlus;
    TextView gasPrem;

    public GasCard(Context context, String result) {
        this(context, R.layout.card_gas);
        mContext = context;
        this.result = result;
    }

    public GasCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        gasAdd = (TextView) parent.findViewById(R.id.gas_add);
        gasBrand = (TextView) parent.findViewById(R.id.gas_name);
        gasReg = (TextView) parent.findViewById(R.id.gas_reg);
        gasPlus = (TextView) parent.findViewById(R.id.gas_plus);
        gasPrem = (TextView) parent.findViewById(R.id.gas_prem);

        Gson gson = new Gson();
        final Main places = gson.fromJson(result, Main.class);

        if(places.getItem().size() > 0) {
            int min = 0;
            int max = places.getItem().size();
            Random r = new Random();
            final int i1 = r.nextInt(max - min) + min;

            gasAdd.setText(places.getItem().get(i1).getAddress());
            gasBrand.setText(places.getItem().get(i1).getBrand());

            if(!TextUtils.equals(places.getItem().get(i1).getRegular(), "false")) {
                gasReg.setText("$" + places.getItem().get(i1).getRegular());
            }else{
                gasReg.setText("$ -- ");
            }

            if(!TextUtils.equals(places.getItem().get(i1).getPlus(), "false")) {
                gasPlus.setText("$" + places.getItem().get(i1).getPlus());
            }else{
                gasPlus.setText("$ -- ");
            }

            if(!TextUtils.equals(places.getItem().get(i1).getPremium(), "false")) {
                gasPrem.setText("$" + places.getItem().get(i1).getPremium());
            }else{
                gasPrem.setText("$ -- ");
            }
        }
    }
}
