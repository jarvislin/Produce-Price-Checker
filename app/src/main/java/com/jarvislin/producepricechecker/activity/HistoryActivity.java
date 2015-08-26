package com.jarvislin.producepricechecker.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jarvislin.producepricechecker.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Jarvis Lin on 2015/8/23.
 */
@EActivity(R.layout.activity_history)
public class HistoryActivity extends AppCompatActivity {
    @ViewById
    AdView adView;

    @AfterViews
    void init(){
        final TelephonyManager tm =(TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        String deviceid = tm.getDeviceId();
        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice(deviceid);
        AdRequest adRequest =builder.build();
        adView.loadAd(adRequest);
    }
}
