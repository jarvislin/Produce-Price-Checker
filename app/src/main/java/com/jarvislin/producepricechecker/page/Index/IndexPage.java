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
import android.widget.FrameLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.BuildConfig;
import com.jarvislin.producepricechecker.SettingsActivity_;
import com.jarvislin.producepricechecker.page.PageListener;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EView;
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
    public void onCreateOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {

    }

    @AfterViews
    protected void init() {
        if (prefs.versionCode().get() != BuildConfig.VERSION_CODE) {
            showNews();
        }
        if (prefs.needToUpdate().get() && ToolsHelper.isNetworkAvailable(getContext())) {
            showUpdate();
        }
    }

    @UiThread
    void showNews() {
        ToolsHelper.showDialog(getContext(), "新功能",
                "1. 自動偵測新版本。\n" +
                        "2. 修改版面。");
        prefs.versionCode().put(BuildConfig.VERSION_CODE);
    }


    @Click
    protected void fruit() {
        GoogleAnalyticsSender.getInstance(getContext()).send("click_fruit");
        presenter.direct("fruit");
    }

    @Click
    public void vegetable() {
        GoogleAnalyticsSender.getInstance(getContext()).send("click_vegetable");
        presenter.direct("vegetable");
    }

    @Click
    public void settings() {
        GoogleAnalyticsSender.getInstance(getContext()).send("click_settings");
        presenter.direct("settings");
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
        if (which == DialogInterface.BUTTON_POSITIVE) {
            presenter.direct("update_app");
        }
    }


}
