package com.david.androidauto.cards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.androidauto.R;
import com.david.androidauto.models.weather.Current;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by davidhodge on 11/23/14.
 */
public class WeatherCard extends Card {

    TextView weatherTemp;
    TextView weatherForecast;
    ImageView weatherIcon;

    Current current;

    public WeatherCard(Context context, Current current) {
        this(context, R.layout.card_weather);
        mContext = context;
        this.current = current;
    }

    public WeatherCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        weatherTemp = (TextView) parent.findViewById(R.id.weather_temp);
        weatherForecast = (TextView) parent.findViewById(R.id.weather_forecast);
        weatherIcon = (ImageView) parent.findViewById(R.id.weather_icon);


        weatherTemp.setText(Math.round(current.getMain().getTemp()) + "" + (char) 0x00B0);
        String forecast = current.getWeather().get(0).getDescription() +", with winds at " + current.getWind().getSpeed() + " mph. " + current.getMain().getHumidity() + "% humidity, pressure at " + current.getMain().getPressure() + " mb.";
        forecast = Character.toUpperCase(forecast.charAt(0)) + forecast.substring(1);
        weatherForecast.setText(forecast);

        this.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.david.ioweather");
                if (intent != null){
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }else{
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("market://details?id=" + "com.david.ioweather"));
                    mContext.startActivity(intent);
                }
            }
        });

        String cond = current.getWeather().get(0).getIcon();
        if(cond.contentEquals("01d")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_sunny)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("01n")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_sunny_n)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("02d")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_mostlysunny)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("02n")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_mostlysunny_n)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("03d")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_mostlycloudy)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("03n")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_mostlycloudy_n)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("04d")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_cloudy)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("04n")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_cloudy)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("09d")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_cloudyrain)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("09n")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_cloudyrain_n)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("10d")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_rain)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("10n")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_rain)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("11d")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_storm)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("11n")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_storm)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("13d")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_snow)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("13n")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_snow)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("50d")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_lightrain)
                    .noFade()
                    .into(weatherIcon);
        }else if(cond.contentEquals("50n")){
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_lightrain)
                    .noFade()
                    .into(weatherIcon);
        }else{
            Picasso.with(mContext)
                    .load(R.drawable.googlenowweatherv3_na)
                    .noFade()
                    .into(weatherIcon);
        }
    }
}