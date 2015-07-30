package com.example.google.playservices.placecomplete.module;

import com.example.android.common.logger.Log;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionsJSONParser1 {
    public ArrayList<String> as=new ArrayList<String>();
    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public ArrayList<String> parse(JSONObject jObject){
        HashMap<String, String> stepdata = new HashMap<String, String>();
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        JSONObject jDistance = null;
        JSONObject jDuration = null;
        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                // System.out.println("legs..............."+jLegs.get(j));
                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                      /*String instruction;
                        JSONObject json_obj =  jSteps.getJSONObject(i);*/

                 System.out.println("steps for going "+jSteps.get(k));
                       /* instruction = json_obj.getString("html_instructions");
                      //  System.out.println("steps for going "+instruction);
                        as.add(instruction );*/

                        if((boolean)(((JSONObject)jSteps.get(k)).has("html_instructions")))
                        {
                            String html=(String)(((JSONObject)jSteps.get(k)).get("html_instructions"));
                            String html1=html.replaceAll("\\<.*?>","");
                            String newString = html1.replaceFirst( "\\((^\\))+\\)", "").trim();
                            System.out.println("steps @@@@ "+html1);
                            Log.d("html_instructions", html1);
                            String distancestep="Distance :"+(String)((JSONObject)((JSONObject)jSteps.get(k)).get("distance")).get("text");
                            String durationstep="Duration :"+((JSONObject)((JSONObject)jSteps.get(k)).get("duration")).get("text");
                            String stepdatastr=newString+"\n"+distancestep+"\n"+durationstep;
                            as.add(stepdatastr);
                        }
                       }
                   }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
        return as;
    }

    /**
     * Method to decode polyline points
     * Courtesy : jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}