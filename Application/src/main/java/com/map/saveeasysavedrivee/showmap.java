package com.map.saveeasysavedrivee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Polyline;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import BeanClass.GPSTracker;
import ParserClass.DirectionsJSONParser;
import ParserClass.DirectionsJSONParser1;
import ParserClass.DirectionsJSONParser2;

public class showmap extends FragmentActivity implements LocationListener,TextToSpeech.OnInitListener{
//initialization
    GoogleMap mGoogleMap;
    GPSTracker gps;
    Marker now,mPositionMarker;
    ProgressDialog progress;
    private TextToSpeech tts;
    static boolean isRouteUpdating = false;
    static int arrraySize = 0;
int rootch=0;
    private static final String API_KEY_trans = "AIzaSyAHsmmOWPMyHN_5GqUySzN9jy7_0U8GhHE";
    private double  currentlatitude;
    private double currentlongitude;
    private LatLng latLng;
    static boolean isRouteModified = false;
    String voicebuttonstatus="off",addressString = null,ponitplace,sourceplace,mylocation,destination,currentadd;
    boolean statusshowdialog=false;
    float bearinginfloat;
    int placecount=0;
    static int check = 0;
    boolean dialogShown=false;
    static boolean isOriginalRouteChanged = false;
    int i=0;
    int k = 0;
    int colorroute=0,itemsel=1;
    private double latitude = 0;
    private double longitude = 0;
    //use for giving color to marker
    ArrayList<LatLng> mMarkerPoints;
    ArrayList<LatLng> points = null;
    ArrayList<LatLng> pointslatlong = new ArrayList<LatLng>();
    ArrayList<String> stepall = new ArrayList<String>();
    public ArrayList<String> steparr=new ArrayList<String>();
    ArrayList<Double> latiarr = new ArrayList<Double>();
    ArrayList<Double> longiarr = new ArrayList<Double>();
    ArrayList<String> placearr = new ArrayList<String>();
    ArrayList<Double> distarr= new ArrayList<Double>();
    ArrayList<Double> currentlocation= new ArrayList<Double>();
    ArrayList<String> voicestatus = new ArrayList<String>();
    ArrayList<String> placespinneritem=new ArrayList<String>();
    ImageView /*btnmap,btnsate,*/voiceimg;
    TextView totalplace,visitedplace,stepshowtxt;
   Spinner placeshowspinner;
    Spinner mapViewSpinner;
Spinner languagOptionSpinner;
    ArrayList<Polyline> removePolyLine;
    ArrayList<Marker> removeMarkers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_showmap);
       //feching data from intent
        final ArrayList<Double> latiarr = (ArrayList<Double>) getIntent().getSerializableExtra("latiarr");
        final ArrayList<Double> longiarr= (ArrayList<Double>) getIntent().getSerializableExtra("longiarr");
        final ArrayList<String> placearr= (ArrayList<String>) getIntent().getSerializableExtra("placearr");
        ArrayList<Double> distarr= (ArrayList<Double>) getIntent().getSerializableExtra("distarr");
        final  ArrayList<String> currentlocation= (ArrayList<String>) getIntent().getSerializableExtra("currentlocation");
        final String Placestatus[]=new String[latiarr.size()];
        //find id
        totalplace= (TextView) findViewById(R.id.textViewplace);
        visitedplace= (TextView) findViewById(R.id.textvisitedplace);
        voiceimg=(ImageView)findViewById(R.id.imageViewvoice);
       removeMarkers = new ArrayList<Marker>();
        removePolyLine = new ArrayList<Polyline>();
     //   btnmap= (ImageView) findViewById(R.id.imageButtonmap);
       // btnsate= (ImageView) findViewById(R.id.imageButtonsatellite);
        points= new ArrayList<LatLng>();

        stepshowtxt= (TextView) findViewById(R.id.textViewstep);
          placeshowspinner= (Spinner) findViewById(R.id.spinnerplaceshow);
        mapViewSpinner = (Spinner) findViewById(R.id.mapviews);
        languagOptionSpinner = (Spinner) findViewById(R.id.languageoption);
        gps = new GPSTracker(showmap.this);
        int sizeplace=placearr.size();
        totalplace.setText(Integer.toString(sizeplace));
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            // Initializing
            mMarkerPoints = new ArrayList<LatLng>();


            tts = new TextToSpeech(this, this);
           languagOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   if(languagOptionSpinner.getSelectedItem().equals("ENGLISH")) {
                       tts.setLanguage(Locale.ENGLISH);
                   } else if(languagOptionSpinner.getSelectedItem().equals("SOMALI")) {
                       int result = tts.setLanguage(Locale.GERMAN);
                   }   else  if(languagOptionSpinner.getSelectedItem().equals("Chinese")) {
                   int result = tts.setLanguage(Locale.CHINESE);
                   }else if(languagOptionSpinner.getSelectedItem().equals("French")) {
                   int result = tts.setLanguage(Locale.FRENCH);
                   }else if(languagOptionSpinner.getSelectedItem().equals("Japanese")) {
                  int result = tts.setLanguage(Locale.JAPANESE);

                   }else if(languagOptionSpinner.getSelectedItem().equals("Korean")) {
                  int result = tts.setLanguage(Locale.KOREAN);

                }else if(languagOptionSpinner.getSelectedItem().equals("Italian")) {
                   int result = tts.setLanguage(Locale.ITALIAN);
               } else if(languagOptionSpinner.getSelectedItem().equals("Taiwan")) {
                  int  result = tts.setLanguage(Locale.TAIWAN);
               }
               }

               @Override
               public void onNothingSelected(AdapterView<?> adapterView) {

               }
           });
            tts.setSpeechRate(1.0f);
            // Getting reference to SupportMapFragment of the activity_main

            mGoogleMap=((MapFragment) getFragmentManager().findFragmentById(
                    R.id.mapshowlay)).getMap();

            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

