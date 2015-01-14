package com.example.gravityshouldbenaut.park;

import io.realm.RealmObject;

/**
 * Created by gravityshouldbenaut on 12/29/14.
 */
public class MapPin extends RealmObject {
    String snip;
    double lat;
    double longi;
    public String createSnip(String s){
        snip = s;
        return snip;
    }

    public double createLat(double d){
         lat = d;
        return lat;
    }

    public double createLongi(double d){
         longi = d;
        return longi;
    }
    public String snip(){
      return snip;
    }
    public double lat(){
        return lat;
    }
    public double longi(){
        return longi;
    }
}
