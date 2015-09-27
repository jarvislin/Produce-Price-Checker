package com.jarvislin.producepricechecker.model;

import android.content.Context;

import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jarvis on 15/9/24.
 */
public class ProduceData implements Serializable{
    private String defaultMarketNumber;
    private int marketNumbersResId;
    private int marketsResId;
    private String bookmarkCategory;
    private String category;

    public ProduceData(String defaultMarketNumber, int marketNumbersResId, int marketsResId, String bookmarkCategory, String category) {
        this.defaultMarketNumber = defaultMarketNumber;
        this.marketNumbersResId = marketNumbersResId;
        this.marketsResId = marketsResId;
        this.bookmarkCategory = bookmarkCategory;
        this.category = category;
    }

    public  int getMarketNumbersResId(){return marketNumbersResId;}
    protected  int getMarketsResId(){return marketsResId;}

    public  String getBookmarkCategory(){return bookmarkCategory;}
    public  String getCategory(){return category;}

    public String getUpdateDate(String marketNumber) {
        return DatabaseController.getUpdateDate(getCategory(), marketNumber);
    }

    public String getDefaultMarketNumber(){
        return defaultMarketNumber;
    }

    public String getMarketName(Context context, String marketNumber) {
        String [] numbers = context.getResources().getStringArray(getMarketNumbersResId());
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
