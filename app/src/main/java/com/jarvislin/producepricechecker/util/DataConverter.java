package com.jarvislin.producepricechecker.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jarvislin.producepricechecker.ProduceData;

import java.util.List;

/**
 * Created by Jarvis Lin on 2014/12/8.
 */
public class DataConverter {

    private final static String PrefrenceTag = "preferences";
    private final static String FAVORITE_LIST = "favorite_list";

    public static void saveFavorites(Context context, List<ProduceData> list) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PrefrenceTag, 0);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(list);
        editor.putString(FAVORITE_LIST, jsonFavorites).commit();
    }
}
