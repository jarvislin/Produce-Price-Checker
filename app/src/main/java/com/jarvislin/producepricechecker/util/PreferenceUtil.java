package com.jarvislin.producepricechecker.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.jarvislin.producepricechecker.ProduceData;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by JarvisLin on 14/11/22.
 */
public class PreferenceUtil {

    private static final String PREFERENCE_DATA = "preference_data";
    private static final String FRUIT_UPDATE_TIME = "fruit_updated_time";
    private static final String VEGETABLE_UPDATE_TIME = "vegetable_updated_time";
    private static final String BOOKMARK_LIST = "bookmark_list";
    private static final String BOOKMARK_LIST_FRUIT = "bookmark_list_fruit";
    private static final String BOOKMARK_LIST_VEGETABLE = "bookmark_list_vegetable";

    public static void setUpdateTime(Context context, String kind) {
        String current = ToolsHelper.getCurrentDate();
        String key = (kind.equals(Constants.FRUIT)) ? FRUIT_UPDATE_TIME : VEGETABLE_UPDATE_TIME;
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_DATA, 0);
        preferences.edit()
                .putString(key, current)
                .apply();
    }

    public static String getUpdateTime(Context context, String kind) {
        String key = (kind.equals(Constants.FRUIT)) ? FRUIT_UPDATE_TIME : VEGETABLE_UPDATE_TIME;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_DATA, 0);
        return settings.getString(key, "0");
    }


    public static boolean isCustomerMode(Context context) {
//        Log.d(TAG, "CustomerMode = " + String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("role", false)));
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("role", false);
    }

    public static boolean isBookmark(Context context, String type, ProduceData data) {
        ArrayList<ProduceData> bookmarkList = PreferenceUtil.getBookmarkList(context, type);
        for (int i = 0; i < bookmarkList.size(); i++) {
            if (bookmarkList.get(i).getType().equals(data.getType()) && bookmarkList.get(i).getName().equals(data.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void saveBookmarks(Context context, ArrayList<ProduceData> list, String type) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFERENCE_DATA, 0);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonBookmarks = gson.toJson(list);
        if (type.equals(Constants.FRUIT))
            editor.putString(BOOKMARK_LIST_FRUIT, jsonBookmarks).apply();
        else
            editor.putString(BOOKMARK_LIST_VEGETABLE, jsonBookmarks).apply();

    }

    public static void addBookmark(Context context, ArrayList<ProduceData> list, ProduceData object, String type) {
        list.add(object);
        saveBookmarks(context, list, type);
    }

    public static void updateBookmarks(Context context, ArrayList<ProduceData> list, ProduceData object, int position, String type) {
        list.set(position, object);
        saveBookmarks(context, list, type);
    }

    public static void removeBookmark(Context context, ArrayList<ProduceData> list, int position, String type) {
        list.remove(position);
        saveBookmarks(context, list, type);
    }

    public static void removeBookmark(Context context, ArrayList<ProduceData> list, ProduceData object, String type) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals(object.getType()) && list.get(i).getName().equals(object.getName())) {
                list.remove(i);
                break;
            }
        }
        saveBookmarks(context, list, type);
    }

    public static ArrayList<ProduceData> getBookmarkList(Context context, String type) {

        SharedPreferences settings;
        String key = (type.equals(Constants.FRUIT)) ? BOOKMARK_LIST_FRUIT : BOOKMARK_LIST_VEGETABLE;

        settings = context.getSharedPreferences(PREFERENCE_DATA, 0);
        if (settings.contains(key)) {
            String jsonBookmarks = settings.getString(key, null);
            Gson gson = new Gson();
            ProduceData[] data = gson.fromJson(jsonBookmarks, ProduceData[].class);

            return new ArrayList<ProduceData>(Arrays.asList(data));
        } else
            return new ArrayList<ProduceData>();
    }

    public static float[] getProfitRange(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String[] profit = prefs.getString("profit", "30,50").split(",");

        return new float[]{(Float.valueOf(profit[0]) + 100) / 100, (Float.valueOf(profit[1]) + 100) / 100};
    }

    public static float getUnit(Context context) {
        String temp = PreferenceManager.getDefaultSharedPreferences(context).getString("unit", "1.0");
        return Float.valueOf(temp);
    }

    public static void setUnit(Context context, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("unit", value).commit();
    }

    public static void resetUpdateTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_DATA, 0);
        preferences.edit().remove(FRUIT_UPDATE_TIME).apply();
        preferences.edit().remove(VEGETABLE_UPDATE_TIME).apply();
    }
}
