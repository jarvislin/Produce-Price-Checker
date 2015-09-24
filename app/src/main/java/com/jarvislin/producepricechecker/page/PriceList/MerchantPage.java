package com.jarvislin.producepricechecker.page.PriceList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.page.PageListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;

import flow.path.Path;

/**
 * Created by jarvis on 15/9/23.
 */
@EView
public class MerchantPage extends RelativeLayout implements PageListener {
    @Bean
    MerchantPresenter presenter;

    public MerchantPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
    }

    @Override
    public void onPrepareOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {

    }
}
