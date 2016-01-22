package com.habibokanla.connectivitydebouncer.app;

import android.content.Context;
import android.net.ConnectivityManager;

import com.habibokanla.connectivitydebouncer.receiver.NetworkConnectivityPayloadInfo;
import com.habibokanla.connectivitydebouncer.tracker.ConnectivityTracker;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Provides;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 2016
 * Created by habibokanla on 21/01/16.
 */
@dagger.Module
public class DebouncerAppModule {

    private static final int CONFIGURABLE_DEBOUNCE_DELAY = 10;
    private ConnectivityDebouncerApp app;

    private Subject<NetworkConnectivityPayloadInfo, NetworkConnectivityPayloadInfo> connectivityStream;
    private Observable<NetworkConnectivityPayloadInfo> debouncedStream;
    private Subject<NetworkConnectivityPayloadInfo, NetworkConnectivityPayloadInfo> userRefreshStream;

    public DebouncerAppModule(ConnectivityDebouncerApp app) {
        this.app = app;
        connectivityStream = new SerializedSubject<>(PublishSubject.create());
        userRefreshStream = new SerializedSubject<>(PublishSubject.create());
        debouncedStream = connectivityStream.share()
                .debounce(CONFIGURABLE_DEBOUNCE_DELAY, TimeUnit.SECONDS)
                .mergeWith(userRefreshStream);
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app;
    }

    @Provides
    @Singleton
    ConnectivityTracker provideConnectivityTracker() {
        return new ConnectivityTracker();
    }

    //Swapping the regular bus for a Main Thread enforcer bus.
    @Provides
    @Singleton
    Bus provideBus() {
        return new MainThreadBus();
    }

    @Provides
    @Singleton
    @Named("debouncedStream")
    Observable<NetworkConnectivityPayloadInfo> provideDebouncedConnectivityStream() {
        return debouncedStream;
    }

    @Provides
    @Singleton
    @Named("userRefreshStream")
    Subject<NetworkConnectivityPayloadInfo, NetworkConnectivityPayloadInfo> provideUserRefreshStream() {
        return userRefreshStream;
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    @Named("connectivityStream")
    Subject<NetworkConnectivityPayloadInfo, NetworkConnectivityPayloadInfo> provideConnectivityStream() {
        return connectivityStream;
    }

}
