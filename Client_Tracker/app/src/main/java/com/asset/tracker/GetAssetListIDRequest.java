package com.asset.tracker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prakhar on 11/3/2017.
 */

public class GetAssetListIDRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL="https://grovelling-speeders.000webhostapp.com/Get_Asset_List_ID.php";    //Location of php file which will get Asset List ID from the Database
    private Map<String,String> params;      //Map of Parameters to send to the php file

    /*GetAssetListIDRequest function which will be called in MainScreenActivity to send username and asset id to the server and store it in database and get its corresponding AssetListID*/

    public GetAssetListIDRequest(String username, String asset_id, Response.Listener<String> listener){
        super(Request.Method.POST, LOGIN_REQUEST_URL,listener,null);
        params= new HashMap<>();
        params.put("username",username);    //put username to params Hashmap
        params.put("asset_id",asset_id);    //put asset id to params Hashmap

    }

    @Override
    public Map<String,String> getParams(){
        return params;
    }

}
