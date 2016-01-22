package com.habibokanla.connectivitydebouncer.tracker;

import com.habibokanla.connectivitydebouncer.app.ConnectivityDebouncerApp;
import com.habibokanla.connectivitydebouncer.receiver.NetworkConnectivityPayloadInfo;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.Subject;

/**
 * 2016
 * Created by habibokanla on 21/01/16.
 */
@Singleton
public class ConnectivityTracker {

    private static final String TAG = "ConnectivityTracker";
    @Inject
    Bus bus;
    @Inject
    @Named("debouncedStream")
    Observable<NetworkConnectivityPayloadInfo> debouncedStream;
    @Inject
    @Named("userRefreshStream")
    Subject<NetworkConnectivityPayloadInfo, NetworkConnectivityPayloadInfo> userRefreshStream;
    @Inject
    ConnectivityManager connectivityManager;

    private NetworkConnectivityPayloadInfo info;
    private ConnectivityChangeEvent event;

    public ConnectivityTracker() {
        ConnectivityDebouncerApp.getComponent().inject(this);
        bus.register(this);
        this.info = getConnectivityInfo(false);
        debouncedStream.subscribe(this::updateConnectivityInfo, throwable -> {
            Log.d(TAG, "Error on stream " + throwable.getMessage());
        });
    }

    // We save and update
    public void updateConnectivityInfo(NetworkConnectivityPayloadInfo info) {
        Log.d(TAG, "Debouncing Connectivity Event");
        boolean unchanged = this.info != null && this.info.compareTo(info) == 0;
        boolean explicitRequest = info != null && info.explicitRequest;
        this.info = info;
        event = new ConnectivityChangeEvent(info);
        if (!unchanged || explicitRequest) {
            bus.post(event);
        }
    }

    public void requestSynchronousConnectivityUpdate() {
        userRefreshStream.onNext(getConnectivityInfo(true));
    }

    private NetworkConnectivityPayloadInfo getConnectivityInfo(boolean explicitRequest) {
        final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        NetworkConnectivityPayloadInfo info = new NetworkConnectivityPayloadInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            info.isConnected = true;
            info.networkType = ni.getTypeName();
        } else {
            info.isConnected = false;
        }
        info.explicitRequest = explicitRequest;
        return info;
    }

    @Produce
    public ConnectivityChangeEvent payloadInfoProducer() {
        return event;
    }
}
