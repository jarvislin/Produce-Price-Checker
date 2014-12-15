package com.jarvislin.producepricechecker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.jarvislin.producepricechecker.ProduceData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by JarvisLin on 14/11/22.
 */
public class PreferenceUtil {

    private static final String PREFERENCE_DATA = "preference_data";
    private static final String LAST_UPDATED_DATE = "last_updated_date";
    private static final String BOOKMARK_LIST = "bookmark_list";
    private static final String BOOKMARK_LIST_FRUIT = "bookmark_list_fruit";
    private static final String BOOKMARK_LIST_VEGETABLE = "bookmark_list_vegetable";
    private static final int FRUIT = -1;
    private static final int VEGETABLE = 1;

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

    public static boolean isCustomerMode(Context context){
//        Log.d(TAG, "CustomerMode = " + String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("role", false)));
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("role", false);
    }

    public static boolean isBookmark(Context context, int type, ProduceData data) {
        ArrayList<ProduceData> bookmarkList = PreferenceUtil.getBookmarkList(context, type);
        for(int i = 0 ; i < bookmarkList.size() ; i++){
            if(bookmarkList.get(i).getType().equals(data.getType()) && bookmarkList.get(i).getName().equals(data.getName())){
                return true;
            }
        }
        return false;
    }

    public static void saveBookmarks(Context context, ArrayList<ProduceData> list, int type) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFERENCE_DATA, 0);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonBookmarks = gson.toJson(list);
        if(type == FRUIT)
            editor.putString(BOOKMARK_LIST_FRUIT, jsonBookmarks).commit();
        else
            editor.putString(BOOKMARK_LIST_VEGETABLE, jsonBookmarks).commit();

    }

    public static void addBookmark(Context context, ArrayList<ProduceData> list, ProduceData object, int type) {
        list.add(object);
        saveBookmarks(context, list, type);
    }

    public static void updateBookmarks(Context context, ArrayList<ProduceData> list, ProduceData object, int position, int type) {
        list.set(position, object);
        saveBookmarks(context, list, type);
    }

    public static void removeBookmark(Context context, ArrayList<ProduceData> list, int position, int type) {
        list.remove(position);
        saveBookmarks(context, list, type);
    }

    public static void removeBookmark(Context context, ArrayList<ProduceData> list, ProduceData object, int type) {
        for(int i = 0 ; i < list.size() ; i++){
            if(list.get(i).getType().equals(object.getType()) && list.get(i).getName().equals(object.getName())){
                list.remove(i);
                break;
            }
        }
        saveBookmarks(context, list, type);
    }

    public static ArrayList<ProduceData> getBookmarkList(Context context, int type) {

        SharedPreferences settings;
        String key = (type == FRUIT) ? BOOKMARK_LIST_FRUIT : BOOKMARK_LIST_VEGETABLE;

        settings = context.getSharedPreferences(PREFERENCE_DATA, 0);
        if (settings.contains(key)) {
            String jsonBookmarks = settings.getString(key, null);
            Gson gson = new Gson();
            ProduceData[] data = gson.fromJson(jsonBookmarks, ProduceData[].class);

            return new ArrayList<ProduceData>(Arrays.asList(data));
        } else
            return new ArrayList<ProduceData>();
    }
}
