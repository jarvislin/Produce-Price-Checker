package com.jarvislin.producepricechecker.activity;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.model.OpenData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by jarvis on 15/8/26.
 */
@EActivity(R.layout.activity_chart)
public class ChartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @RestService
    ApiClient client;

    @ViewById
    LineChart chart;
    @ViewById
    Spinner spinner;
    @ViewById
    ProgressBar chartProgress;

    private int currentPosition = 0;
    private String[] dateRange = {"一週", "一個月", "三個月", "半年", "一年", "兩年"};

    @AfterViews
    void init() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dateRange);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        chart.setDescription("請用兩指手勢進行放大/縮小");
        chartProgress.setVisibility(View.VISIBLE);
        fetch("104.08.20", "104.08.26", "椰子", "台北一");
        chartProgress.setVisibility(View.GONE);
    }

    @Background
    protected void fetch(String startDate, String endDate, String produceName, String marketName) {
        setProgressVisibility(View.VISIBLE);
        Gson gson = new Gson();
        ArrayList<OpenData> list;
        String data = client.getOpenData(startDate, endDate, produceName, marketName);
        list = gson.fromJson(data, new TypeToken<ArrayList<OpenData>>() {
        }.getType());
        Log.e("GG", list.size() + "");
        Iterator<OpenData> iterator = list.iterator();
        while (iterator.hasNext()) {
            OpenData openData = iterator.next();
            if (!openData.getProduceNumber().equals("11") || !openData.getMarketNumber().equals("109")) {
                iterator.remove();
            }
        }
        Collections.sort(list);
        Log.e("GG", list.size() + "");
        updateChart(list);
        setProgressVisibility(View.GONE);
    }

    @UiThread
    void setProgressVisibility(int status){
        chartProgress.setVisibility(status);
    }

    @UiThread
    protected void updateChart(ArrayList<OpenData> list) {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        ArrayList<String> xLabels = new ArrayList<String>();
        ArrayList<Entry> lowValues = new ArrayList<Entry>();
        ArrayList<Entry> midValues = new ArrayList<Entry>();
        ArrayList<Entry> topValues = new ArrayList<Entry>();
        ArrayList<Entry> avgValues = new ArrayList<Entry>();

        for (int i = 0; i < list.size(); i++) {
            xLabels.add(list.get(i).getTransactionDate());
            lowValues.add(new Entry(Float.parseFloat(list.get(i).getLowPrice()), i));
            midValues.add(new Entry(Float.parseFloat(list.get(i).getMiddlePrice()), i));
            topValues.add(new Entry(Float.parseFloat(list.get(i).getTopPrice()), i));
//            avgValues.add(new Entry(Float.parseFloat(list.get(i).getAveragePrice()), i));
        }

        LineDataSet low = new LineDataSet(lowValues, "Low ");
        low.setLineWidth(2.5f);
        low.setCircleSize(4f);

        LineDataSet mid = new LineDataSet(midValues, "Mid ");
        mid.setLineWidth(2.5f);
        mid.setCircleSize(4f);

        LineDataSet top = new LineDataSet(topValues, "Top ");
        top.setLineWidth(2.5f);
        top.setCircleSize(4f);

        LineDataSet avg = new LineDataSet(avgValues, "Avg ");
        avg.setLineWidth(2.5f);
        avg.setCircleSize(4f);

        low.setColor(mColors[0]);
        low.setCircleColor(mColors[0]);

        mid.setColor(mColors[1]);
        mid.setCircleColor(mColors[1]);

        top.setColor(mColors[2]);
        top.setCircleColor(mColors[2]);

        avg.setColor(mColors[3]);
        avg.setCircleColor(mColors[3]);


        dataSets.add(low);
        dataSets.add(mid);
        dataSets.add(top);
//        dataSets.add(avg);

        LineData data = new LineData(xLabels, dataSets);
        chart.setData(data);
        chart.invalidate();
    }

    private int[] mColors = new int[]{
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2],
            ColorTemplate.VORDIPLOM_COLORS[3]
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(currentPosition != position) {
            currentPosition = position;
            switch (position) {
                case 0:
                    fetch("104.08.20", "104.08.26", "椰子", "台北一");
                    break;
                case 1:
                    fetch("104.07.26", "104.08.26", "椰子", "台北一");
                    break;
                case 2:
                    fetch("104.05.26", "104.08.26", "椰子", "台北一");
                    break;
                case 3:
                    fetch("104.02.26", "104.08.26", "椰子", "台北一");
                    break;
                case 4:
                    fetch("103.08.26", "104.08.26", "椰子", "台北一");
                    break;
                case 5:
                    fetch("102.08.26", "104.08.26", "椰子", "台北一");
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
