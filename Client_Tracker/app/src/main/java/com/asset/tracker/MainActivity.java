package com.asset.tracker;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/*
Read the following link to see changes made to build.gradle to add google play services:
https://stackoverflow.com/questions/45692460/failed-to-resolve-com-google-android-gmsplay-services-in-intellij-idea-with-gr
*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText eUsername = (EditText) findViewById(R.id.editText);  // Username(Asset Owner key) field
        final EditText eAsset_ID = (EditText) findViewById(R.id.editText2); //Asset ID(vehicle number) field
        final Button bEnter = (Button) findViewById(R.id.button);   // Enter Button

        bEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username =  eUsername.getText().toString();    //Get username
                final String asset_id = eAsset_ID.getText().toString();     //Get Asset ID field

                /*Listen to response from server*/
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);     //make JSON object of response
                            boolean success = jsonResponse.getBoolean("success");   //store value of "success" key

                            /*If success is true, go to MainScreen, if not retry*/
                            if(success){
                                Intent intent = new Intent(MainActivity.this,MainScreenActivity.class);
                                intent.putExtra("username",username);
                                intent.putExtra("asset_id",asset_id);
                                startActivity(intent);
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Asset Registration Failed").setNegativeButton("Retry",null).create().show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                AssetAdditionRequest assetAdditionRequest = new AssetAdditionRequest(username,asset_id,listener); //Make a AssetAdditionRequest object
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);     //make a queue of requests
                queue.add(assetAdditionRequest);        //add assetAdditionRequest to queue of Volley which helps in networking of data

            }
        });

    }



//    public void enter(View view){
//        Intent intent= new Intent(this, MainScreenActivity.class);
//        startActivity(intent);
//    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
