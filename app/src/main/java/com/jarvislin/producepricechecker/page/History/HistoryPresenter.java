package com.jarvislin.producepricechecker.page.History;

import android.view.View;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.page.Presenter;
import com.jarvislin.producepricechecker.util.Constants;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/5.
 */
@EBean
public class HistoryPresenter extends Presenter{
    private HistoryPage page;
    private HistoryPath path;
    @Override
    protected void init(Path path, View view) {
        this.path = (HistoryPath) path;
        this.page = (HistoryPage) view;
    }

    public ArrayList<Produce> getProduces(){
        return new ArrayList<>(path.getList());
    }

    public String getMarketName() {
        if(path.getList().isEmpty()) return "";
        String currentNumber = path.getList().get(0).marketNumber;
        boolean isFruit = path.getList().get(0).mainCategory.equals(Constants.FRUIT);
        String[] markets = getContext().getResources().getStringArray(isFruit ? R.array.pref_fruit_market_titles : R.array.pref_vegetable_market_titles);
        String[] numbers = getContext().getResources().getStringArray(isFruit ? R.array.pref_fruit_market_values : R.array.pref_vegetable_market_values);
        for(int i = 0 ; i < markets.length ; i++){
            if(numbers[i].equals(currentNumber)) return markets[i];
        }
        return "";
    }

    public String getDate() {
        if(path.getList().isEmpty()) return "";
        return path.getList().get(0).transactionDate;
    }
}
