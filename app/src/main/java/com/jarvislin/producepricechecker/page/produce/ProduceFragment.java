package com.jarvislin.producepricechecker.page.produce;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Jarvis on 2017/4/29.
 */
@EFragment(R.layout.fragment_produce)
public class ProduceFragment extends BaseFragment<ProducePresenter> implements ProduceView {
    @ViewById
    Spinner spinnerMarket;
    @ViewById
    ImageView buttonDatePicker;
    @ViewById
    FloatingActionButton fab;
    @ViewById
    RecyclerView recyclerView;

    @Override
    protected ProducePresenter createPresenter() {
        return new ProducePresenter(this);
    }

    @AfterViews
    void init() {

    }
}
