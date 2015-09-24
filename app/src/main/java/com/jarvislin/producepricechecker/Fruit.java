package com.jarvislin.producepricechecker;

import android.content.Context;

import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Migration01;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.Preferences_;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jarvis Lin on 2015/8/15.
 */
public class Fruit extends ProduceData {
    private Preferences_ prefs;

    public Fruit(Context context){
        prefs = new Preferences_(context);
    }

    @Override
    public int getNumbersResId() {
        return R.array.pref_fruit_market_values;
    }

    @Override
    protected int getMarketsResId() {
        return R.array.pref_fruit_market_titles;
    }

    @Override
    public String getMarketNumber() {
        return prefs.fruitMarketList().get();
    }

    @Override
    public String getBookmarkCategory() {
        return Constants.FRUIT_BOOKMARK;
    }

    @Override
    public String getCategory() {
        return Constants.FRUIT;
    }
}
