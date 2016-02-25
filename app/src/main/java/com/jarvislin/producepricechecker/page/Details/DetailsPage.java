package com.jarvislin.producepricechecker.page.Details;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.custom.TitleTextView;
import com.jarvislin.producepricechecker.model.OpenData;
import com.jarvislin.producepricechecker.page.PageListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import flow.path.Path;

/**
 * Created by Jarvis Lin on 2016/2/23.
 */
@EView
public class DetailsPage extends RelativeLayout implements PageListener, AdapterView.OnItemSelectedListener {
    private enum Price {TOP, MID, LOW}

    private int currentPosition = 0;
    @Bean
    protected DetailsPresenter presenter;
    @ViewById
    protected TitleTextView name;
    @ViewById
    protected LineChart chart;
    @ViewById
    protected Spinner spinner;
    private String[] dataAmount = {"10筆", "50筆", "100筆", "200筆", "400筆", "750筆"};
    private ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    private LineData lineData;


    private ArrayList<OpenData> list;

    public DetailsPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
        componentHelper.showToolbar(false);
        componentHelper.showArrow();
        componentHelper.getToolbar().setTitle(presenter.getMarketName() + " " + presenter.getProduceName());
        initViews();
    }

    private void initViews() {
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, dataAmount);
        spinner.setAdapter(adapter);

        int amount = 10;
        list = presenter.getChartDataList();

        dataSets.add(generateLineData(Price.TOP, amount));
        dataSets.add(generateLineData(Price.MID, amount));
        dataSets.add(generateLineData(Price.LOW, amount));
        lineData = new LineData(getDates(list, amount), dataSets);
        chart.setDescription("單位：元/公斤");
        chart.setData(lineData);
        chart.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (currentPosition != position) {
            currentPosition = position;
            int amount = 10;
            switch (position) {
                case 0:
                    amount = 10;
                    break;
                case 1:
                    amount = 50;
                    break;
                case 2:
                    amount = 100;
                    break;
                case 3:
                    amount = 200;
                    break;
                case 4:
                    amount = 400;
                    break;
                case 5:
                    amount = 750;
                    break;
            }
            dataSets.clear();
            lineData.clearValues();
            dataSets.add(generateLineData(Price.TOP, amount));
            dataSets.add(generateLineData(Price.MID, amount));
            dataSets.add(generateLineData(Price.LOW, amount));

            lineData = new LineData(getDates(list, amount), dataSets);
            chart.setData(lineData);
            chart.invalidate();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private List<String> getDates(ArrayList<OpenData> openData, int amount) {
        amount = amount > list.size() ? list.size() : amount;
        ArrayList<String> result = new ArrayList<>();
        for (int i = list.size() - amount; i < list.size(); i++) {
            result.add(openData.get(i).getTransactionDate());
        }
        return result;
    }

    private LineDataSet generateLineData(Price price, int amount) {
        String title;
        int color;
        switch (price) {
            case TOP:
                title = "上價";
                color = Color.rgb(251, 73, 67);
                break;
            case MID:
                title = "中價";
                color = Color.rgb(0, 180, 0);
                break;
            case LOW:
                title = "下價";
                color = Color.rgb(53, 114, 227);
                break;
            default:
                title = "";
                color = 0;
                break;
        }

        ArrayList<Entry> entries = new ArrayList<>();
        amount = amount > list.size() ? list.size() : amount;

        for (int index = list.size() - amount, count = 0; index < list.size(); index++, count++) {
            switch (price) {
                case TOP:
                    entries.add(new Entry(Float.parseFloat(list.get(index).getTopPrice()), count));
                    break;
                case MID:
                    entries.add(new Entry(Float.parseFloat(list.get(index).getMiddlePrice()), count));
                    break;
                case LOW:
                    entries.add(new Entry(Float.parseFloat(list.get(index).getLowPrice()), count));
                    break;
            }
        }

        LineDataSet set = new LineDataSet(entries, title);
        set.setColor(color);
        set.setLineWidth(1.f);
        set.setCircleColor(color);
        set.setCircleRadius(1.5f);
        set.setFillColor(color);
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(11f);
        set.setValueTextColor(color);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return set;
    }


    @Override
    public void onCreateOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {

    }
}
