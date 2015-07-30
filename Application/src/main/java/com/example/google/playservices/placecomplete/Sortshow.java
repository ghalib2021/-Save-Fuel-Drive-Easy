package com.example.google.playservices.placecomplete;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.google.playservices.placecomplete.module.CustomListAdapter;
import com.example.google.playservices.placecomplete.module.CustomsortListAdapter;
import com.example.google.playservices.placecomplete.module.GPSTracker;
import com.example.google.playservices.placecomplete.module.Placedata;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Sortshow extends ActionBarActivity {
    TableLayout tab;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortshow);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        /*mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);*/
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar,null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mapsh= (TextView) findViewById(R.id.buttonmapshow);
        tab= (TableLayout) findViewById(R.id.table1);
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
try {
    Placedata[] p = new Placedata[placearr.size()];

    for (int i = 0; i < placearr.size(); i++) {
        p[i] = new Placedata();
        System.out.println("placeeee" + placearr.get(i));
        p[i].place = placearr.get(i);

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

    for (int i = 0; i < distarr.size(); i++) {
            /*Location newLocation = new Location("New Location");
            newLocation.setLatitude(latiarr.get(i));
            newLocation.setLongitude(longiarr.get(i));

            Location oldLocation = new Location("old");
            oldLocation.setLatitude(currentlocation.get(0));
            oldLocation.setLongitude(currentlocation.get(1));
            DecimalFormat df = new DecimalFormat("#.##");

           double dist= Double.valueOf(oldLocation.distanceTo(newLocation)/1000);
           // p[i].distance=distarr.get(i);
            p[i].distance=dist;*/

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





}
