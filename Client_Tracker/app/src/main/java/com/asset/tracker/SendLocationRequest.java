package com.asset.tracker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prakhar on 11/3/2017.
 */

public class SendLocationRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL="https://grovelling-speeders.000webhostapp.com/Send_Location.php"; //Location of php file which will Send Location to the Database
    private Map<String,String> params;      //Map of Parameters to send to the php file

    /*SendLocationRequest function which will be called in MainScreenActivity to send location to the server and store it in database*/
    public SendLocationRequest(int asset_list_id,double latitude, double longitude, String date_time, Response.Listener<String> listener){
        super(Request.Method.POST, LOGIN_REQUEST_URL,listener,null);
        params= new HashMap<>();
        params.put("asset_list_id",asset_list_id+"");
        params.put("latitude",latitude+"");
        params.put("longitude",longitude+"");
        params.put("date_time",date_time);

    }

    @Override
    public Map<String,String> getParams(){
        return params;
    }

}