//zoom disable

            UiSettings uiSettings = mGoogleMap.getUiSettings();

            uiSettings.setZoomGesturesEnabled(false);
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location From GPS
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                onLocationChanged(location);
            }
            mGoogleMap.setBuildingsEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            //add place to spinner
            placespinneritem.add("Places");
           for(int a=0;a<placearr.size();a++)
           {
               placespinneritem.add(placearr.get(a));
           }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, placespinneritem);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            placeshowspinner.setAdapter(dataAdapter);

            placeshowspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   // Toast.makeText(getApplicationContext(),"place"+position+"id"+id,Toast.LENGTH_LONG).show();

                    if(placeshowspinner.getSelectedItem() != null) {
                        // Code you want to execute only on UI selects of the spinner

                        if (position == 0) {

                        } else {

                            mGoogleMap.clear();
                           // Toast.makeText(getApplicationContext(), "place select" + position, Toast.LENGTH_LONG).show();


                            colorroute = 1;
                            int placesel = position - 1;
                            LatLng routchangeloc = new LatLng(latLng.latitude, latLng.longitude);
                            LatLng locationtogo = new LatLng(latiarr.get(placesel), longiarr.get(placesel));
                            String url1 = getDirectionsUrl(routchangeloc, locationtogo);
//System.out.println("url.............."+url1);
                            DownloadTask downloadTask1 = new DownloadTask();

                            // Start downloading json data from Google Directions API
                            downloadTask1.execute(url1);

//new route step voice


                            // Start downloading json data from Google Directions API


                            DownloadTask3 voicetask = new DownloadTask3();
                            voicetask.execute(url1);


                        }
                        placeshowspinner.setSelection(0);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            for(int i=0;i<currentlocation.size();i++)
            {
                latitude = Double.parseDouble(currentlocation.get(0));
                longitude =Double.parseDouble( currentlocation.get(1));

            }
            final LatLng point = new LatLng(latitude, longitude);
            voiceimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voicebuttonstatus="On";
                    voiceimg.setImageResource(R.drawable.redv);
                    try {
                        for (int i = 0; i < latiarr.size(); i++) {


                            LatLng first = new LatLng(latiarr.get(0), longiarr.get(0));
                            String url1 = getDirectionsUrl(point, first);


                            DownloadTask3 voicetask = new DownloadTask3();
                            voicetask.execute(url1);
                        }
                        for (int i = 0; i < latiarr.size(); i++) {
                            Placestatus[i]="true";
                            if (latiarr.size() > i + 1) {
                                LatLng origin = new LatLng(latiarr.get(i), longiarr.get(i));
                                LatLng dest = new LatLng(latiarr.get(i + 1), longiarr.get(i + 1));

                                // Getting URL to the Google Directions API
                                String url = getDirectionsUrl(origin, dest);



                                // Start downloading json data from Google Directions API



                                DownloadTask3 voicetask = new DownloadTask3();
                                voicetask.execute(url);

                            }

                        }
                    }
                    catch (Exception es)
                    {
                        Toast.makeText(getApplicationContext(),"No internet Connection",Toast.LENGTH_LONG).show();
                    }
                }
            });



            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(point)
                    .bearing(45)
                    .tilt(90)
                    .zoom(16)
                    .build(); //Creates a CameraPosition from the builder*/
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    cameraPosition));

         /*   btnmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mGoogleMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    }
                }
            });
            btnsate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mGoogleMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL) {
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    }
                }
            });*/
            mapViewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(mapViewSpinner.getSelectedItem().equals("NORMAL")) {
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    } else if(mapViewSpinner.getSelectedItem().equals("HYBRID")) {
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    } else if(mapViewSpinner.getSelectedItem().equals("SATELLITE")) {
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    } else if(mapViewSpinner.getSelectedItem().equals("Terrain")) {
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    } else if(mapViewSpinner.getSelectedItem().equals("None")) {
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(point.latitude,point.longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {


                    sb.append( addresses.get(0).getAddressLine(0));
                    sb.append(addresses.get(0).getAddressLine(1));
                    sb.append(addresses.get(0).getAddressLine(2));
                }

                addressString = sb.toString();

                Log.e("Address from lat,long ;", addressString);
            } catch (IOException e) {}
          Marker m =  drawMarker(point,addressString );
          removeMarkers.add(m);
//for showing current location at action bar
            try {


                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {


                    sb.append( addresses.get(0).getAddressLine(0));
                    sb.append(addresses.get(0).getAddressLine(1));
                    sb.append(addresses.get(0).getAddressLine(2));
                }

               currentadd = sb.toString();
                stepshowtxt.setText(currentadd);
             //  voicestatus.add("true");
               //tts.speak("" + stepshowtxt, TextToSpeech.QUEUE_FLUSH, null);

            } catch (IOException e) {}
try {
    points.clear();
    arrraySize = latiarr.size();
    points.add(new LatLng(truncateDecimal(point.latitude,5).doubleValue(),truncateDecimal(point.longitude,5).doubleValue()));
    for (int i = 0; i < latiarr.size(); i++) {

// points contain the current location
        String addressplace = placearr.get(i);
        LatLng pointplace = new LatLng(latiarr.get(i), longiarr.get(i));
       Marker marker =  drawMarker(pointplace, addressplace);
        removeMarkers.add(marker);
        LatLng first = new LatLng(latiarr.get(0), longiarr.get(0));
       /// this drwas the line between two cities
        String url1 = getDirectionsUrl(point, first); // from here it draws line
//System.out.println("url.............."+url1);
       isRouteUpdating = true;
        DownloadTask downloadTask1 = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask1.execute(url1);
 DownloadTask3 voicetask = new DownloadTask3();
        voicetask.execute(url1);
    }
    for (int i = 0; i < latiarr.size(); i++) {
        Placestatus[i]="true";
        if (latiarr.size() > i + 1) {
            LatLng origin = new LatLng(latiarr.get(i), longiarr.get(i));
            LatLng dest = new LatLng(latiarr.get(i + 1), longiarr.get(i + 1));

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);
            isRouteUpdating = true;
            DownloadTask downloadTask = new DownloadTask();
            sourceplace = placearr.get(i);
            ponitplace = placearr.get(i + 1);
            // Start downloading json data from Google Directions API

            downloadTask.execute(url);

           DownloadTask3 voicetask = new DownloadTask3();
            voicetask.execute(url);

        }

    }

}
catch (Exception es)
{
    Toast.makeText(getApplicationContext(),"No internet Connection",Toast.LENGTH_LONG).show();
}


