package com.example.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.myapplication.service.ServiceView;

public class MyBootReceiver extends BroadcastReceiver {

    private static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
    private static final String TAG = "pxwen";
    private Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Log.d(TAG, "onReveive ::" + intent.getAction());
        if (intent.getAction().equals(BOOT_ACTION)) {
            Intent serviceIntent = new Intent(context, ServiceView.class);
            context.startForegroundService(serviceIntent);
            Log.d(TAG, "startForegroundService");
        }
    }

}
