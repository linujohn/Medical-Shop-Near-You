package com.example.hp.map_experiment;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by navneet on 23/7/16.
 */
public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {


    String googlePlacesData;
    GoogleMap mMap;
    String url,txt;




    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            txt = params[2].toString();
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        Searching sm = new Searching();
        List<HashMap<String, String>> npl= sm.selected(nearbyPlacesList,txt);
        ShowNearbyPlaces(npl);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");


    }
    private void ShowNearbyPlaces(List<HashMap<String, String>> nearyPlacesList) {

            for (int i = 0; i < nearyPlacesList.size(); i++) {
                Log.d("onPostExecute", "Entered into showing locations");
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : " + vicinity);
                mMap.addMarker(markerOptions);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }


    }
}
