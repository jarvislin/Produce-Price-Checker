package com.jarvislin.producepricechecker.page.PriceList;

import android.view.View;
import android.widget.Toast;

import com.jarvislin.producepricechecker.bean.DataLoader;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.page.Index.IndexPath;
import com.jarvislin.producepricechecker.page.Presenter;
import com.jarvislin.producepricechecker.path.HandlesBack;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import flow.Flow;
import flow.History;
import flow.path.Path;

/**
 * Created by jarvis on 15/9/23.
 */
@EBean
public class PriceListPresenter extends Presenter implements DataLoader.OnReceiveDataListener, HandlesBack {
    private PriceListPage page;
    private ProduceDataGetter path;
    @Bean
    protected DataLoader dataLoader;

    @Override
    protected void init(Path path, View view) {
        this.path = (ProduceDataGetter) path;
        this.page = (PriceListPage) view;
    }


    @Background
    public void loadData(String marketNumber) {
        ToolsHelper.showProgressDialog(getContext(), false);
        dataLoader.setOnReceiveDataListener(this);
        dataLoader.loadLatestData(getContext(), path.getData().getCategory(), marketNumber, path.getData().getUpdateDate(marketNumber), path.getData().getBookmarkCategory());
        ToolsHelper.closeProgressDialog(false);
    }

    @Background
    public void loadData() {
        dataLoader.setOnReceiveDataListener(this);
        ToolsHelper.showProgressDialog(getContext(), false);
        dataLoader.loadLatestData(getContext(), "", "", "", "");
        ToolsHelper.closeProgressDialog(false);
    }

    public ProduceData getProduceData() {
        return this.path.getData();
    }

    public boolean isLoaderAlive() {
        return dataLoader.isAlive();
    }

    @Override
    public void OnReceived(ArrayList<Produce> produces) {
        page.handleData(produces);
    }

    @Override
    public void OnFailed() {
        showToast("似乎是網路或伺服器出了點問題。", Toast.LENGTH_SHORT);
        onBackPressed();
    }

    public String getMarketNumber() {
        return dataLoader.getMarketNumber();
    }

    @Override
    public boolean onBackPressed() {
        Flow.get(getContext()).setHistory(History.single(new IndexPath()), Flow.Direction.BACKWARD);
        return false;
    }
}
