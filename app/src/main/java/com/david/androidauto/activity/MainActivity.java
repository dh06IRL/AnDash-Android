package com.david.androidauto.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.androidauto.R;
import com.david.androidauto.adapter.AppsInstalledAdapter;
import com.david.androidauto.cards.GasCard;
import com.david.androidauto.cards.NearbyCard;
import com.david.androidauto.cards.TrafficCard;
import com.david.androidauto.cards.WeatherCard;
import com.david.androidauto.models.places.Places;
import com.david.androidauto.models.weather.Current;
import com.david.androidauto.utils.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import service.LocationService;


public class MainActivity extends BaseActivity implements LocationListener {

    Context mContext;
    public String TAG = "dash";
    ActionBar actionBar;

    @InjectView(R.id.main_holder)
    LinearLayout mainHolder;
    @InjectView(R.id.app_holder)
    LinearLayout appHolder;

    @InjectView(R.id.elevation)
    TextView elevationText;
    @InjectView(R.id.heading)
    TextView headingText;
    @InjectView(R.id.speed)
    TextView speedText;
    @InjectView(R.id.app_list)
    GridView appList;
    @InjectView(R.id.btn_search)
    ImageView searchbtn;
    @InjectView(R.id.btn_nav)
    ImageView navBtn;
    @InjectView(R.id.btn_phone)
    ImageView phoneBtn;
    @InjectView(R.id.btn_apps)
    ImageView appsBtn;
    @InjectView(R.id.btn_audio)
    ImageView audioBtn;
    @InjectView(R.id.btn_extra)
    ImageView extraBtn;
    @InjectView(R.id.btn_settings)
    ImageView settingsBtn;
    @InjectView(R.id.card_list)
    ListView cardList;
    @InjectView(R.id.map)
    MapView mapView;
    GoogleMap mMap;

    LocationService locationService;

    CardArrayAdapter cardArrayAdapter;
    List<Card> cards;
    String zipCode;

    LocationRequest mLocationRequest;
//    LocationClient mLocationClient;

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    private GoogleApiClient mGoogleApiClient;

    InterstitialAd interstitial;
    AdRequest adRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mContext = this;
        actionBar = getSupportActionBar();
        actionBar.hide();

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.onCreate(savedInstanceState);
        setUpMapIfNeeded();

        locationService = new LocationService(mContext);

        cards = new ArrayList<Card>();
        cardArrayAdapter = new CardArrayAdapter(mContext, cards);
        cardList.setAdapter(cardArrayAdapter);

        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-5640708555066600/7283621296");
        adRequest = new AdRequest.Builder().build();

//        mLocationClient = new LocationClient(this, this, this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        PendingResult<Status> result = LocationServices.FusedLocationApi
                                .requestLocationUpdates(
                                        mGoogleApiClient,   // your connected GoogleApiClient
                                        mLocationRequest,   // a request to receive a new location
                                        MainActivity.this); // the listener which will receive updated locations

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })
                .build();


        int highlightColor = mContext.getResources().getColor(android.R.color.white);
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);
        searchbtn.setColorFilter(colorFilter);
        settingsBtn.setColorFilter(colorFilter);

        addCards();
        getAppsInstalled();

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.google.android.googlequicksearchbox",
                        "com.google.android.googlequicksearchbox.VoiceSearchActivity");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                    Log.d(TAG, "Google Voice Search is not found");
                }
            }
        });

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", locationService.getLatitude(), locationService.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:0123456789"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(mContext, "No phone apps installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        appsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHolder.setVisibility(View.GONE);
                appHolder.setVisibility(View.VISIBLE);
            }
        });

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                startActivity(intent);
            }
        });

        extraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHolder.setVisibility(View.VISIBLE);
                appHolder.setVisibility(View.GONE);

                interstitial.loadAd(adRequest);
                interstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        if(interstitial.isLoaded()) {
                            interstitial.show();
                        }
                    }
                });
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cards.clear();
                addCards();
