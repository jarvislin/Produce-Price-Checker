package com.jarvislin.producepricechecker;

import com.facebook.stetho.Stetho;

import timber.log.Timber;

/**
 * Created by Jarvis Lin on 2015/6/13.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
        }
    }
}
