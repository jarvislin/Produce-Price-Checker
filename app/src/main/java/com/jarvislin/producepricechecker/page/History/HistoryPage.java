package com.jarvislin.producepricechecker.page.History;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.adapter.CustomerAdapter;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.page.PageListener;
import com.jarvislin.producepricechecker.page.PriceList.PriceListPage;
import com.jarvislin.producepricechecker.util.Preferences_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/5.
 */
@EView
public class HistoryPage extends PriceListPage implements PageListener{
    @Bean
    protected HistoryPresenter presenter;
    public HistoryPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    protected CustomerAdapter getAdapter(Context context, ArrayList<Produce> list, Preferences_ prefs, String bookmarkCategory) {
        return null;
    }

    @Override
    protected boolean enableSpinner() {
        return false;
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
    }

    @Override
    public void onCreateOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {

    }

    @UiThread
    @Override
    protected void handleData(ArrayList<Produce> list) {

        super.handleData(list);
    }
}
