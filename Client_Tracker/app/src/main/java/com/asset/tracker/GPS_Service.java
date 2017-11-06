package com.asset.tracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by Rohan on 9/29/2017.
 */

public class GPS_Service extends Service{
    private LocationManager locationManager;        //LocationManager requests location of user
    private LocationListener locationListener;      //LocationListener listens to the response of LocationManager

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate() {
                locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                textView.setText("Latitude:"+location.getLatitude()+" , Longitude:"+location.getLongitude());
                Intent i=new Intent("location_update");
                i.putExtra("coordinates","Lat:"+location.getLatitude()+",\nLong: "+location.getLongitude());
                i.putExtra("latitude",location.getLatitude());
                i.putExtra("longitude",location.getLongitude());
                sendBroadcast(i);       //send Broadcast to MainScreenActivity to update location
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }
            /*If GPS is not on, open settings*/
            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        /*listen to location*/
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,locationListener); //Request location every 5 seconds

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }
}
