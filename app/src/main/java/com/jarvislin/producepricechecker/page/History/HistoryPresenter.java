package com.jarvislin.producepricechecker.page.History;

import android.view.View;

import com.jarvislin.producepricechecker.page.Presenter;

import org.androidannotations.annotations.EBean;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/5.
 */
@EBean
public class HistoryPresenter extends Presenter{
    private HistoryPage page;
    private ProduceListGetter path;
    @Override
    protected void init(Path path, View view) {
        this.path = (ProduceListGetter) path;
        this.page = (HistoryPage) view;
    }
}
