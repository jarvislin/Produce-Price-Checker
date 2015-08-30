package com.jarvislin.producepricechecker.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.BuildConfig;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;


@EActivity(R.layout.activity_index)
public class IndexActivity extends AppCompatActivity implements DialogInterface.OnClickListener{

    @Pref
    Preferences_ prefs;
    @RestService
    ApiClient client;
    @ViewById
    Button vegetable;
    @ViewById
    Button fruit;
    @ViewById
    Button settings;

    @AfterViews
    protected void init(){
        showNews();
        if(prefs.needToUpdate().get()){
            showUpdate();
        }
        if(ToolsHelper.isNetworkAvailable(this)) {
            checkLatestVersion();
        }
    }

    @UiThread
    void showNews() {
        if(prefs.versionCode().get() != BuildConfig.VERSION_CODE){
            ToolsHelper.showDialog(this, "新功能",
                    "1. 自動偵測新版本。\n" +
                            "2. 修改版面。");
            prefs.versionCode().put(BuildConfig.VERSION_CODE);
        }
    }

    @Background
    protected void checkLatestVersion() {
        try {
            Connection.Response res = null;
            res = Jsoup.connect("https://play.google.com/store/apps/details?id=com.jarvislin.producepricechecker").execute();
            String verName = res.parse().select("div[itemprop=softwareVersion]").first().text().trim();
            if(!TextUtils.isEmpty(verName)) {
                if(!BuildConfig.VERSION_NAME.equals(verName)) {
                    prefs.needToUpdate().put(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Click
    protected void fruit() {
        GoogleAnalyticsSender.getInstance(this).send("click_fruit");
        Intent intent = new Intent(this, (prefs.userMode().get().equals(Constants.CUSTOMER) ? CustomerActivity_.class : MerchantActivity_.class));
        intent.putExtra("category", Constants.FRUIT);
        startActivity(intent);
    }

    @Click
    public void vegetable(View view) {
        GoogleAnalyticsSender.getInstance(this).send("click_vegetable");
        Intent intent = new Intent(this, (prefs.userMode().get().equals(Constants.CUSTOMER) ? CustomerActivity_.class : MerchantActivity_.class));
        intent.putExtra("category", Constants.VEGETABLE);
        IndexActivity.this.startActivity(intent);
    }

    @Click
    public void settings(View view) {
        GoogleAnalyticsSender.getInstance(this).send("click_settings");
        Intent intent = new Intent(IndexActivity.this, SettingsActivity_.class);

//        Intent intent = new Intent(IndexActivity.this, HistoryActivity_.class);

//        Intent intent = new Intent(IndexActivity.this, ChartActivity_.class);

        IndexActivity.this.startActivity(intent);
    }

    @UiThread
    protected void showUpdate() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("發現新版本");
        dialog.setMessage("目前蔬果行情站版本過舊，請問要進行更新嗎？");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "馬上更新", this);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "現在不要", this);
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }
}
