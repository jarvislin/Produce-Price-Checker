package com.jarvislin.producepricechecker.page.History;

import android.view.View;

import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.page.Presenter;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/5.
 */
@EBean
public class HistoryPresenter extends Presenter{
    private HistoryPage page;
    private HistoryPath path;
    @Override
    protected void init(Path path, View view) {
        this.path = (HistoryPath) path;
        this.page = (HistoryPage) view;
    }

    public ArrayList<Produce> getProduces(){
        return path.getList();
    }
}
