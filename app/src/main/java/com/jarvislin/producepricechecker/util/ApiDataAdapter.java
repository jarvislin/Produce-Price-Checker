package com.jarvislin.producepricechecker.util;

import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ApiProduce;

import java.util.ArrayList;

/**
 * Created by Jarvis Lin on 2015/8/15.
 */
public class ApiDataAdapter {
    private ArrayList<Produce> dataList;

    public ApiDataAdapter(ArrayList<ApiProduce> list) {
        if(list == null) return;
        Produce data;
        dataList = new ArrayList<>();
        for (ApiProduce produce : list) {
            data = new Produce();
            data.produceName = produce.getProduceName();
            data.produceNumber = produce.getProduceNumber();
            data.topPrice = produce.getTopPrice();
            data.middlePrice = produce.getMiddlePrice();
            data.lowPrice = produce.getLowPrice();
            data.averagePrice = produce.getAveragePrice();
            data.mainCategory = produce.getMainCategory();
            data.subCategory = produce.getSubCategory();
            data.transactionDate = produce.getTransactionDate();
            data.transactionAmount = produce.getTransactionAmount();
            data.marketName = produce.getMarketName();
            data.marketNumber = produce.getMarketNumber();
            dataList.add(data);
        }
    }

    public ArrayList<Produce> getDataList() {
        return dataList;
    }
}
