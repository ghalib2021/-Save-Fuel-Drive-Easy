package com.example.google.playservices.placecomplete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Handler;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import ParserClass.DirectionsJSONParser;
import ParserClass.DirectionsJSONParser1;
import ParserClass.DirectionsJSONParser2;
import BeanClass.GPSTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class showmap extends FragmentActivity implements LocationListener,TextToSpeech.OnInitListener{

    GoogleMap mGoogleMap;
    GPSTracker gps;
    int placecount=0;
    boolean statusshowdialog=false;
    ProgressDialog progress;
    private float[] mGravity;
    private float[] mMagnetic;
    ArrayList<LatLng> mMarkerPoints;
    private TextToSpeech tts;
    String addressString = null;
    private double latitude = 0;
    private double longitude = 0;
    ArrayList<LatLng> points = null;
    ArrayList<LatLng> pointslatlong = new ArrayList<LatLng>();
    ArrayList<String> stepall = new ArrayList<String>();
    public ArrayList<String> steparr=new ArrayList<String>();
    ArrayList<Double> latiarr = new ArrayList<Double>();
    ArrayList<Double> longiarr = new ArrayList<Double>();
    ArrayList<String> placearr = new ArrayList<String>();
    ArrayList<Double> distarr= new ArrayList<Double>();
    ArrayList<Double> currentlocation= new ArrayList<Double>();
    public String ponitplace,sourceplace,mylocation,destination;
    ImageView btnmap,btnsate;
    TextView totalplace,visitedplace;
    float bearinginfloat;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_showmap);

        final ArrayList<Double> latiarr = (ArrayList<Double>) getIntent().getSerializableExtra("latiarr");
        final ArrayList<Double> longiarr= (ArrayList<Double>) getIntent().getSerializableExtra("longiarr");
        final ArrayList<String> placearr= (ArrayList<String>) getIntent().getSerializableExtra("placearr");
        ArrayList<Double> distarr= (ArrayList<Double>) getIntent().getSerializableExtra("distarr");
        final ArrayList<Double> currentlocation= (ArrayList<Double>) getIntent().getSerializableExtra("currentlocation");
        final String Placestatus[]=new String[latiarr.size()];
        totalplace= (TextView) findViewById(R.id.textViewplace);
        visitedplace= (TextView) findViewById(R.id.textvisitedplace);
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
             btnmap= (ImageView) findViewById(R.id.imageButtonmap);
             btnsate= (ImageView) findViewById(R.id.imageButtonsatellite);
         //   voiceimg= (ImageButton) findViewById(R.id.imagevoice);
            tts = new TextToSpeech(this, this);
            tts.setPitch(0.1f);
           // tts.setSpeechRate(0.5f);
            tts.setSpeechRate(0.1f);
            // Getting reference to SupportMapFragment of the activity_main
            // SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapView);
            mGoogleMap=((MapFragment) getFragmentManager().findFragmentById(
                    R.id.mapshowlay)).getMap();
            // Getting Map for the SupportMapFragment
            // mGoogleMap = fm.getMap();

            // Enable MyLocation Button in the Map
            mGoogleMap.setMyLocationEnabled(true);

           // mGoogleMap.setTrafficEnabled(true);

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
            for(int i=0;i<currentlocation.size();i++)
            {
                latitude = currentlocation.get(0);
                longitude = currentlocation.get(1);

            }

           mGoogleMap.setBuildingsEnabled(true);
