package com.jarvislin.producepricechecker.page.PriceList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.page.PageListener;

import flow.path.Path;

/**
 * Created by jarvis on 15/9/23.
 */
public class MerchantPage extends RelativeLayout implements PageListener {

    public MerchantPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {

    }

    @Override
    public void onPrepareOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {

    }
}