//                Intent intent = new Intent(mContext, SettingsActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void getAppsInstalled() {
        final PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        Collections.sort(list, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo s1, ApplicationInfo s2) {
                return s1.loadLabel(packageManager).toString().compareTo(s2.loadLabel(packageManager).toString());
            }
        });
        AppsInstalledAdapter appsInstalledAdapter = new AppsInstalledAdapter(mContext, list, packageManager);
        appList.setAdapter(appsInstalledAdapter);
    }

    private void setUpMap() {
        mMap.setIndoorEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(false);
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(HOME));
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((MapView) findViewById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    public void addCards() {
        CityAsyncTask cityAsyncTask = new CityAsyncTask(this, locationService.getLatitude(), locationService.getLongitude());
        cityAsyncTask.execute();
        addWeatherCard();
        addTrafficCard();
        addFourSquareCard();
//        addGasCard();
    }

    public void addWeatherCard() {
        Ion.with(mContext)
                .load("http://api.openweathermap.org/data/2.5/weather?lat="
                        + locationService.getLatitude() + "&lon=" + locationService.getLongitude() + "&units=imperial")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Log.e("weather", e.toString());
                        } else {
                            try {
                                Current current = new Gson().fromJson(result, Current.class);
                                WeatherCard weatherCard = new WeatherCard(mContext, current);

                                cards.add(weatherCard);
                                cardArrayAdapter.notifyDataSetChanged();
                            } catch (Exception e1) {
                                Log.e("weather", e1.toString());
                            }
                        }

                    }
                });
    }

    public void addTrafficCard() {
        String locs = locationService.getLatitude() + "," + locationService.getLongitude() + "," + (locationService.getLatitude() + 1.1) + "," + (locationService.getLongitude() + 1.1);
        String trafficRequest = "http://dev.virtualearth.net/REST/v1/Traffic/Incidents/" + locs + "?key=" + Constants.BING_KEY;

        Log.d("auto", trafficRequest);

        Ion.with(mContext)
                .load(trafficRequest)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> stringResponse) {
                        if (e != null) {
                            Log.e("auto", e.toString());
                        } else {
                            Log.d("auto", stringResponse.getResult().toString());
                            try {
                                Reader stringReader = new StringReader(stringResponse.getResult());

                                JsonReader reader = new JsonReader(stringReader);
                                reader.setLenient(true);

                                JsonParser parser = new JsonParser();
                                JsonObject result = parser.parse(stringResponse.getResult()).getAsJsonObject();

                                JsonArray set = result.getAsJsonArray("resourceSets");
                                JsonObject setObject = set.get(0).getAsJsonObject();
                                String accidentCount = setObject.getAsJsonPrimitive("estimatedTotal").toString();

                                TrafficCard trafficCard = new TrafficCard(mContext, accidentCount, Double.toString(locationService.getLatitude()), Double.toString(locationService.getLongitude()));
//                                trafficCard.setBackgroundResource(new ColorDrawable(android.R.color.transparent));
//                                trafficCard.setShadow(false);
                                cards.add(trafficCard);
                                cardArrayAdapter.notifyDataSetChanged();

                            } catch (Exception e1) {
                                Log.e("auto", e1.toString());
                                TrafficCard trafficCard = new TrafficCard(mContext, "Unavailable", Double.toString(locationService.getLatitude()), Double.toString(locationService.getLongitude()));
//                                trafficCard.setBackgroundResource(new ColorDrawable(android.R.color.transparent));
//                                trafficCard.setShadow(false);
                                cards.add(trafficCard);
                                cardArrayAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    public void addFourSquareCard() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyyMMdd");
        String now = parser.format(new Date());

        String testUrl = "https://api.foursquare.com/v2/venues/search?ll="
                + locationService.getLatitude() + "," + locationService.getLongitude() + "&client_id="
                + Constants.FOUR_CLIENT_ID + "&client_secret="
                + Constants.FOUR_CLIENT_SECRET
                + "&v=" + now;

        Log.d("auto", testUrl);

        Ion.with(mContext)
                .load(testUrl)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> stringResponse) {
                        if (e != null) {
                            Log.e("auto", e.toString());
                        } else {
                            Log.d("auto", stringResponse.getResult().toString());
                            try {
                                Places places = new Gson().fromJson(stringResponse.getResult(), Places.class);
                                NearbyCard placesCard = new NearbyCard(mContext, places);
//                                placesCard.setBackgroundResource(new ColorDrawable(android.R.color.transparent));
//                                placesCard.setShadow(false);
                                cards.add(placesCard);
                            } catch (IllegalArgumentException e1) {
                                Log.e("auto", e1.toString());
                            }
                            cardArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void addGasCard() {
        String url = Constants.GAS_API_BASE + zipCode;
        Log.d("auto", url);
        Ion.with(mContext)
                .load(url)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> stringResponse) {
                        if (e != null) {
                            Log.e("auto", e.toString());
                        } else {
                            Log.d("auto", stringResponse.getResult());
                            GasCard gasCard = new GasCard(mContext, stringResponse.getResult());
//                            gasCard.setShadow(false);
//                            gasCard.setBackgroundResource(new ColorDrawable(android.R.color.transparent));
                            cards.add(gasCard);
                            cardArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        locationService.stopSelf();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        elevationText.setText(Math.round(location.getAltitude()) + "");
        headingText.setText(getWind(Math.round(location.getBearing())));
        //Move the camera to the user's location once it's available!
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
        if (location.getSpeed() != 0.0) {
            speedText.setText(Math.round(location.getSpeed() * 2.23694) + "");
        } else {
            speedText.setText(Math.round(location.getSpeed()) + "");
        }
    }


    public class CityAsyncTask extends AsyncTask<String, String, String> {
        Activity act;
        double latitude;
        double longitude;

        public CityAsyncTask(Activity act, double latitude, double longitude) {
            this.act = act;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            Geocoder geocoder = new Geocoder(act, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude,
                        longitude, 10);
                Log.d("weather", "result size: " + addresses.size());
                result = addresses.get(0).getLocality();
                if (addresses.size() > 0) {
                    zipCode = addresses.get(0).getPostalCode();
                    int count = 0;
                    while (zipCode == null && count < addresses.size()) {
                        zipCode = addresses.get(count).getPostalCode();
                        count++;
                    }
                    Log.d("weather", "Zipcode: " + zipCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            TODO ADD BACK
            addGasCard();
            try {
                Log.d("weather", result);
            } catch (Exception e) {
                Log.e("weather", e.toString());
            }
        }
    }

    public String getWind(double degree) {
        String direction;
        if (degree >= 348.75) {
            direction = "N";
        } else if (degree <= 11.25) {
            direction = "N";
        } else if (degree > 11.25 && degree <= 33.75) {
            direction = " NNE";
        } else if (degree > 33.75 && degree <= 56.25) {
            direction = "NE";
        } else if (degree > 56.25 && degree <= 78.75) {
            direction = "ENE";
        } else if (degree > 78.75 && degree <= 101.25) {
            direction = "E";
        } else if (degree > 101.25 && degree <= 123.75) {
            direction = "ESE";
        } else if (degree > 123.75 && degree <= 146.25) {
            direction = "SE";
        } else if (degree > 146.25 && degree <= 168.75) {
            direction = "SSE";
        } else if (degree > 168.75 && degree <= 191.25) {
            direction = "S";
        } else if (degree > 191.25 && degree <= 213.75) {
            direction = "SSW";
        } else if (degree > 213.75 && degree <= 236.25) {
            direction = "SW";
        } else if (degree > 236.25 && degree <= 258.75) {
            direction = "WSW";
        } else if (degree > 258.75 && degree <= 281.25) {
            direction = "W";
        } else if (degree > 281.25 && degree <= 303.75) {
            direction = "WNW";
        } else if (degree > 303.75 && degree <= 326.25) {
            direction = "NW";
        } else if (degree > 326.25 && degree <= 348.75) {
            direction = "NNW";
        } else {
            direction = "U/A";
        }
        return direction;
    }
}
