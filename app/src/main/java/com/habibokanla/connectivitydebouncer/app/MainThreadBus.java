package com.habibokanla.connectivitydebouncer.app;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

/**
 * 2016
 * Created by habibokanla on 21/01/16.
 */
@Singleton
public class MainThreadBus extends Bus {
    private final Handler mainThread = new Handler(Looper.getMainLooper());

    @Override
    public void post(Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mainThread.post(() -> MainThreadBus.super.post(event));
        }

    }
}
