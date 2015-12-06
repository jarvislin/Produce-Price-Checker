package com.jarvislin.producepricechecker.page.Index;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.Menu;
import android.widget.FrameLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.BuildConfig;
import com.jarvislin.producepricechecker.page.PageListener;
import com.jarvislin.producepricechecker.path.HandlesBack;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.sharedpreferences.Pref;

import flow.path.Path;

/**
 * Created by jarvis on 15/9/22.
 */
@EView
public class IndexPage extends FrameLayout implements PageListener, HandlesBack {
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
            prefs.versionCode().put(BuildConfig.VERSION_CODE);
            prefs.needToUpdate().put(false);
        }
        if (prefs.needToUpdate().get() && ToolsHelper.isNetworkAvailable(getContext())) {
            showUpdate();
        }
    }

    @UiThread
    void showNews() {
        ToolsHelper.showDialog(getContext(), "新功能",
                "1. 歷史價格查詢。\n" +
                        "2. 版面調整。");
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
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "馬上更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.direct("update_app");
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "現在不要", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    @Override
    public boolean onBackPressed() {
        int count = prefs.openAppCount().get();
        count++;
        prefs.openAppCount().put(count);
        if (count > 15 && !prefs.hasShownRating().get() && ToolsHelper.isNetworkAvailable(getContext())) {
            // show dialog
            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
            dialog.setTitle("系統訊息");
            dialog.setMessage("請問在使用過程中還滿意嗎？");
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "滿意", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prefs.hasShownRating().put(true);
                    showRatingDialog();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "不滿意", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prefs.hasShownRating().put(true);
                    showContactDialog();
                }
            });
            dialog.show();

            return true;
        }
        return false;
    }

    private void showContactDialog() {
//        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
//        dialog.setTitle("系統訊息");
//        dialog.setMessage("很抱歉沒達到您的要求，請問要跟作者提出建議嗎？");
//        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "是", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ToolsHelper.openUrl(getContext(), "http://jarvislin.com/contact/");
//            }
//        });
//        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//        dialog.show();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:admin@jarvisllin.com?subject=" + "提供建議" + "&body=" + "對於蔬果行情站不是很滿意，我認為這麼做會更好：\n");
        intent.setData(data);
        getContext().startActivity(Intent.createChooser(intent, "提供建議給開發人員"));
    }

    private void showRatingDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle("系統訊息");
        dialog.setMessage("謝謝您，請問可以給行情站一個好評分表示支持嗎？");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "可以", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToolsHelper.rating(getContext());
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "不要", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
}
