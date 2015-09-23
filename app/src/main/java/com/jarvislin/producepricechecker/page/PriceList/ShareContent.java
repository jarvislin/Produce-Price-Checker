package com.jarvislin.producepricechecker.page.PriceList;

import java.util.ArrayList;

import com.jarvislin.producepricechecker.database.Produce;

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
