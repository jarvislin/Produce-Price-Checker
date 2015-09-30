package com.jarvislin.producepricechecker.page.PriceList;

import android.view.View;

import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ApiProduce;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.page.Presenter;
import com.jarvislin.producepricechecker.util.ApiDataAdapter;
import com.jarvislin.producepricechecker.util.DateUtil;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;

import flow.Flow;
import flow.path.Path;

/**
 * Created by jarvis on 15/9/23.
 */
@EBean
public class PriceListPresenter extends Presenter {
    private PriceListPage page;
    private ProduceDataGetter path;
    @RestService
    protected ApiClient client;
    private String currentMarketNumber;

    @Override
    protected void init(Path path, View view) {
        this.path = (ProduceDataGetter) path;
        this.page = (PriceListPage) view;
        currentMarketNumber = this.path.getData().getDefaultMarketNumber();
        loadData(currentMarketNumber);
    }

    @AfterInject
    protected void afterInject() {
        client.setRestErrorHandler(new RestErrorHandler() {
            @Override
            public void onRestClientExceptionThrown(NestedRuntimeException e) {
                //show Toast and Reload button
            }
        });
    }

    public String getMarketNumber() {
        return currentMarketNumber;
    }

    @Background
    protected void loadData(String marketNumber) {
        currentMarketNumber = marketNumber;
        ToolsHelper.showProgressDialog(getContext(), false);
        //show
        String updateDate = path.getData().getUpdateDate(marketNumber);
        if (updateDate.equals(DateUtil.getCurrentDate())) {
            //has today's data
            loadClientData(marketNumber);
        } else if (ToolsHelper.isNetworkAvailable(getContext())) {
            //download latest data
            downloadData(marketNumber);
        } else if (DatabaseController.getProduces(this.path.getData().getCategory(), marketNumber).size() > 0) {
            //load client DB
            loadClientData(marketNumber);
        } else {
            //show no network
            Flow.get(getContext()).goBack();
        }
        ToolsHelper.closeProgressDialog(false);
    }

    protected void downloadData(String marketNumber) {
        MultiValueMap params = new LinkedMultiValueMap<>();
        params.add("token", getString(R.string.token));
        params.add("market", getMarketNumber());
        params.add("category", this.path.getData().getCategory());
        ArrayList<ApiProduce> list = client.getData(params);
        ApiDataAdapter adapter = new ApiDataAdapter(list);
        page.handleData(adapter.getDataList());
        updateDatabase(adapter.getDataList(), marketNumber);
    }

    public void loadClientData(String marketNumber) {
        ArrayList<Produce> produces = DatabaseController.getProduces(this.path.getData().getCategory(), marketNumber);
        page.handleData(produces);
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

    public ProduceData getProduceData() {
        return this.path.getData();
    }
}
