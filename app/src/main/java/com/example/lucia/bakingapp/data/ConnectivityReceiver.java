package com.example.lucia.bakingapp.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener mConnectivityReceiverListener;

    public interface ConnectivityReceiverListener {
        void onConnectivityChanged(boolean isConnected);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction;

        if (mConnectivityReceiverListener != null) {
            intentAction = intent.getAction();
            if (intentAction != null || intentAction.trim().length() != 0) {
                if (intentAction.equals("android.net.conn.CONNECTIVITY_CHANGE") ||
                        intentAction.equals("android.net.wifi.WIFI_STATE_CHANGED")) {
                    mConnectivityReceiverListener.onConnectivityChanged(hasConnectivity(context));
                }
            }
        }
    }

    /**
     * Utility Method to check if device has got connectivity
     * @param context
     * @return TRUE (if connected) / FALSE
     */
    public static boolean hasConnectivity(Context context) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}