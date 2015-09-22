package com.jarvislin.producepricechecker.page.Index;

import android.view.View;
import com.jarvislin.producepricechecker.page.Presenter;


import org.androidannotations.annotations.EBean;

import flow.path.Path;

/**
 * Created by jarvis on 15/9/22.
 */

@EBean
public class IndexPresenter extends Presenter {
    IndexPath path;
    IndexPage page;
    @Override
    protected void init(Path path, View view) {
        this.path = (IndexPath) path;
        this.page = (IndexPage) view;
    }
}
