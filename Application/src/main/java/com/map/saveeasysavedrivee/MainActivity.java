/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.map.saveeasysavedrivee;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.map.saveeasysavedrivee.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import Adapterclass.CustomListAdapter;
import Adapterclass.PlaceListAdapter;
import BeanClass.ConnectionDetector;
import BeanClass.GPSTracker;
import BeanClass.Placedata;

public class MainActivity extends ActionBarActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    LinearLayout verlay;
    GPSTracker gps;
    Context ctx;
    ListView placeshowlist;
    Map<String, String> treeMap;
    private double latitude = 0;
    private double longitude = 0;
    long latintformat,langintformat;
    private ListView listView;
    private String lati ;
    private String longi;
    ArrayList<String> currentlocation= new ArrayList<String>();
   public Double dist;
    private ArrayList<Placedata> arrlistPlaces = new ArrayList<>();
    ArrayList<String> placearr = new ArrayList<String>();

    ArrayList<String> latiarr= new ArrayList<String>();
    ArrayList<String> longiarr= new ArrayList<String>();
    ArrayList<String> placeid= new ArrayList<String>();
    private PlaceListAdapter placeList;
   public  String distance,placeshow,name,placeidstr;
    ArrayList<Object> objectadd = new ArrayList<Object>();
    ArrayList <Placedata> InfoList = new ArrayList<Placedata> ();
    TreeMap tm = new TreeMap();
    ConnectionDetector cd;
    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    Boolean isInternetPresent = false;
    protected static final int RESULT_SPEECH = 1;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> distanceOptionList = new ArrayList<>();
    TreeMap<String, String> hmap;
    /** Declaring an ArrayAdapter to set items to ListView */
    ArrayAdapter<String> adapter;
    protected GoogleApiClient mGoogleApiClient;
    private ImageView btnSpeak;
    private PlaceAutocompleteAdapter mAdapter;
    ArrayList<Placedata> results = new ArrayList<Placedata>();
    private AutoCompleteTextView mAutocompleteView;
    Location location;
    private TextView mPlaceDetailsText;
ImageView sortbtn,resetbtn,addbtn;
    private TextView mPlaceDetailsAttribution;
    private String selectedItem;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    private Spinner distanceChooser;
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
        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        setContentView(R.layout.activity_main);
        distanceOptionList.add("KiloMeter");
        distanceOptionList.add("Miles");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinnerlayout,distanceOptionList);
verlay= (LinearLayout) findViewById(R.id.linver);
distanceChooser = (Spinner)findViewById(R.id.spinner1);
   distanceChooser.setAdapter(adapter);
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Retrieve the TextViews that will display details and attributions of the selected place.


        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.


        mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        mAutocompleteView.setAdapter(mAdapter);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
           /* showAlertDialog(MainActivity.this, "Internet Connection",
                    "You have internet connection", true);*/
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(MainActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

       placeshowlist= (ListView) findViewById(R.id.listViewplace);
        // Set up the 'clear text' button that clears the text in the autocomplete view
       // listView = (ListView) findViewById(R.id.listItem);
        btnSpeak = (ImageView) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        getString(R.string.speech_prompt));

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    mAutocompleteView.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            currentlocation.add(Double.toString(latitude));
            currentlocation.add(Double.toString(longitude));
        }
        else
        {
            gps.showSettingsAlert();

        }

           addbtn= (ImageView) findViewById(R.id.buttonplus);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(gps.canGetLocation()==false)
                    {
                        gps = new GPSTracker(MainActivity.this);
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        currentlocation.add(Double.toString(latitude));
                        currentlocation.add(Double.toString(longitude));

                    }


                    String placeselected=placeshow;
                    if (placeselected.equals("")) {
                        Toast.makeText(getBaseContext(), "please select place", Toast.LENGTH_SHORT).show();

                    } else {
                        if (placeid.contains(placeidstr)) {
                            Toast.makeText(getBaseContext(), "You Have already added this place", Toast.LENGTH_SHORT).show();
                        } else {


                            Placedata newsData = new Placedata();
                            newsData.setPlace(placeselected);
                            results.add(newsData);

                            placeshowlist.setAdapter(new CustomListAdapter(MainActivity.this, results));

                            placearr.add(placeselected);
                            System.out.print("#########1 screen"+dist + "lati" + lati + "logi" + longi + placeidstr);

                            latiarr.add(lati);
                            longiarr.add(longi);
                            placeid.add(placeidstr);

                            mAutocompleteView.setText("");
                        }

                    }


                }
                catch (Exception e)
                {


                }
            }
        });
        resetbtn= (ImageView) findViewById(R.id.button_reset);

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(getIntent());

            }
        });

       sortbtn= (ImageView) findViewById(R.id.buttonsort);



sortbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

if(placearr.size()>0) {
    Intent intentcount = new Intent(getApplicationContext(), Sortshow.class);

    intentcount.putExtra("placearr", placearr);
    intentcount.putExtra("distanceoption",distanceChooser.getSelectedItem().toString());
    intentcount.putExtra("latiarr", latiarr);
    intentcount.putExtra("longiarr", longiarr);
    intentcount.putExtra("currentlocation", currentlocation);
    startActivity(intentcount);
}
else{
    Toast.makeText(getBaseContext(), "please add place first", Toast.LENGTH_SHORT).show();
}

    }
});

        placeshowlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                selectedItem = placearr.get(position).toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_HOLO_DARK);
                builder.setMessage("Do you want to remove " + selectedItem + "?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        results.remove(position);
                        placearr.remove(position);

                        latiarr.remove(position);
                        longiarr.remove(position);
                        CustomListAdapter adapter_code = (CustomListAdapter)placeshowlist.getAdapter();

                        adapter_code.notifyDataSetChanged();


                        Toast.makeText(
                                getApplicationContext(),
                                selectedItem + " has been removed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Create and show the dialog
                builder.show();

                // Signal OK to avoid further processing of the long click
                return true;
            }
        });



    }
    public static void printMap(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());


        }
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
//            Log.i(TAG, "Autocomplete item selected: " + item.description);
            if(item.description.equals("")) {
                Toast.makeText(getApplicationContext(), "Turn on Internet connection and GPS Location" + item.description,
                        Toast.LENGTH_SHORT).show();
            }
            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);




        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
//                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            LatLng latLng=place.getLatLng();
            lati=Double.toString(latLng.latitude);
            longi=Double.toString(latLng.longitude);
            placeidstr=place.getId();
            placeshow=place.getName().toString()+" "+place.getAddress().toString();
            String show=placeshow+" Distance="+distance;
         // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
              //  mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
               // mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
               // mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }

 //           Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
            CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
 //       Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
 //               websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

 //       Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
 //               + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }



    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
       // alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // application exit code
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Exit Application");

        builder.setMessage("Do you really want to exit?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    mAutocompleteView.setText(text.get(0));
                }
                break;
            }

        }
    }

}
