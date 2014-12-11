package com.jarvislin.producepricechecker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.jarvislin.producepricechecker.ProduceData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JarvisLin on 14/11/22.
 */
public class PreferenceUtil {

    private static final String PREFERENCE_DATA = "preference_data";
    private static final String LAST_UPDATED_DATE = "last_updated_date";
    private final static String BOOKMARK_LIST = "bookmark_list";

    public static void updateLastDate(Context context){

        String current = ToolsHelper.getCurrentDate();

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_DATA, 0);
        preferences.edit()
                .putString(LAST_UPDATED_DATE, current)
                .commit();

    }
    public static String getLastUpdatedDate(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LAST_UPDATED_DATE, "0");
    }

    public static void saveBookmarks(Context context, List<ProduceData> list) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFERENCE_DATA, 0);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonBookmarks = gson.toJson(list);
        editor.putString(BOOKMARK_LIST, jsonBookmarks).commit();
    }
}
