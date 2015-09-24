package com.jarvislin.producepricechecker.page.PriceList;

import android.view.View;

import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.Fruit;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.Vegetable;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ApiProduce;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.page.Presenter;
import com.jarvislin.producepricechecker.util.ApiDataAdapter;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.DateUtil;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;

import flow.Flow;
import flow.path.Path;

/**
 * Created by jarvis on 15/9/23.
 */
@EBean
public class CustomerPresenter extends Presenter {
    CustomerPage page;
    CustomerPath path;
    @RestService
    ApiClient client;


    @Override
    protected void init(Path path, View view) {
        this.path = (CustomerPath) path;
        this.page = (CustomerPage) view;
        loadData(this.path.getData().getMarketNumber());
    }

    @Background
    protected void loadData(String marketNumber) {
        ToolsHelper.showProgressDialog(getContext(), false);
        //show
        String updateDate = "";
//                path.getData().getUpdateDate(marketNumber);
        if (updateDate.equals(DateUtil.getCurrentDate())) {
            loadClientData(marketNumber);
        } else if (ToolsHelper.isNetworkAvailable(getContext())) {
            downloadData(marketNumber);
        } else if (DatabaseController.getProduces(this.path.getData().getCategory(), marketNumber).size() > 0) {
            loadClientData(marketNumber);
        } else {
            Flow.get(getContext()).goBack();
            //show no data
        }
        ToolsHelper.closeProgressDialog(false);
    }

    protected void downloadData(String marketNumber) {
        MultiValueMap params = new LinkedMultiValueMap<>();
        params.add("token", getString(R.string.token));
        params.add("market", this.path.getData().getMarketNumber());
        params.add("category", this.path.getData().getCategory());
        ArrayList<ApiProduce> list = client.getData(params);
        ApiDataAdapter adapter = new ApiDataAdapter(list);
        page.handleData(adapter.getDataList(), this.path.getData());
        updateDatabase(adapter.getDataList(), marketNumber);
    }

    public void loadClientData(String marketNumber) {
        ArrayList<Produce> produces = DatabaseController.getProduces(this.path.getData().getCategory(), marketNumber);
        page.handleData(produces, this.path.getData());
    }

    @Background
    protected void updateDatabase(ArrayList<Produce> produces, String marketNumber) {
        if (produces != null && !produces.isEmpty()) {
            DatabaseController.clearTable(this.path.getData().getCategory(), marketNumber);
            for (Produce produce : produces) {
                produce.save();
            }
            this.path.getData().updateDatabase(produces, this.path.getData().getCategory());
        }
    }
}