/// sets your location on the map
            mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {

                    if (now != null) {
                        now.remove();
                    }


                    latLng = new LatLng(truncateDecimal(location.getLatitude(),5).doubleValue(), truncateDecimal(location.getLongitude(),5).doubleValue());

                    now = mGoogleMap.addMarker(new MarkerOptions().position(latLng).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.navigationblack)).anchor(0.5f, 0.5f).infoWindowAnchor(0.5f, 0.5f));

                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                    gps = new GPSTracker(showmap.this);
                    currentlatitude = gps.getLatitude();
                    currentlongitude = gps.getLongitude();
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .bearing(bearinginfloat)
                            .tilt(45)
                            .zoom(16)
                            .build(); //Creates a CameraPosition from the builder*//**//*

                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            cameraPosition));

                   if((points != null) &&  !isRouteUpdating) {
                       boolean islocationexisted = false;
                       Location locationA = new Location("point A");
                       locationA.setLatitude(latLng.latitude);
                       locationA.setLongitude(latLng.longitude);
                       Location locationB = new Location("point B");
                       locationB.setLatitude(points.get(0).latitude);
                       locationB.setLongitude(points.get(0).longitude);
                       float distance = locationA.distanceTo(locationB);
                       if(distance > 30) {
                           for (int k = 0; k < points.size(); k++) {
                               if (latLng.equals(points.get(k))) {
                                   islocationexisted = true;
                                   break;
                               } else {
                                   islocationexisted = false;
                                   locationA.setLatitude(latLng.latitude);
                                   locationA.setLongitude(latLng.longitude);
                                   locationB.setLatitude(points.get(i).latitude);
                                   locationB.setLongitude(points.get(i).longitude);
                                   float d = locationA.distanceTo(locationB);
                                   if(d < 30) {
                                       islocationexisted = true;
                                       break;
                                   }
                               }


                               if ((k == points.size() - 1) && !islocationexisted && !isRouteUpdating) {

                                   isOriginalRouteChanged = true;
                                   for (int i = 0; i < removePolyLine.size(); i++) {
                                       removePolyLine.get(i).remove();
                                   }
                                   removePolyLine.clear();
                                   points.clear();
                                   points.add(latLng);
                                   for (int i = 0; i < removeMarkers.size(); i++) {
                                       removeMarkers.get(i).remove();
                                   }
                                   removeMarkers.clear();
                                   Marker m = drawMarker(latLng, addressString);
                                   removeMarkers.add(m);
                                   for (int i = 0; i < latiarr.size(); i++) {
                                       // points contain the current location

                                       String addressplace = placearr.get(i);
                                       LatLng pointplace = new LatLng(latiarr.get(i), longiarr.get(i));
                                       Marker marker = drawMarker(pointplace, addressplace);
                                       removeMarkers.add(marker);

                                       LatLng first = new LatLng(latiarr.get(0), longiarr.get(0));
                                       /// this drwas the line between two cities
                                       String url1 = getDirectionsUrl(latLng, first); // from here it draws line
                                       // System.out.println("url.............."+url1);
                                       isRouteUpdating = true;
                                       DownloadTask downloadTask1 = new DownloadTask();

                                       // Start downloading json data from Google Directions API
                                       downloadTask1.execute(url1);
                                       DownloadTask3 voicetask = new DownloadTask3();
                                       voicetask.execute(url1);
                                   }
                                   for (int i = 0; i < latiarr.size(); i++) {
                                       Placestatus[i] = "true";
                                       if (latiarr.size() > i + 1) {
                                           LatLng origin = new LatLng(latiarr.get(i), longiarr.get(i));
                                           LatLng dest = new LatLng(latiarr.get(i + 1), longiarr.get(i + 1));

                                           // Getting URL to the Google Directions API
                                           String url = getDirectionsUrl(origin, dest);
                                           isRouteUpdating = true;
                                           DownloadTask downloadTask = new DownloadTask();
                                           sourceplace = placearr.get(i);
                                           ponitplace = placearr.get(i + 1);
                                           // Start downloading json data from Google Directions API

                                           downloadTask.execute(url);

                                           DownloadTask3 voicetask = new DownloadTask3();
                                           voicetask.execute(url);

                                       }

                                   }
                                   // makes the orginal route false


                               }
                           }
                       }
                   }
                    String placeraech = null;
                    int i = 0;
                    final int placeno = 0;
