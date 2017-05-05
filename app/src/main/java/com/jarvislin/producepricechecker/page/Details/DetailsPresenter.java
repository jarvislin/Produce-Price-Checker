package com.jarvislin.producepricechecker.page.Details;

import android.view.View;

import com.jarvislin.producepricechecker.model.OpenData;
import com.jarvislin.producepricechecker.page.Presenter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;

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

    public ArrayList<OpenData> getChartDataList() {
        return path.list;
    }

    public String getMarketName() {
        return path.market;
    }

    public String getProduceName() {
        return path.produce;
    }
}
