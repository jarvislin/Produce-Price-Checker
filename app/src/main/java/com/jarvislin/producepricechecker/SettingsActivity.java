package com.jarvislin.producepricechecker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.Preferences_;

import org.androidannotations.annotations.AfterPreferences;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.PreferenceByKey;
import org.androidannotations.annotations.PreferenceChange;
import org.androidannotations.annotations.PreferenceClick;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by Jarvis Lin on 2015/6/19.
 */
@EActivity
public class SettingsActivity extends PreferenceActivity {

    @Pref
    Preferences_ prefs;

    @RestService
    ApiClient client;

    private static final String PREF_NAME = "Preferences";
    private static final String PLAY_STORE = "http://play.google.com/store/apps/details?id=com.jarvislin.producepricechecker";
    private static final String HOMEPAGE = "http://jarvislin.com";
    private static final String SOURCE_CODE = "https://github.com/jarvislin/Produce-Price-Checker";

    private EditText lowProfit;
    private EditText highProfit;

    @PreferenceByKey(R.string.pref_key_profit)
    Preference profit;

    @PreferenceByKey(R.string.pref_key_unit)
    Preference unit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(PREF_NAME);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @AfterPreferences
    void initPrefs() {
        setProfitSummary();
        setUnitSummary();

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar_settings, root, false);
        root.addView(bar, 0); // insert at top
    }



    @PreferenceClick(R.string.pref_key_rating)
    void rating() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @PreferenceChange(R.string.pref_key_fruit_market)
    void fruitMarketChanged() {
        prefs.edit()
                .fruitUpdateDate().put("")
                .apply();
    }

    @PreferenceChange(R.string.pref_key_vegetable_market)
    void vegetableMarketChanged() {
        prefs.edit()
                .vegetableUpdateDate().put("")
                .apply();
    }

    @PreferenceChange(R.string.pref_key_is_customer)
    void roleChanged(boolean isCustomer, Preference preference) {
        String role = isCustomer ? Constants.CUSTOMER : Constants.MERCHANT;
        prefs.userMode().put(role);
    }

    @PreferenceChange(R.string.pref_key_unit)
    void unitChanged(String unit, Preference preference){
        float prefUnit = Float.valueOf(unit);
        prefs.unit().put(prefUnit);
        setUnitSummary();
    }

    @PreferenceClick(R.string.pref_key_about)
    void about() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.about));
        builder.setMessage(getString(R.string.about_app));
        builder.setNegativeButton(getString(R.string.back), null);
        builder.setPositiveButton(getString(R.string.get_source_code), new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openUrl(SOURCE_CODE);
            }
        });
        builder.show();
    }

    @PreferenceClick(R.string.pref_key_visit)
    void visit() {
        openUrl(HOMEPAGE);
    }

    @PreferenceClick(R.string.pref_key_profit)
    void profit() {
        final Dialog dialog = new Dialog(this, R.style.alertDialog);
        dialog.setContentView(R.layout.dialog_profit);
        Button submit = (Button) dialog.findViewById(R.id.profit_submit);
        submit.setOnClickListener(clickSubmit(dialog));
        lowProfit = (EditText) dialog.findViewById(R.id.profit_low);
        highProfit = (EditText) dialog.findViewById(R.id.profit_high);
        lowProfit.setText(String.valueOf(Math.round(prefs.lowProfit().get() * 100)));
        highProfit.setText(String.valueOf(Math.round(prefs.hightProfit().get() * 100)));
        lowProfit.setSelection(lowProfit.getText().toString().length());
        dialog.show();
    }

    private View.OnClickListener clickSubmit(final Dialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = getFormatError();
                if (TextUtils.isEmpty(error)) {
                    float low = Float.valueOf(lowProfit.getText().toString()) / 100;
                    float high = Float.valueOf(highProfit.getText().toString()) / 100;
                    prefs.edit()
                            .lowProfit().put(low)
                            .hightProfit().put(high)
                            .apply();
                    setProfitSummary();
                    if (dialog.isShowing())
                        dialog.dismiss();
                } else {
                    Toast.makeText(SettingsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private String getFormatError() {
        if (lowProfit.getText().toString().isEmpty() || highProfit.getText().toString().isEmpty()) {
            return "欄位不可空白";
        }
        float low = Float.valueOf(lowProfit.getText().toString()) / 100;
        float high = Float.valueOf(highProfit.getText().toString()) / 100;
        if (low > high) {
            return "最低利潤不可大於最高利潤";
        }
        return null;
    }

    public void openUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Intent chooser = Intent.createChooser(intent, getString(R.string.choose_service));
        startActivity(chooser);
    }

    private void setProfitSummary() {
        profit.setSummary(String.valueOf(Math.round(prefs.lowProfit().get() * 100)) + "% ~ " + String.valueOf(Math.round(prefs.hightProfit().get() * 100)) + "%");
    }

    private void setUnitSummary() {
        unit.setSummary(prefs.unit().get() < 1 ? "台斤" : "公斤");
    }

}
