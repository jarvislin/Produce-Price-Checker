package com.jarvislin.producepricechecker.page.PriceList;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.BlankActivity;
import com.jarvislin.producepricechecker.BlankActivity_;
import com.jarvislin.producepricechecker.MainActivity_;
import com.jarvislin.producepricechecker.bean.DataLoader;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ApiProduce;
import com.jarvislin.producepricechecker.model.HistoryDirectory;
import com.jarvislin.producepricechecker.model.OpenData;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.page.Details.DetailsPath;
import com.jarvislin.producepricechecker.page.History.CustomerHistoryPath;
import com.jarvislin.producepricechecker.page.History.MerchantHistoryPath;
import com.jarvislin.producepricechecker.page.Index.IndexPath;
import com.jarvislin.producepricechecker.page.Presenter;
import com.jarvislin.producepricechecker.path.HandlesBack;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Iterator;

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
    @Pref
    protected Preferences_ preferences;
    @RestService
    protected ApiClient client;

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

    @Background
    public void fetchHistoryDirectory() {
        ToolsHelper.showProgressDialog(getContext(), false);
        HistoryDirectory directory = dataLoader.getHistoryDirectory();
        ToolsHelper.closeProgressDialog(false);
        if (directory != null) {
            page.showHistoryDialog(directory);
        }
    }

    @Background
    public void fetchHistory(String year, String date) {
        ToolsHelper.showProgressDialog(getContext(), false);
        ArrayList<ApiProduce> list = dataLoader.getHistory(year, date);
        ToolsHelper.closeProgressDialog(false);
        Intent intent = new Intent();
        intent.setClass(getContext(), BlankActivity_.class);
        intent.putExtra("historyPath", (preferences.userMode().get().equals(Constants.CUSTOMER)) ? new CustomerHistoryPath(list) : new MerchantHistoryPath(list));
        getContext().startActivity(intent);
    }

    @Background
    public void getChartItems(final Produce produce) {
        ToolsHelper.showProgressDialog(getContext(), false);
        String[] date = produce.transactionDate.split("\\.");
        ArrayList<OpenData> list = null;
        try {
            int year = Integer.parseInt(date[0]);
            String marketName = produce.marketName;
            if(produce.marketName.equals("台中市場")) {
                marketName = "台中市";
            } else if (produce.marketName.equals("高雄市場")) {
                marketName = "高雄市";
            } else if (produce.marketName.equals("彰化市場")) {
                marketName = "溪湖鎮";
            }
            String openData = client.getOpenData(year - 2 + "." + date[1] + "." + date[2], produce.transactionDate, produce.produceName, marketName);
            list = new Gson().fromJson(openData, new TypeToken<ArrayList<OpenData>>() {
            }.getType());

            Iterator<OpenData> iterator = list.iterator();
            OpenData temp;
            while (iterator.hasNext()) {
                temp = iterator.next();
                if (!temp.getProduceNumber().equals(produce.produceNumber) || temp.getTransactionAmount().equals("0")) {
                    iterator.remove();
                }
            }
        } catch (Exception ex) {

        } finally {
            ToolsHelper.closeProgressDialog(false);
            if (list == null || list.isEmpty()) {
                //show error
                showToast("無法取得資料，請確認網路狀況。", Toast.LENGTH_SHORT);
            } else {
                direct(produce, list);
            }
        }
    }

    @UiThread
    protected void direct(Produce produce, ArrayList<OpenData> list) {
        Intent intent = new Intent();
        intent.setClass(getContext(), BlankActivity_.class);
        intent.putExtra("detailsPath", new DetailsPath(produce.marketName, produce.produceName, list));
        getContext().startActivity(intent);
    }
}
