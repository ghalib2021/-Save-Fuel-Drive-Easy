package com.example.google.playservices.placecomplete;

import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import Adapterclass.CustomdirectionstepListAdapter;

import ParserClass.DirectionsJSONParser;
import BeanClass.stepdata;

import java.util.ArrayList;


public class DirectionStep extends ActionBarActivity
       {

    ListView stepsh;
    String mylocation,destination;
    DirectionsJSONParser parseobj;
    ArrayList<String> steparr=new ArrayList<String>();
    ArrayList<String> steparr2=new ArrayList<String>();
    ArrayList<stepdata> results = new ArrayList<stepdata>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar,null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        setContentView(R.layout.activity_direction_step);

          //listview resource
        stepsh= (ListView) findViewById(R.id.listViewstep);
        //taking data from intent
        final ArrayList<String> steparr= (ArrayList<String>) getIntent().getSerializableExtra("steparr");
        Bundle p = getIntent().getExtras();
        mylocation = p.getString("mylocation");
        destination= p.getString("destination");
        //adding source to arraylist
        steparr2.add("My Location "+mylocation);
        //adding place to arraylist
        for(int i=0;i<steparr.size();i++)
        {
            steparr2.add(steparr.get(i));
        }

        for(int i=0;i<steparr2.size();i++)
        {
            stepdata s=new stepdata();
            s.setStep(steparr2.get(i));
            results.add(s);
            stepsh.setAdapter(new CustomdirectionstepListAdapter(DirectionStep.this,results));
        }
//adding data to listview
        stepdata s=new stepdata();
        s.setStep("Destination "+destination);
        results.add(s);
        stepsh.setAdapter(new CustomdirectionstepListAdapter(DirectionStep.this,results));


    }









    public void onLocationChanged(Location location) {

    }


    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }


    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }







}
