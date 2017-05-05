package com.jarvislin.producepricechecker.bean;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ApiProduce;
import com.jarvislin.producepricechecker.model.HistoryDirectory;
import com.jarvislin.producepricechecker.adapter.ApiDataAdapter;
import com.jarvislin.producepricechecker.util.DateUtil;
import com.jarvislin.producepricechecker.util.ToolsHelper;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jarvis Lin on 2015/11/1.
 */
@EBean(scope = EBean.Scope.Singleton)
public class DataLoader {
    private String currentMarketNumber;
    private String currentCategory;
    private String currentBookmarkCategory;
    private OnReceiveDataListener onReceiveDataListener;
    private String currentUpdateDate;

    public interface OnReceiveDataListener {
        void OnReceived(ArrayList<Produce> produces);
        void OnFailed();
    }

    public void setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
        this.onReceiveDataListener = onReceiveDataListener;
    }

    @AfterInject
    protected void afterInject() {
        client.setRestErrorHandler(new RestErrorHandler() {
            @Override
            public void onRestClientExceptionThrown(NestedRuntimeException e) {
                //show Toast and Reload button
                loadClientData();
            }
        });
    }

    public void loadLatestData(Context context, String category, String marketNumber, String updateDate, String bookmarkCategory) {
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(marketNumber) && !TextUtils.isEmpty(bookmarkCategory)) {
            currentMarketNumber = marketNumber;
            currentCategory = category;
            currentBookmarkCategory = bookmarkCategory;
            currentUpdateDate = updateDate;
        }
        //show
        if (currentUpdateDate.equals(DateUtil.getCurrentDate())) {
            //has today's data
            loadClientData();
        } else if (ToolsHelper.isNetworkAvailable(context)) {
            //download latest data
            downloadData();
        } else if (DatabaseController.getProduces(currentCategory, currentMarketNumber).size() > 0) {
            //load client data in DB
            loadClientData();
        } else {
            //show no network
            onReceiveDataListener.OnFailed();
        }
    }

    private void loadClientData() {
        ArrayList<Produce> produces = DatabaseController.getProduces(currentCategory, currentMarketNumber);
        onReceiveDataListener.OnReceived(produces);
    }


    private void downloadData() {
        ArrayList<ApiProduce> list = new Gson().fromJson(client.getDataFromGitHub(currentCategory, currentMarketNumber)
                , new TypeToken<List<ApiProduce>>() {
        }.getType());
        if(list == null || list.size() == 0 ){
            loadClientData();
        } else {
            ApiDataAdapter adapter = new ApiDataAdapter(list);
            onReceiveDataListener.OnReceived(adapter.getDataList());
            updateDatabase(adapter.getDataList());
        }
    }

    public ArrayList<ApiProduce> getHistory(String year, String date) {
        ArrayList<ApiProduce> list = new Gson().fromJson(client.getHistoryDataFromGitHub(currentCategory, currentMarketNumber, year, date)
                , new TypeToken<List<ApiProduce>>() {
        }.getType());
        return list;
    }

    public HistoryDirectory getHistoryDirectory() {
        String json = client.getHistoryDirectoryFromGitHub(currentCategory, currentMarketNumber);
        return new Gson().fromJson(json, HistoryDirectory.class);
    }

    private void updateDatabase(ArrayList<Produce> produces) {
        if (produces != null && !produces.isEmpty()) {
            DatabaseController.clearTable(currentCategory, currentMarketNumber);
            TransactionManager.getInstance().saveOnSaveQueue(produces);
            DatabaseController.updateBookmark(produces, currentBookmarkCategory);
        }
    }

    public boolean isAlive() {
        return TextUtils.isEmpty(currentCategory) && TextUtils.isEmpty(currentMarketNumber) && TextUtils.isEmpty(currentBookmarkCategory) && onReceiveDataListener != null;
    }

    public String getMarketNumber() {
        return currentMarketNumber;
    }
}
