package com.example.gravityshouldbenaut.park;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        try {
            setUpMapIfNeeded();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            setUpMapIfNeeded();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() throws IOException, JSONException {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */





    private List<MapPin> loadFreePinsToList() throws IOException, JSONException  {
        List<MapPin> items = new ArrayList<MapPin>();

        // In this case we're loading from local assets.
        // NOTE: could alternatively easily load from network
        InputStream stream = null;
        try {
            stream = new URL("https://raw.githubusercontent.com/gravityshouldbenaut/JSONFiles/master/parkingSpots.json").openStream();
        } catch (IOException e) {
            return null;
        }

        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(new InputStreamReader(stream)).getAsJsonArray();

        // Open a transaction to store items into the realm
        realm.beginTransaction();
        for (JsonElement e : jsonArray) {
            // Create a realm capable object
            MapPin freePin = realm.createObject(MapPin.class);
            freePin.createSnip(e.getAsJsonObject().get("Snip").getAsString());
            freePin.createLat(e.getAsJsonObject().get("Lat").getAsDouble());
            freePin.createLongi(e.getAsJsonObject().get("Long").getAsDouble());
            // Minor optimization to keep the new cities in a list
            // so it doesn't have to be reloaded the first time
            items.add(freePin);
        }
        realm.commitTransaction();

        return items;
    }
    private List<MapPin> loadPaidPinsToList() throws IOException, JSONException  {
        List<MapPin> items = new ArrayList<MapPin>();

        // In this case we're loading from local assets.
        // NOTE: could alternatively easily load from network
        InputStream stream = null;
        try {
            stream = new URL("https://raw.githubusercontent.com/gravityshouldbenaut/JSONFiles/master/paidParkingSpots.json").openStream();
        } catch (IOException e) {
            return null;
        }

        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(new InputStreamReader(stream)).getAsJsonArray();

        // Open a transaction to store items into the realm
        realm.beginTransaction();
        for (JsonElement e : jsonArray) {
            // Create a realm capable object
            MapPin paidPin = realm.createObject(MapPin.class);
            paidPin.createSnip(e.getAsJsonObject().get("Snip").getAsString());
            paidPin.createLat(e.getAsJsonObject().get("Lat").getAsDouble());
            paidPin.createLongi(e.getAsJsonObject().get("Long").getAsDouble());
            // Minor optimization to keep the new cities in a list
            // so it doesn't have to be reloaded the first time
            items.add(paidPin);
        }
        realm.commitTransaction();

        return items;
    }
    private void setUpMap() throws IOException, JSONException {
     List <MapPin> FreePins = this.loadFreePinsToList();
     for(int x=0; x<FreePins.size(); x++){
         MapPin xPin = FreePins.get(x);
         String xSnip = xPin.snip();
         double xLat = xPin.lat();
         double xLong = xPin.longi();
         mMap.addMarker(new MarkerOptions().position(new LatLng(xLat, xLong)).title(xSnip).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
     }
    List<MapPin> PaidPins = this.loadPaidPinsToList();
        for(int x=0; x<PaidPins.size(); x++){
            MapPin xPin = PaidPins.get(x);
            String xSnip = xPin.snip();
            double xLat = xPin.lat();
            double xLong = xPin.longi();
            mMap.addMarker(new MarkerOptions().position(new LatLng(xLat, xLong)).title(xSnip).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


        }




    }


}
