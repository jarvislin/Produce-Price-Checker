package com.jarvislin.producepricechecker.activity;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.adapter.MerchantBookmarkAdapter;

import org.androidannotations.annotations.EActivity;

import database.DatabaseController;

/**
 * Created by Jarvis Lin on 2015/6/20.
 */
@EActivity(R.layout.activity_bookmark_merchant)
public class MerchantBookmarkActivity extends CustomerBookmarkActivity {

    @Override
    protected void initListView() {
        adapter = new MerchantBookmarkAdapter(this, DatabaseController.getBookmarks(getBookmarkCategory()), getBookmarkCategory(), prefs);
        bookmarkList.setAdapter(adapter);
    }
}
