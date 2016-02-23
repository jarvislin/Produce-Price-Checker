package com.jarvislin.producepricechecker.page.Details;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.widget.ScrollView;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.page.PageListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;

import flow.path.Path;

/**
 * Created by Jarvis Lin on 2016/2/23.
 */
@EView
public class DetailsPage extends ScrollView implements PageListener {
    @Bean
    protected DetailsPresenter presenter;
    public DetailsPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
    }

    @Override
    public void onCreateOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {

    }
}
