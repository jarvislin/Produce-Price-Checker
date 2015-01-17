package com.jarvislin.producepricechecker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jarvis Lin on 2014/10/13.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String HOMEPAGE = "http://jarvislin.com";
    private static final String SOURCE_CODE = "https://github.com/jarvislin/Produce-Price-Checker";
    private static final String PLAY_STORE = "http://play.google.com/store/apps/details?id=com.jarvislin.producepricechecker";
    private static final ArrayList<String> PREFERENCE_ITEM = new ArrayList<String>(){{
        add(0, "about");
        add(1, "rating");
        add(2, "visit");
        add(3, "profit");
    }};

    private EditText mLowProfit;
    private EditText mHighProfit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);
        initPreference();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void initPreference() {
        for(int i = 0 ; i < PREFERENCE_ITEM.size() ; i ++){
            Preference preference = (Preference) findPreference(PREFERENCE_ITEM.get(i));
            preference.setOnPreferenceClickListener(clickPref(i, getActivity()));
        }
    }

    private Preference.OnPreferenceClickListener clickPref(final int key, final Context context){
        return new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                switch (key) {
                    case 0:
                        setAbout(context);
                        break;
                    case 1:
                        openUrl(PLAY_STORE);
                        break;
                    case 2:
                        openUrl(HOMEPAGE);
                        break;
                    case 3:
                        showProfit();
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
    }

    private void showProfit() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String[] profit = prefs.getString("profit", "30,50").split(",");

        final Dialog dialog = new Dialog(getActivity(), R.style.alertDialog);
        dialog.setContentView(R.layout.dialog_profit);

        Button cancel = (Button)dialog.findViewById(R.id.profit_dismiss);
        Button submit = (Button)dialog.findViewById(R.id.profit_submit);
        cancel.setOnClickListener(clickCancel(dialog));
        submit.setOnClickListener(clickSubmit(dialog));

        mLowProfit = (EditText)dialog.findViewById(R.id.profit_low);
        mHighProfit = (EditText)dialog.findViewById(R.id.profit_high);
        mLowProfit.setText(profit[0]);
        mHighProfit.setText(profit[1]);



        dialog.show();

    }

    private View.OnClickListener clickCancel(final Dialog dialog){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        };
    }

    private View.OnClickListener clickSubmit(final Dialog dialog){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String low = mLowProfit.getText().toString();
                String high = mHighProfit.getText().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                prefs.edit().putString("profit",low + "," + high).commit();

                if(dialog.isShowing())
                    dialog.dismiss();
            }
        };
    }

    private void setAbout(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    public void openUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Intent chooser = Intent.createChooser(intent, getString(R.string.choose_service));
        startActivity(chooser);
    }


}