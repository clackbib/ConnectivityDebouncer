package com.habibokanla.connectivitydebouncer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.habibokanla.connectivitydebouncer.app.ConnectivityDebouncerApp;
import com.habibokanla.connectivitydebouncer.tracker.ConnectivityTracker;

import javax.inject.Inject;
import javax.inject.Named;

import rx.subjects.Subject;

/**
 * 2016
 * Created by habibokanla on 21/01/16.
 */
public class ConnectivityReceiver extends BroadcastReceiver {
    @Inject
    ConnectivityTracker tracker;
    @Inject
    @Named("connectivityStream")
    Subject<NetworkConnectivityPayloadInfo, NetworkConnectivityPayloadInfo> connectivityStream;

    private static final String TAG = "ConnectivityManager";
    //A subject is essentially similar to a bus, with information traveling up and down,

    public ConnectivityReceiver() {
        ConnectivityDebouncerApp.getComponent().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Network connectivity change");
        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            //Build a Network Connectivity info payload.
            NetworkConnectivityPayloadInfo info = new NetworkConnectivityPayloadInfo();
            if (ni != null && ni.isConnectedOrConnecting()) {
                info.isConnected = true;
                info.networkType = ni.getTypeName();
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                info.isConnected = false;
            }
            //Send the payload up a stream.
            connectivityStream.onNext(info);
        }
    }
}
