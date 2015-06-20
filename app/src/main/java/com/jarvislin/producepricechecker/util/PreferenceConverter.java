package com.jarvislin.producepricechecker.util;

import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Arrays;

import database.Produce;

/**
 * Created by Jarvis Lin on 2015/6/19.
 */
@EBean(scope = EBean.Scope.Singleton)
public class PreferenceConverter {
    @Pref
    Preferences_ prefs;

    public ArrayList<Produce> getBookmarks() {
        Gson gson = new Gson();
        String bookmarks = prefs.bookmarks().get();
        Produce[] produces = gson.fromJson(bookmarks, Produce[].class);
        if(produces.length > 0)
            return new ArrayList<Produce>(Arrays.asList(produces));
        else
            return new ArrayList<Produce>();
    }
}
