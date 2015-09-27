package com.jarvislin.producepricechecker.model;

import android.content.Context;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.Preferences_;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jarvis on 15/9/24.
 */
public abstract class ProduceData implements Serializable{
    public ProduceData(){}

    protected String number;
    public abstract int getNumbersResId();
    protected abstract int getMarketsResId();

    public abstract String getBookmarkCategory();
    public abstract String getCategory();

    public String getUpdateDate(String marketNumber) {
        return DatabaseController.getUpdateDate(getCategory(), marketNumber);
    }

    public String getDefaultMarketNumber(){
        return number;
    }

    public String getMarketName(Context context, String marketNumber) {
        String [] numbers = context.getResources().getStringArray(getNumbersResId());
        String [] markets = context.getResources().getStringArray(getMarketsResId());
        for (int i = 0; i < numbers.length; i++) {
            if(marketNumber.equals(numbers[i])) {
                return markets[i];
            }
        }
        return "無法顯示";
    }
    public void updateDatabase(ArrayList<Produce> produces, String category) {
        DatabaseController.updateBookmark(produces, category);
    }


}
