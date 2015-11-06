package com.jarvislin.producepricechecker.page.History;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.page.PageListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/6.
 */
@EView
abstract class HistoryPage extends RelativeLayout implements PageListener{
    @Bean
    protected HistoryPresenter presenter;
    public HistoryPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
        init();
    }

    @Override
    public void onCreateOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {

    }

    abstract void init();
}
