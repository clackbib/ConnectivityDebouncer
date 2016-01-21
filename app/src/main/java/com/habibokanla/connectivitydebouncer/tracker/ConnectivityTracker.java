package com.habibokanla.connectivitydebouncer.tracker;

import android.util.Log;

import com.habibokanla.connectivitydebouncer.app.ConnectivityDebouncerApp;
import com.habibokanla.connectivitydebouncer.receiver.NetworkConnectivityPayloadInfo;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import rx.Observable;

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

    private NetworkConnectivityPayloadInfo info;
    private ConnectivityChangeEvent event;

    public ConnectivityTracker() {
        ConnectivityDebouncerApp.getComponent().inject(this);
        bus.register(this);
        debouncedStream.subscribe(this::updateConnectivityInfo, throwable -> {
            Log.d(TAG, "Error on stream " + throwable.getMessage());
        });
    }

    // We save and update
    public void updateConnectivityInfo(NetworkConnectivityPayloadInfo info) {
        Log.d(TAG, "Debouncing Connectivity Event");
        boolean unchanged = this.info != null && this.info.compareTo(info) == 0;
        this.info = info;
        event = new ConnectivityChangeEvent(info);
        if (!unchanged) {
            bus.post(event);
        }
    }

    @Produce
    public ConnectivityChangeEvent payloadInfoProducer() {
        return event;
    }
}
