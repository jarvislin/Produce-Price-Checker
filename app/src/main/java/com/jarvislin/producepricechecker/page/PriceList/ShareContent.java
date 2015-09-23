package com.jarvislin.producepricechecker.page.PriceList;

import com.jarvislin.producepricechecker.database.Produce;

import java.util.ArrayList;

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
