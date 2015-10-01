package com.jarvislin.producepricechecker.page.Questions;

import android.view.View;

import com.jarvislin.producepricechecker.page.Presenter;

import org.androidannotations.annotations.EBean;

import flow.path.Path;

/**
 * Created by jarvis on 15/10/1.
 */
@EBean
public class QuestionsPresenter extends Presenter{
    QuestionsPath path;
    QuestionsPage page;
    @Override
    protected void init(Path path, View view) {
        this.path = (QuestionsPath) path;
        this.page = (QuestionsPage) view;
    }
}
