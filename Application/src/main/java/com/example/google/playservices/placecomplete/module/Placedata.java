package com.example.google.playservices.placecomplete.module;

import java.util.Comparator;

/**
 * Created by abc on 7/7/2015.
 */
public class Placedata {

    public double latitude;
    public double longitude;

    public double getCurrentlatitude() {
        return currentlatitude;
    }

    public void setCurrentlatitude(double currentlatitude) {
        this.currentlatitude = currentlatitude;
    }

    public double currentlatitude;

    public double getCurrentlongitude() {
        return currentlongitude;
    }

    public void setCurrentlongitude(double currentlongitude) {
        this.currentlongitude = currentlongitude;
    }

    public double currentlongitude;
    public double distance;
    public String place;



    public Placedata(){
        super();
    }

    public Placedata(String placeName, double dist, double latitude,double longitude)
    {
        super();
        this.place = placeName;

        this.distance = dist;

        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }




    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



}






   /*@Override
    public int compareTo(Placedata another) {

       double distancecompare = ((Placedata) another).getDistance();
        return (int)(this.distance - distancecompare);
    }*/


