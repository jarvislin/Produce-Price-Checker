package com.jarvislin.producepricechecker.page.History;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.adapter.MerchantAdapter;
import com.jarvislin.producepricechecker.page.PageListener;
import com.jarvislin.producepricechecker.page.PriceList.MerchantPage;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.Preferences_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/5.
 */
@EView
public class MerchantHistoryPage extends HistoryPage{
    @Pref
    protected Preferences_ preferences;

    @ViewById
    protected ListView dataList;
    public MerchantHistoryPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    protected void init() {
        String bookmark = "";
        if(!presenter.getProduces().isEmpty()) {
            bookmark = presenter.getProduces().get(0).mainCategory.equals(Constants.FRUIT) ? Constants.FRUIT : Constants.VEGETABLE;
        }
        dataList.setAdapter(new MerchantAdapter(getContext(), presenter.getProduces(), preferences, bookmark));
    }
}