mGoogleMap.getUiSettings().setCompassEnabled(true);
            final LatLng point = new LatLng(latitude, longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(point)
                    .bearing(45)
                    .tilt(90)
                    .zoom(60)
                    .build(); //Creates a CameraPosition from the builder*/
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    cameraPosition));
           /* mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));*/
            btnmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mGoogleMap.getMapType()==GoogleMap.MAP_TYPE_SATELLITE) {
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
            drawMarker(point,addressString );
try {
    for (int i = 0; i < latiarr.size(); i++) {

        String addressplace = placearr.get(i);
        LatLng pointplace = new LatLng(latiarr.get(i), longiarr.get(i));
        drawMarker(pointplace, addressplace);
        LatLng first = new LatLng(latiarr.get(0), longiarr.get(0));
        String url1 = getDirectionsUrl(point, first);

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



            mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    /*final LatLng movingpoint = new LatLng(location.getLatitude(),location.getLongitude());
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(movingpoint));*/
                    LatLng movingpoint = new LatLng(location.getLatitude(),location.getLongitude());
                    /*CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(movingpoint)
                            .bearing(45)
                            .tilt(90)
                            .zoom(60)
                            .build(); //Creates a CameraPosition from the builder*//*
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            cameraPosition));*/

                    /*CameraPosition currentPlace = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(),location.getLongitude()))
                            .bearing(90).tilt(90).zoom(18f).build();
                    mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));*/
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(),location.getLongitude()))
                            .bearing(bearinginfloat)
                            .tilt(45)
                            .zoom(14)
                            .build(); //Creates a CameraPosition from the builder*/
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            cameraPosition));
                  String placeraech = null;
                    int i=0;
                   final int placeno=0;
                    gps = new GPSTracker(showmap.this);
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Location newLocation = new Location("my location");
                    newLocation.setLatitude(latitude);
                    newLocation.setLongitude(longitude);
                    Location oldLocation = new Location("dest");
            for( i=0;i<latiarr.size();i++) {

              oldLocation.setLongitude(longiarr.get(i));
              oldLocation.setLatitude(latiarr.get(i));
              placeraech=placearr.get(i);

                DecimalFormat df = new DecimalFormat("#.##");
                Double  distbet= Double.valueOf(oldLocation.distanceTo(newLocation)/1000);
                Double tar=1.0/7;

                if(distbet<tar&&Placestatus[i].equals("true"))
                {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(showmap.this, AlertDialog.THEME_HOLO_DARK);
                    builder.setTitle("DO YOU WANT TO GO NEXT DESTINATION");

                    builder.setMessage("YOU REACH YOUR DESTINATION "+"\n"+placeraech);

                    final int finalI = i;
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           Placestatus[finalI]="false";
                            dialog.dismiss();
                            placecount=placecount+1;
                            visitedplace.setText(Integer.toString(placecount));
                        }
                    });
                    builder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    new CountDownTimer(5000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub

                            dialog.dismiss();
                        }
                    }.start();
                   // Toast.makeText(getApplicationContext(),"Reach to Place "+placeraech,Toast.LENGTH_LONG).show();

                }
                }
                    Location turnLocation = new Location("turnpoint");
                    for(int j=0;j<stepall.size();j++) {
                        turnLocation.setLatitude(pointslatlong.get(j).latitude);
                        turnLocation.setLongitude(pointslatlong.get(j).longitude);

                        DecimalFormat df = new DecimalFormat("#.##");

                        Double  distbet2= Double.valueOf(turnLocation.distanceTo(newLocation));

                        Double tar=23d;

                        if(distbet2<tar)
                        {

                            String text =stepall.get(j);

                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);


                        }
                    }




                }
            });
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    Double markerlat =marker.getPosition().latitude;
                    Double markerlongi=marker.getPosition().longitude;
                    LatLng mark = null;
                  //  marker.getTitle();
                    if(marker.getTitle().equals(addressString))
                    {
                        mylocation=addressString;
                        for(int i=0;i<latiarr.size();i++)
                        {
                            destination=placearr.get(0);
                            mark=new LatLng(latiarr.get(0),longiarr.get(0));
                        }
                    }
          for(int j=0;j<placearr.size();j++) {

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


                    }
                    catch (Exception ex)
                    {
                        for(int j=0;j<placearr.size();j++) {
                            if (marker.getTitle().equals(placearr.get(placearr.size() - 1))) {
                                Toast.makeText(getApplicationContext(), "This is last Destination", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                               // Toast.makeText(getApplicationContext()," network probleam",Toast.LENGTH_LONG).show();
                               // ex.printStackTrace();
                                Toast.makeText(getApplicationContext()," sss"+ex.toString(),Toast.LENGTH_LONG).show();
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

        // Building the url to the web service
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

            int result = tts.setLanguage(Locale.ENGLISH);

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
    ParserTask parserTask = new ParserTask();

    // Invokes the thread for parsing the JSON data
    parserTask.execute(result);
    progress.dismiss();
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
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
          points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);



                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            try {
                mGoogleMap.addPolyline(lineOptions);

            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "No Route between"+sourceplace+" to "+ponitplace,Toast.LENGTH_SHORT).show();
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
                //  Log.d("Background Task",data);
               /* Toast.makeText(getApplicationContext(),
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

System.out.println("data"+result.size());


            for (Map.Entry<String, String> entry :result.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
                stepall.add(entry.getValue());
                String placelatlong[]=entry.getKey().split(",");
                Double lat= Double.valueOf(placelatlong[0]);
                Double lang= Double.valueOf(placelatlong[1]);
                LatLng placeturn=new LatLng(lat,lang);
                pointslatlong.add(placeturn);
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

    private void drawMarker(LatLng point,String addrepl){
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
        mGoogleMap.addMarker(options);
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


    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {

            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetic = event.values.clone();
                break;
            default:
                return;
        }
        if(mGravity != null && mMagnetic != null) {
            getDirection();
        }
    }

    private float getDirection()
    {

        float[] temp = new float[9];
        float[] R = new float[9];
        //Load rotation matrix into R
        SensorManager.getRotationMatrix(temp, null,
                mGravity, mMagnetic);

        //Remap to camera's point-of-view
        SensorManager.remapCoordinateSystem(temp,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z, R);

        //Return the orientation values
        float[] values = new float[3];
        SensorManager.getOrientation(R, values);

        //Convert to degrees
        for (int i=0; i < values.length; i++) {
            Double degrees = (values[i] * 180) / Math.PI;
            values[i] = degrees.floatValue();
        }
bearinginfloat=values[0];
        return bearinginfloat;

    }
}