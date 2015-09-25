package com.jarvislin.producepricechecker.page.PriceList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.adapter.CustomerAdapter;
import com.jarvislin.producepricechecker.adapter.MerchantAdapter;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.page.PageListener;
import com.jarvislin.producepricechecker.util.Preferences_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;

import java.util.ArrayList;

import flow.path.Path;

/**
 * Created by jarvis on 15/9/23.
 */
@EView
public class MerchantPage extends PriceListPage {
    @Bean
    PriceListPresenter presenter;

    public MerchantPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    protected CustomerAdapter getAdapter(Context context, ArrayList<Produce> list, Preferences_ prefs, String bookmarkCategory) {
        return new MerchantAdapter(context, list, prefs, bookmarkCategory);
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
        componentHelper.showToolbar(false);
    }

}
