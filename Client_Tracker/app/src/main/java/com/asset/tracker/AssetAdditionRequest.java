package com.asset.tracker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prakhar on 11/3/2017.
 */

public class AssetAdditionRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL="https://grovelling-speeders.000webhostapp.com/Send_Data.php"; //Location of php file which will add assets to the Database
    private Map<String,String> params;

    public AssetAdditionRequest(String username, String asset_id, Response.Listener<String> listener){
        super(Request.Method.POST, LOGIN_REQUEST_URL,listener,null);
        params= new HashMap<>();
        params.put("username",username);
        params.put("asset_id",asset_id);

    }

    @Override
    public Map<String,String> getParams(){
        return params;
    }

}
