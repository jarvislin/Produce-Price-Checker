package com.jarvislin.producepricechecker.activity;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
public class ChartActivity extends AppCompatActivity {
    @RestService
    ApiClient client;

    @ViewById
    LineChart chart;

    @AfterViews
    void init() {
        chart.setDescription("請用兩指手勢進行放大/縮小");
        fetch();
    }

    @Background
    protected void fetch() {
        Gson gson = new Gson();
        ArrayList<OpenData> list;
        String data = client.getOpenData("104.08.20", "104.08.26", "椰子", "台北一");
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
}
