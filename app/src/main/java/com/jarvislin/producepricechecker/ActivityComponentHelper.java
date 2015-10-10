package com.jarvislin.producepricechecker;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


/**
 * Created by wu on 2015/06/26
 */
public interface ActivityComponentHelper {

    AppCompatActivity getActivity();

    Toolbar getToolbar();

    void hideToolbar();

    void showToolbar(boolean showSpinner);

    void showHamburger();

    void showArrow();
}
