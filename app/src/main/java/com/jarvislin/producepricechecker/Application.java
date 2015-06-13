package com.jarvislin.producepricechecker;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Jarvis Lin on 2015/6/13.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
