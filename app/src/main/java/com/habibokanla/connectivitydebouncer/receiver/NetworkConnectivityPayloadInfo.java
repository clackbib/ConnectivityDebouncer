package com.habibokanla.connectivitydebouncer.receiver;

import android.support.annotation.NonNull;

/**
 * 2016
 * Created by habibokanla on 21/01/16.
 */
public class NetworkConnectivityPayloadInfo implements Comparable<NetworkConnectivityPayloadInfo> {
    public boolean isConnected;
    public String networkType;
    public boolean explicitRequest;

    public NetworkConnectivityPayloadInfo() {
        this.networkType = "undetected";
    }

    @Override
    public int compareTo(@NonNull NetworkConnectivityPayloadInfo other) {
        return Boolean.valueOf(isConnected).compareTo(other.isConnected);
    }
}
