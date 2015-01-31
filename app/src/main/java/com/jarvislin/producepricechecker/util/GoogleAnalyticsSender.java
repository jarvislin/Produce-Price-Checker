package com.jarvislin.producepricechecker.util;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Jarvis Lin on 2014/12/8.
 */
public class GoogleAnalyticsSender {
    private static final String PROPERTY_ID = "";
    private static GoogleAnalyticsSender mSender;
    private static Tracker mTracker;

    public static GoogleAnalyticsSender getInstance(Context context){
        if(null == mSender){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            mTracker = analytics.newTracker(PROPERTY_ID);
            mTracker.enableAdvertisingIdCollection(true);
            mSender = new GoogleAnalyticsSender();
        }
        return mSender;
    }

    private GoogleAnalyticsSender(){}

    public void send(String path){
        mTracker.setScreenName(path);
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

}
