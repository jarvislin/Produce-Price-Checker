package com.jarvislin.producepricechecker;

import com.jarvislin.producepricechecker.database.Produce;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jarvis Lin on 2015/8/15.
 */
public interface ShareContent extends Serializable{
    String getMarketNumber();
    String getMarketName();
    String getUpdateDate(String marketNumber);
    String getBookmarkCategory();
    void updateDatabase(ArrayList<Produce> produces);
    String getCategory();
}
