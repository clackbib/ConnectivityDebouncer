package com.habibokanla.connectivitydebouncer.tracker;

import com.habibokanla.connectivitydebouncer.receiver.NetworkConnectivityPayloadInfo;

/**
 * 2016
 * Created by habibokanla on 21/01/16.
 */
public class ConnectivityChangeEvent {
    public final NetworkConnectivityPayloadInfo info;

    public ConnectivityChangeEvent(NetworkConnectivityPayloadInfo info) {
        this.info = info;
    }
}
