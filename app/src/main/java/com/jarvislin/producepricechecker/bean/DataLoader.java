package com.jarvislin.producepricechecker.bean;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ApiProduce;
import com.jarvislin.producepricechecker.model.HistoryDirectory;
import com.jarvislin.producepricechecker.util.ApiDataAdapter;
import com.jarvislin.producepricechecker.util.DateUtil;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;

import java.util.ArrayList;
import java.util.List;

import flow.Flow;

/**
 * Created by Jarvis Lin on 2015/11/1.
 */
@EBean(scope = EBean.Scope.Singleton)
public class DataLoader {
    @RestService
    protected ApiClient client;
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
        ApiDataAdapter adapter = new ApiDataAdapter(list);
        onReceiveDataListener.OnReceived(adapter.getDataList());
        updateDatabase(adapter.getDataList());
    }

    public ArrayList<Produce> getHistory(String year, String date) {
        ArrayList<ApiProduce> list = new Gson().fromJson(client.getHistoryDataFromGitHub(currentCategory, currentMarketNumber, year, date)
                , new TypeToken<List<ApiProduce>>() {
        }.getType());
        ApiDataAdapter adapter = new ApiDataAdapter(list);
        return adapter.getDataList();
    }

    public HistoryDirectory getHistoryDirectory() {
        String json = client.getHistoryDirectoryFromGitHub(currentCategory, currentMarketNumber);
        return new Gson().fromJson(json, HistoryDirectory.class);
    }

    private void updateDatabase(ArrayList<Produce> produces) {
        if (produces != null && !produces.isEmpty()) {
            DatabaseController.clearTable(currentCategory, currentMarketNumber);
            for (Produce produce : produces) {
                produce.save();
            }
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
