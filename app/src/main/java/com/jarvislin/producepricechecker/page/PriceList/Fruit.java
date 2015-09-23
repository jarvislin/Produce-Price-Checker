package com.jarvislin.producepricechecker.page.PriceList;

import android.content.Context;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.Preferences_;

import java.util.ArrayList;

import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;

/**
 * Created by Jarvis Lin on 2015/8/15.
 */
public class Fruit implements ShareContent {
    private Preferences_ prefs;
    private Context context;

    public Fruit(Context context){
        this.context = context;
        prefs = new Preferences_(context);
    }

    @Override
    public String getMarketNumber() {
        return prefs.fruitMarketList().get();
    }

    @Override
    public String getMarketName() {
        String [] numbers = context.getResources().getStringArray(R.array.pref_fruit_market_values);
        String [] markets = context.getResources().getStringArray(R.array.pref_fruit_market_titles);
        String currentNumber = getMarketNumber();
        for (int i = 0; i < numbers.length; i++) {
            if(currentNumber.equals(numbers[i])) {
                return markets[i];
            }
        }
        return "無法顯示";
    }

    @Override
    public String getUpdateDate() {
        return prefs.fruitUpdateDate().get();
    }

    @Override
    public String getBookmarkCategory() {
        return Constants.FRUIT_BOOKMARK;
    }

    @Override
    public void updateDatabase(ArrayList<Produce> produces) {
        DatabaseController.updateBookmark(produces, Constants.FRUIT_BOOKMARK);
        prefs.fruitUpdateDate().put(produces.get(0).transactionDate);
    }
}
