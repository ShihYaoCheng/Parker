package com.jas.parker.module.map;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
/**
 * Created by User on 2016/7/31.
 */
public class Navigation {
    private double departureLat;//出發緯度
    private double departureLon;//出發經度
    private double destinationLat;//目的地緯度
    private double destinationLon;//目的地經度

    public Navigation(){
    }
    public Navigation(double lat,double lon){
        destinationLat = lat;
        destinationLon = lon;
    }
    public void startNavigation(double lat,double lon){
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon + "&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        //startActivity(mapIntent);
    }
}
