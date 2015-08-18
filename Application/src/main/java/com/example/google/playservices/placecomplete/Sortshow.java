package com.example.google.playservices.placecomplete;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import Adapterclass.CustomsortListAdapter;
import BeanClass.GPSTracker;
import BeanClass.Placedata;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Sortshow extends ActionBarActivity {

    GPSTracker gps;
    private double latitude = 0;
    private double longitude = 0;
    TextView mapsh;
    ListView placeshowlist;
    String Operationname;
    ArrayList<Placedata> results = new ArrayList<Placedata>();
    ArrayList<String> placearr = new ArrayList<String>();
    ArrayList<String> distarr = new ArrayList<String>();
    ArrayList<String> latiarr = new ArrayList<String>();
    ArrayList<String> longiarr = new ArrayList<String>();
    ArrayList<String> currentlocation= new ArrayList<String>();
    ArrayList<String> sorplacearr = new ArrayList<String>();
    ArrayList<Double> sordistarr = new ArrayList<Double>();
    ArrayList<Double> sorlatiarr= new ArrayList<Double>();
    ArrayList<Double> sorlongiarr= new ArrayList<Double>();
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortshow);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar,null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mapsh= (TextView) findViewById(R.id.buttonmapshow);

        Bundle b = getIntent().getExtras();
        gps = new GPSTracker(Sortshow.this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        ArrayList<String> placearr = (ArrayList<String>) getIntent().getSerializableExtra("placearr");
        ArrayList<Double> distarr = (ArrayList<Double>) getIntent().getSerializableExtra("distarr");
        ArrayList<Double> latiarr = (ArrayList<Double>) getIntent().getSerializableExtra("latiarr");
        ArrayList<Double> longiarr= (ArrayList<Double>) getIntent().getSerializableExtra("longiarr");
        final ArrayList<Double> currentlocation= (ArrayList<Double>) getIntent().getSerializableExtra("currentlocation");
        placeshowlist= (ListView) findViewById(R.id.listViewsortplace);
/*
        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(Sortshow.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId("ca-app-pub-8601037025948902/2292455274");

        //Locate the Banner Ad in activity_main.xml
        AdView adView = (AdView) this.findViewById(R.id.adView);

        // Request for Ads
        AdRequest adRequest = new AdRequest.Builder()

                // Add a test device to show Test Ads
                *//*.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("abc")*//*
                .build();

        // Load ads into Banner Ads
        adView.loadAd(adRequest);

        // Load ads into Interstitial Ads
        interstitial.loadAd(adRequest);

        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });*/
  try {
    Placedata[] p = new Placedata[placearr.size()];

    for (int i = 0; i < placearr.size(); i++) {
        p[i] = new Placedata();
        System.out.println("placeeee" + placearr.get(i));
        p[i].place = placearr.get(i);
//System.out.println("lati#####"+latiarr.get(i));
        Location newLocation = new Location("New Location");
        newLocation.setLatitude(latiarr.get(i));
        newLocation.setLongitude(longiarr.get(i));

        Location oldLocation = new Location("old");
        oldLocation.setLatitude(currentlocation.get(0));
        oldLocation.setLongitude(currentlocation.get(1));
        DecimalFormat df = new DecimalFormat("#.##");

        double dist = Double.valueOf(oldLocation.distanceTo(newLocation) / 1000);
        // p[i].distance=distarr.get(i);
        p[i].distance = dist;

    }



    for (int i = 0; i < latiarr.size(); i++) {
        p[i].latitude = latiarr.get(i);

    }


    for (int i = 0; i < longiarr.size(); i++) {


        p[i].longitude = longiarr.get(i);

    }

    if (placearr.size() > 1) {
        for (int i = 0; i < p.length; i++) {

            for (int j = i + 1; j < p.length; j++) {
                System.out.println(p[i].distance);
                double dist1 = p[i].distance;
                double dist2 = p[j].distance;
                if (dist1 > dist2) {
                    Placedata ptmp = new Placedata();
                    ptmp = p[i];
                    p[i] = p[j];
                    p[j] = ptmp;
                }

            }
        }
    }

    for (int i = 0; i < p.length; i++) {
        sorplacearr.add(p[i].place);
        sordistarr.add(p[i].distance);
        sorlatiarr.add(p[i].latitude);
        sorlongiarr.add(p[i].longitude);
        results.add(p[i]);
        placeshowlist.setAdapter(new CustomsortListAdapter(Sortshow.this, results));
    }
}
catch (Exception ex)
{


}
mapsh.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intentcount=new Intent(getApplicationContext(),showmap.class);
        intentcount.putExtra("latiarr",sorlatiarr);
        intentcount.putExtra("longiarr",sorlongiarr);
        intentcount.putExtra("placearr",sorplacearr);
        intentcount.putExtra("distarr",sordistarr);
        intentcount.putExtra("currentlocation",currentlocation);
        startActivity(intentcount);

    }
});

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

        }
        return super.onKeyDown(keyCode, event);
    }



    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
