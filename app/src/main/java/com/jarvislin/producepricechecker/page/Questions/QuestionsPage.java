package com.jarvislin.producepricechecker.page.Questions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.widget.ScrollView;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.page.PageListener;

import org.androidannotations.annotations.EView;

import flow.path.Path;

/**
 * Created by jarvis on 15/10/1.
 */
@EView
public class QuestionsPage extends ScrollView implements PageListener {
    public QuestionsPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(getContext());
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        componentHelper.showToolbar(false);
        componentHelper.showArrow();
        componentHelper.getActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {

    }
}
