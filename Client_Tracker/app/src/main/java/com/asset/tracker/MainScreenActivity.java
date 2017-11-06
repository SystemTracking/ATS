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
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.util.Date;


public class MainScreenActivity extends AppCompatActivity {
    private Button button;
    private Button res_button;
    private double latitude;
    private double longitude;
    private BroadcastReceiver broadcastReceiver;
    private TextView textView;
    private TextView tv;
    private String username;
    private String asset_id;
    private int asset_list_id;
    Geocoder geocoder;
    private String currentDateTimeString;
    List<Address> addresses;


    @Override
/*as long as the MainScreenActivity is displayed, run this function*/
    protected void onResume() {
        super.onResume();

        if(broadcastReceiver==null){
            /*broadcast service run in background which gets coordinates of the user*/
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    textView.setText(""+intent.getExtras().get("coordinates"));     //set coordinates of user in text field
                    latitude=Double.parseDouble(""+intent.getExtras().get("latitude"));     //get latitude from intent
                    longitude=Double.parseDouble(""+intent.getExtras().get("longitude"));   //get longitude from intent
                    currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());    //current date time

                    /*Dummy response Listener which does nothing*/
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };


                    SendLocationRequest sendLocationRequest = new SendLocationRequest(asset_list_id,latitude,longitude,currentDateTimeString,listener);    //SendLocationRequest object
                    RequestQueue queue = Volley.newRequestQueue(MainScreenActivity.this);   //make a queue of requests
                    queue.add(sendLocationRequest);     //add sendLocationRequest to queue of Volley which helps in networking of data

//                    try{
//                        addresses=geocoder.getFromLocation(latitude,longitude,1);
//
//                        String address= addresses.get(0).getAddressLine(0);
//                        String area= addresses.get(0).getLocality();
//                        String city= addresses.get(0).getAdminArea();
//                        String country= addresses.get(0).getCountryName();
////            String country= addresses.get(0).get();
//
//                        String fullAdd=address+", "+area+", "+city+", "+country;
//                        tv.setText(fullAdd);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    /*when the app closes, unregister the broadcast receiver*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
    }


    /*When the MainScreenActivity is created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        button = (Button) findViewById(R.id.button6);   //
        res_button = (Button) findViewById(R.id.button4);   //
        textView=(TextView) findViewById(R.id.textView8);   //
        tv=(TextView) findViewById(R.id.textView9);     //
        EditText name = (EditText) findViewById(R.id.editText3);    // name field
        Intent intent = getIntent();        //get Intent from MainActivity
        username = intent.getStringExtra("username");       //get username entered in MainActivity
        asset_id = intent.getStringExtra("asset_id");   //get asset id entered in MainActivity

        name.setText(username);
        /*Listen to response from server, if response is true, then get corresponding Asset List ID else retry*/
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        asset_list_id = jsonResponse.getInt("asset_list_id");
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
                        builder.setMessage("Getting Asset List ID Failed").setNegativeButton("Retry",null).create().show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };





        GetAssetListIDRequest getAssetListIDRequest = new GetAssetListIDRequest(username,asset_id,listener); //create object of GetAssetListIDRequest
        RequestQueue queue = Volley.newRequestQueue(MainScreenActivity.this);
        queue.add(getAssetListIDRequest);



//        geocoder=new Geocoder(this, Locale.getDefault());

/*If all the permissions are enabled, run enable_buttons() function which starts the GPS service when Start button is clicked and stops it when Reset button is clicked */
        if(!runtime_permissions()){
            enable_buttons();
        }


    }
/**/
    private void enable_buttons() {
        /*when Start button is clicked, GPS_Service starts*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),GPS_Service.class);
                startService(intent);
            }
        });
        /*when Reset button is clicked, GPS_Service is stopped and MainActivity screen is shown*/
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

    /*check if the required permissions are given*/
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

/*On clicking Exit button */
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
