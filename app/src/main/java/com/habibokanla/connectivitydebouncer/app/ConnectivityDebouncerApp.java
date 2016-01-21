package com.habibokanla.connectivitydebouncer.app;

import android.app.Application;

/**
 * 2016
 * Created by habibokanla on 21/01/16.
 */
public class ConnectivityDebouncerApp extends Application {

    private static ConnectivityDebouncerApp sApp;
    private DebouncerAppComponent debouncerAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
    }

    //Dagger Stuff
    private void initComponent() {
        debouncerAppComponent = DaggerDebouncerAppComponent.builder()
                .debouncerAppModule(new DebouncerAppModule(this))
                .build();
    }

    public ConnectivityDebouncerApp() {
        sApp = this;
    }


    public static DebouncerAppComponent getComponent() {
        return sApp.debouncerAppComponent;
    }
}
