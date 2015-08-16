package com.jarvislin.producepricechecker.activity;

import java.util.ArrayList;

import database.Produce;

/**
 * Created by Jarvis Lin on 2015/8/15.
 */
public interface ShareContent {
    String getMarketNumber();
    String getMarketName();
    String getUpdateDate();
    String getBookmarkCategory();
    void updateDatabase(ArrayList<Produce> produces);
}
