package com.habibokanla.connectivitydebouncer.app;

import com.habibokanla.connectivitydebouncer.MainActivity;
import com.habibokanla.connectivitydebouncer.receiver.ConnectivityReceiver;
import com.habibokanla.connectivitydebouncer.tracker.ConnectivityTracker;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * 2016
 * Created by habibokanla on 21/01/16.
 */
@dagger.Component(modules = DebouncerAppModule.class)
@Singleton
@Named
public interface DebouncerAppComponent {
    void inject(ConnectivityReceiver connectivityReceiver);

    void inject(ConnectivityTracker connectivityTracker);

    void inject(MainActivity mainActivity);
}
