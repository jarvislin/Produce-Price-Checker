package com.jarvislin.producepricechecker.model;

import android.content.Context;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.Preferences_;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jarvis Lin on 2015/8/15.
 */
public class Vegetable extends ProduceData {
    public Vegetable(String number){
        super();
        super.number = number;
    }
    @Override
    public int getNumbersResId() {
        return R.array.pref_vegetable_market_values;
    }

    @Override
    protected int getMarketsResId() {
        return R.array.pref_vegetable_market_titles;
    }

    @Override
    public String getBookmarkCategory() {
        return Constants.VEGETABLE_BOOKMARK;
    }


    @Override
    public String getCategory() {
        return Constants.VEGETABLE;
    }
}
