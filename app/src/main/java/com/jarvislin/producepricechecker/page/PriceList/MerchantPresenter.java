package com.jarvislin.producepricechecker.page.PriceList;

import android.view.View;

import com.jarvislin.producepricechecker.page.Presenter;

import org.androidannotations.annotations.EBean;

import flow.path.Path;

/**
 * Created by jarvis on 15/9/23.
 */
@EBean
public class MerchantPresenter extends Presenter {
    MerchantPage page;
    MerchantPath path;

    @Override
    protected void init(Path path, View view) {
        this.page = (MerchantPage) view;
        this.path = (MerchantPath) path;
    }
}
