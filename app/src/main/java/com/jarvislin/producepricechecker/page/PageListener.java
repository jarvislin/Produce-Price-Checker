package com.jarvislin.producepricechecker.page;

import android.view.Menu;

import com.jarvislin.producepricechecker.ActivityComponentHelper;

/**
 * Created by wu on 2015/06/24
 */
public interface PageListener {
    void onPageStart(ActivityComponentHelper componentHelper);

    void onCreateOptionsMenu(ActivityComponentHelper componentHelper, Menu menu);
}
