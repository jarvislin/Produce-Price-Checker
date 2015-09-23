package com.jarvislin.producepricechecker.page.Index;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.BuildConfig;
import com.jarvislin.producepricechecker.SettingsActivity_;
import com.jarvislin.producepricechecker.page.PageListener;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import flow.path.Path;

/**
 * Created by jarvis on 15/9/22.
 */
@EView
public class IndexPage extends FrameLayout implements PageListener, DialogInterface.OnClickListener {
    @Pref
    Preferences_ prefs;

    @Bean
    protected IndexPresenter presenter;

    public IndexPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
        componentHelper.hideToolbar();
    }

    @Override
    public void onPrepareOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {

    }

    @AfterViews
    protected void init(){
        showNews();
        if(prefs.needToUpdate().get()){
            showUpdate();
        }
        if(ToolsHelper.isNetworkAvailable(getContext())) {
            checkLatestVersion();
        }
    }

    @UiThread
    void showNews() {
        if(prefs.versionCode().get() != BuildConfig.VERSION_CODE){
            ToolsHelper.showDialog(getContext(), "新功能",
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
        GoogleAnalyticsSender.getInstance(getContext()).send("click_fruit");
//        Intent intent = new Intent(this, (prefs.userMode().get().equals(Constants.CUSTOMER) ? CustomerActivity_.class : MerchantActivity_.class));
//        intent.putExtra("category", Constants.FRUIT);
//        startActivity(intent);
    }

    @Click
    public void vegetable() {
        GoogleAnalyticsSender.getInstance(getContext()).send("click_vegetable");
//        Intent intent = new Intent(this, (prefs.userMode().get().equals(Constants.CUSTOMER) ? CustomerActivity_.class : MerchantActivity_.class));
//        intent.putExtra("category", Constants.VEGETABLE);
//        IndexActivity.this.startActivity(intent);
    }

    @Click
    public void settings() {
        GoogleAnalyticsSender.getInstance(getContext()).send("click_settings");
        Intent intent = new Intent(getContext(), SettingsActivity_.class);
        getContext().startActivity(intent);
    }

    @UiThread
    protected void showUpdate() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle("發現新版本");
        dialog.setMessage("目前蔬果行情站版本過舊，請問要進行更新嗎？");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "馬上更新", this);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "現在不要", this);
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {
            final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
            try {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }


}