//sets the current location
                    Location newLocation = new Location("my location");
                    newLocation.setLatitude(currentlatitude);
                    newLocation.setLongitude(currentlongitude);
                    Location oldLocation = new Location("dest");

                    for (i = 0; i < latiarr.size(); i++) {


                        oldLocation.setLongitude(longiarr.get(i));
                        oldLocation.setLatitude(latiarr.get(i));
                        placeraech = placearr.get(i);


                        MarkerOptions options = new MarkerOptions();
                        LatLng turnpos = new LatLng(latiarr.get(i), longiarr.get(i));
                        // Setting the position of the marker
                        options.position(turnpos);
                        mGoogleMap.addMarker(options);
                        DecimalFormat df = new DecimalFormat("#.##");
                        // checks if the person has reached its destination
                        Double distbet = Double.valueOf(oldLocation.distanceTo(newLocation) / 1000);
                        Double tar = 1.0 / 8;
                        if (distbet < tar && Placestatus[i].equals("true")) {


                            if (dialogShown == true) {
                                return;
                            } else {
                                dialogShown = true;
                                final AlertDialog.Builder builder = new AlertDialog.Builder(showmap.this, AlertDialog.THEME_HOLO_DARK);
                                builder.setTitle("DO YOU WANT TO GO NEXT DESTINATION");

                                builder.setMessage("YOU REACH YOUR DESTINATION " + "\n" + placeraech);

                                final int finalI = i;
                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        placecount = placecount + 1;
                                        visitedplace.setText(Integer.toString(placecount));
                                        Placestatus[finalI] = "false";
                                        dialog.dismiss();
                                        dialogShown = false;

                                    }
                                });
                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        dialog.dismiss();
                                    }
                                });

                                final AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    }
                    // if the voice button is turned on
                    if (voicebuttonstatus.equals("On")) {
                        for (int j = 0; j < stepall.size(); j++) {
                            Location turnLocation = new Location("turnpoint");
                            turnLocation.setLatitude(pointslatlong.get(j).latitude);
                            turnLocation.setLongitude(pointslatlong.get(j).longitude);
                            Location maplocation = new Location("current loc");
                            maplocation.setLatitude(location.getLatitude());
                            maplocation.setLongitude(location.getLongitude());
                            DecimalFormat df = new DecimalFormat("#.##");

                            Double distbet2 = Double.valueOf(turnLocation.distanceTo(maplocation));

                            Double tar = 90d;
                            System.out.println("voice####" + stepall.get(j));


                            if (distbet2 < tar && voicestatus.get(j).equals("true")) {

                                String text = stepall.get(j);

                                stepshowtxt.setText("" + text);
                                tts.speak("" + text, TextToSpeech.QUEUE_FLUSH, null);
                                voicestatus.add(j, "false");

                            }
                        }
                    }


                }
            });
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    Double markerlat = marker.getPosition().latitude;
                    Double markerlongi = marker.getPosition().longitude;
                    LatLng mark = null;
                    //  marker.getTitle();
                    if (marker.getTitle().equals(addressString)) {
                        mylocation = addressString;
                        for (int i = 0; i < latiarr.size(); i++) {
                            destination = placearr.get(0);
                            mark = new LatLng(latiarr.get(0), longiarr.get(0));
                        }
                    }
                    for (int j = 0; j < placearr.size(); j++) {

                        if (marker.getTitle().equals(placearr.get(j)) && placearr.size() > j + 1) {
                            mylocation = placearr.get(j);
                            destination = placearr.get(j + 1);
                            mark = new LatLng(latiarr.get(j + 1), longiarr.get(j + 1));


                        }


                    }
                    try {
                        LatLng source = new LatLng(markerlat, markerlongi);
                        String url = getDirectionsUrl(source, mark);

                        DownloadTask1 downloadTask1 = new DownloadTask1();

                        // Start downloading json data from Google Directions API
                        downloadTask1.execute(url);


                    } catch (Exception ex) {
                        for (int j = 0; j < placearr.size(); j++) {
                            if (marker.getTitle().equals(placearr.get(placearr.size() - 1))) {
                                Toast.makeText(getApplicationContext(), "This is last Destination", Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(getApplicationContext(), " please wait", Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                }
            });

        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service driving
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&sensor=false&units=metric&mode=driving";

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            //  Log.e("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

          //  int result = tts.setLanguage(Locale.ENGLISH.US);
            int result =0;
            if(languagOptionSpinner.getSelectedItem().equals("ENGLISH")) {
                result =tts.setLanguage(Locale.ENGLISH);
            } else if(languagOptionSpinner.getSelectedItem().equals("SOMALI")) {
              result = tts.setLanguage(Locale.GERMAN);
            } else  if(languagOptionSpinner.getSelectedItem().equals("Chinese")) {
                result = tts.setLanguage(Locale.CHINESE);
            }else if(languagOptionSpinner.getSelectedItem().equals("French")) {
                result = tts.setLanguage(Locale.FRENCH);
            }else if(languagOptionSpinner.getSelectedItem().equals("Japanese")) {
                result = tts.setLanguage(Locale.JAPANESE);
            }else if(languagOptionSpinner.getSelectedItem().equals("Korean")) {
                result = tts.setLanguage(Locale.KOREAN);
            }else if(languagOptionSpinner.getSelectedItem().equals("Italian")) {
                result = tts.setLanguage(Locale.ITALIAN);
            } else if(languagOptionSpinner.getSelectedItem().equals("Taiwan")) {
                result = tts.setLanguage(Locale.TAIWAN);
            }

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
              // voiceimg.setEnabled(true);
               // speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }



    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String>{

        Dialog progress;
        // Downloading data in non-ui thread
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(showmap.this,
                    "Loading data", "Please wait...");
            super.onPreExecute();
        }
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
              //  Log.d("Background Task",data);
                Toast.makeText(getApplicationContext(),
                        "data json"+data,Toast.LENGTH_LONG).show();

            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
try {
    progress.dismiss();
    ParserTask parserTask = new ParserTask();

    // Invokes the thread for parsing the JSON data
    parserTask.execute(result);

}
catch (Exception es)
{
    Toast.makeText(getApplicationContext(), "Error in connection",Toast.LENGTH_SHORT).show();

}
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            check++;
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);

            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
  //   ArrayList<LatLng> points = null;

            try {
            PolylineOptions lineOptions = null;
                PolylineOptions lineOptions1 = null;
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                ArrayList<LatLng>    pointss = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                lineOptions1 = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    pointss.add(position);
                    points.add(position);

  }


                // Adding all the points in the route to LineOption
                if(isOriginalRouteChanged) {
                    lineOptions.addAll(pointss);
                    lineOptions.width(15);
                    lineOptions.color(Color.parseColor("#ff0000"));//blue #42A5F5 #CC0000

                    lineOptions1.addAll(pointss);
                    lineOptions1.width(5);
                    lineOptions1.color(Color.parseColor("#ff0000"));
                }

                if(colorroute==1 && !isOriginalRouteChanged)
                {
                    lineOptions.addAll(pointss);
                    lineOptions.width(15);
                    lineOptions.color(Color.parseColor("#585858"));//blue #42A5F5 #CC0000

                    lineOptions1.addAll(pointss);
                    lineOptions1.width(5);
                    lineOptions1.color(Color.parseColor("#DFDBDB"));//"#EF5350"
                }
                else if(!isOriginalRouteChanged)
                {
                    lineOptions.addAll(pointss);
                    lineOptions.width(15);
                    lineOptions.color(Color.parseColor("#2196F3"));//blue #42A5F5 #CC0000

                    lineOptions1.addAll(pointss);
                    lineOptions1.width(5);
                    lineOptions1.color(Color.parseColor("#90CAF9"));//"#EF5350"
                }

            }

            // Drawing polyline in the Google Map for the i-th route

                Polyline polyLine2 = mGoogleMap.addPolyline(lineOptions);
                Polyline polyLine1 = mGoogleMap.addPolyline(lineOptions1);
                removePolyLine.add(polyLine1);
                removePolyLine.add(polyLine2);
                if(check == arrraySize) {
                    isRouteUpdating = false;
                    check = 0;
                    if(isOriginalRouteChanged) {
                        isOriginalRouteChanged = false;
                    }
                }

            }
            catch (Exception e)
            {
                System.out.println("excc"+e.toString());
               // Toast.makeText(getApplicationContext(), "No Route between"+sourceplace+" to "+ponitplace,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DownloadTask3 extends AsyncTask<String, Void, String>{

        Dialog progress;
        // Downloading data in non-ui thread
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(showmap.this,
                    "Loading data", "Please wait...");
            super.onPreExecute();
        }
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);


            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask3 parserTask3 = new ParserTask3();

            // Invokes the thread for parsing the JSON data
            parserTask3.execute(result);
            progress.dismiss();
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask3 extends AsyncTask<String, Integer, HashMap<String,String> >{


        // Parsing the data in non-ui thread
        @Override
        protected  HashMap<String,String> doInBackground(String... jsonData) {

            JSONObject jObject;
            HashMap<String,String> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser2 parser = new DirectionsJSONParser2();

                // Starts parsing data
                routes = parser.parse(jObject);

            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(HashMap<String,String> result) {
try {
    System.out.println("data" + result.size());


    for (Map.Entry<String, String> entry : result.entrySet()) {
        System.out.println(entry.getKey() + " = " + entry.getValue());
        stepall.add(entry.getValue());
        String placelatlong[] = entry.getKey().split(",");
        Double lat = Double.valueOf(placelatlong[0]);
        Double lang = Double.valueOf(placelatlong[1]);
        LatLng placeturn = new LatLng(lat, lang);
        pointslatlong.add(placeturn);
        voicestatus.add("true");
    }
}catch (Exception es)
{
    Toast.makeText(getApplicationContext(), "No Internet connection " ,
            Toast.LENGTH_SHORT).show();
}
        }
    }
    private class DownloadTask1 extends AsyncTask<String, Void, String>{
        Dialog progress;
        // Downloading data in non-ui thread
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(showmap.this,
                    "Loading data", "Please wait...");
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                //  Log.d("Background Task",data);
                /*Toast.makeText(getApplicationContext(),
                        "data json"+data,Toast.LENGTH_LONG).show();*/

            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask1 parserTask = new ParserTask1();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
            progress.dismiss();
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask1 extends AsyncTask<String, Integer,ArrayList<String> >{

        // Parsing the data in non-ui thread
        @Override
        protected  ArrayList<String> doInBackground(String... jsonData) {

            JSONObject jObject;
            ArrayList<String> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser1 parser = new DirectionsJSONParser1();

                // Starts parsing data
                routes = parser.parse(jObject);

            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(ArrayList<String> result) {

            Intent intentcount=new Intent(getApplicationContext(),DirectionStep.class);
            intentcount.putExtra("steparr",result);
            intentcount.putExtra("mylocation",mylocation);
            intentcount.putExtra("destination",destination);
            startActivity(intentcount);

         }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_showmap, menu);
        return true;
    }

    private Marker drawMarker(LatLng point,String addrepl){
        mMarkerPoints.add(point);


        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(point).title(addrepl);

        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
        if(mMarkerPoints.size()==1){
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }else if(mMarkerPoints.size()==2){
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        // Add new marker to the Google Map Android API V2
       return  mGoogleMap.addMarker(options);
    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    private static BigDecimal truncateDecimal(double x,int numberofDecimals)
    {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }

}