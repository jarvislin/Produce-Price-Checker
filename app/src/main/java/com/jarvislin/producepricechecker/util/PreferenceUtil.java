package com.jarvislin.producepricechecker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by JarvisLin on 14/11/22.
 */
public class PreferenceUtil {

    private static final String PREFERENCE_DATA = "preference_data";
    private static final String UPDATE_DATE = "update_date";

    public static void setUpdateDate(Context context){

        String current = ToolsHelper.getCurrentDate();

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_DATA, 0);
        preferences.edit()
                .putString(UPDATE_DATE, current)
                .commit();

    }
    public static String getUpdateDate(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(UPDATE_DATE, "0");
    }
}
