package com.jarvislin.producepricechecker.page.Index;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.jarvislin.producepricechecker.BuildConfig;
import com.jarvislin.producepricechecker.model.Fruit;
import com.jarvislin.producepricechecker.SettingsActivity_;
import com.jarvislin.producepricechecker.model.Vegetable;
import com.jarvislin.producepricechecker.page.Presenter;
import com.jarvislin.producepricechecker.page.PriceList.CustomerPath;
import com.jarvislin.producepricechecker.page.PriceList.MerchantPath;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import flow.Flow;
import flow.path.Path;

/**
 * Created by jarvis on 15/9/22.
 */

@EBean
public class IndexPresenter extends Presenter {
    IndexPath path;
    IndexPage page;
    @Pref
    Preferences_ prefs;
    @Override
    protected void init(Path path, View view) {
        this.path = (IndexPath) path;
        this.page = (IndexPage) view;

        if(ToolsHelper.isNetworkAvailable(getContext())) {
            checkLatestVersion();
        }
    }

    public void direct(String page){
        Path path = null;
        switch (page) {
            case "fruit":
                path = prefs.userMode().get().equals(Constants.CUSTOMER) ? new CustomerPath(new Fruit(getContext())) : new MerchantPath(new Fruit(getContext()));
                break;
            case "vegetable":
                path = prefs.userMode().get().equals(Constants.CUSTOMER) ? new CustomerPath(new Vegetable(getContext())) : new MerchantPath(new Vegetable(getContext()));
                break;
            case "settings":
                Intent intent = new Intent(getContext(), SettingsActivity_.class);
                getContext().startActivity(intent);
                break;
            default:
                final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
        }
        if(path != null){
            Flow.get(getContext()).set(path);
        }
    }

    @Background
    public void checkLatestVersion() {
        try {
            Connection.Response res = Jsoup.connect("https://play.google.com/store/apps/details?id=com.jarvislin.producepricechecker").execute();
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
}
