package com.jarvislin.producepricechecker.page.Details;

import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.model.OpenData;
import com.jarvislin.producepricechecker.page.Presenter;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.Iterator;

import flow.path.Path;

/**
 * Created by Jarvis Lin on 2016/2/23.
 */
@EBean
public class DetailsPresenter extends Presenter {
    private DetailsPath path;
    private DetailsPage page;
    @RestService
    protected ApiClient client;


    @Override
    protected void init(Path path, View view) {
        this.path = (DetailsPath) path;
        this.page = (DetailsPage) view;

        fetch();
    }

    @Background
    protected void fetch() {
        Gson gson = new Gson();
        ArrayList<OpenData> list;
        String data = client.getOpenData("102.12.26", "104.12.26", "椰子", "台北一");
        list = gson.fromJson(data, new TypeToken<ArrayList<OpenData>>() {
        }.getType());

        Iterator<OpenData> iterator = list.iterator();
        while (iterator.hasNext()) {
            if(!iterator.next().getProduceNumber().equals("11")) {
                iterator.remove();
            }
        }

        Log.e("GG", list.size() + "");
        for(OpenData openData: list){
            Log.e("GG",openData.getTransactionDate() + "");
        }
    }
}
