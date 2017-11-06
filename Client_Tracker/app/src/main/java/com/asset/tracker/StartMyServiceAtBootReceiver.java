package com.asset.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Prakhar on 10/23/2017.
 */

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action= intent.getAction();
        if (action.equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")) {
            Intent myIntent=new Intent(context,com.asset.tracker.MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
    }
}