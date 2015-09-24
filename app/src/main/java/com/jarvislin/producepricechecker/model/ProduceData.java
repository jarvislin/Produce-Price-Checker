package com.jarvislin.producepricechecker.model;

import android.content.Context;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.util.Constants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jarvis on 15/9/24.
 */
public abstract class ProduceData{
    public abstract int getNumbersResId();
    protected abstract int getMarketsResId();
    public abstract String getMarketNumber();
    public abstract String getBookmarkCategory();
    public abstract String getCategory();

    public String getMarketName(Context context) {
        String [] numbers = context.getResources().getStringArray(getNumbersResId());
        String [] markets = context.getResources().getStringArray(getMarketsResId());
        String currentNumber = getMarketNumber();
        for (int i = 0; i < numbers.length; i++) {
            if(currentNumber.equals(numbers[i])) {
                return markets[i];
            }
        }
        return "無法顯示";
    }
    public void updateDatabase(ArrayList<Produce> produces, String category) {
        DatabaseController.updateBookmark(produces, category);
    }


}
