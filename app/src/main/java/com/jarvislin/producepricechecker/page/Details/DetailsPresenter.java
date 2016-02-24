package com.jarvislin.producepricechecker.page.Details;

import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.OpenData;
import com.jarvislin.producepricechecker.model.ProduceData;
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

    @Override
    protected void init(Path path, View view) {
        this.path = (DetailsPath) path;
        this.page = (DetailsPage) view;
    }

    public Produce getProduceData() {
        return path.data;
    }

    public ArrayList<OpenData> getChartDataList() {
        return path.list;
    }
}
