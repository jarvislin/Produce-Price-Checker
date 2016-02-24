package com.jarvislin.producepricechecker.page.Details;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Menu;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.custom.TitleTextView;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.OpenData;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.page.PageListener;

import org.androidannotations.annotations.AfterViews;
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
public class DetailsPage extends ScrollView implements PageListener {
    @Bean
    protected DetailsPresenter presenter;

    @ViewById
    protected TitleTextView name;
    @ViewById
    protected CombinedChart chart;


    private ArrayList<OpenData> list;
    private Produce data;

    public DetailsPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
        initViews();
    }

    private void initViews() {
        list = presenter.getChartDataList();
        data = presenter.getProduceData();
        CombinedData data = new CombinedData(getDates(list));
        data.setData(generateLineData());
        chart.setData(data);
        chart.invalidate();
    }

    private List<String> getDates(ArrayList<OpenData> openData) {
        ArrayList<String> list = new ArrayList<>();
        for (OpenData data : openData) {
            list.add(data.getTransactionDate());
        }
        return list;
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < list.size(); index++)
            entries.add(new Entry(Float.parseFloat(list.get(index).getTopPrice()), index));

        LineDataSet set = new LineDataSet(entries, "上價");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }


    @Override
    public void onCreateOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {

    }
}
