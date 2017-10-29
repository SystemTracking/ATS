package com.asset.tracker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainScreenActivity extends AppCompatActivity {
    private Button button;
    private Button res_button;
    private double latitude;
    private double longitude;
    private BroadcastReceiver broadcastReceiver;
    private TextView textView;
    private TextView tv;
    Geocoder geocoder;
    List<Address> addresses;
    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver==null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    textView.setText(""+intent.getExtras().get("coordinates"));
                    latitude=Double.parseDouble(""+intent.getExtras().get("latitude"));
                    longitude=Double.parseDouble(""+intent.getExtras().get("longitude"));
                    try{
                        addresses=geocoder.getFromLocation(latitude,longitude,1);

                        String address= addresses.get(0).getAddressLine(0);
                        String area= addresses.get(0).getLocality();
                        String city= addresses.get(0).getAdminArea();
                        String country= addresses.get(0).getCountryName();
//            String country= addresses.get(0).get();

                        String fullAdd=address+", "+area+", "+city+", "+country;
                        tv.setText(fullAdd);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        button = (Button) findViewById(R.id.button6);
        res_button = (Button) findViewById(R.id.button4);
        textView=(TextView) findViewById(R.id.textView8);
        tv=(TextView) findViewById(R.id.textView9);
        geocoder=new Geocoder(this, Locale.getDefault());


        if(!runtime_permissions()){
            enable_buttons();
        }


    }

    private void enable_buttons() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),GPS_Service.class);
                startService(intent);
            }
        });
        res_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent1=new Intent(v.getContext(), MainActivity.class);

                final Intent intent=new Intent(v.getContext(),GPS_Service.class);

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Reset?")
                        .setMessage("Are you sure you want to reset the AO key and vehicle ID?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                stopService(intent);
                                startActivity(intent1);

                            }
                        }).create().show();

            }

        });
    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET
                },10);
                return true;
        }
        return false;
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    enable_buttons();
                else
                    runtime_permissions();
        }
    }


    public void exit(View view){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
