package com.jarvislin.producepricechecker.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.adapter.CustomerBookmarkAdapter;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.Preferences_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import database.DatabaseController;

/**
 * Created by Jarvis Lin on 2015/6/20.
 */
@EActivity(R.layout.activity_bookmark_customer)
public class CustomerBookmarkActivity extends AppCompatActivity {
    @Pref
    Preferences_ prefs;
    @ViewById
    ListView bookmarkList;
    @ViewById
    Toolbar toolbar;
    @ViewById
    ImageView back;
    @ViewById
    ImageView edit;

    protected CustomerBookmarkAdapter adapter;

    @AfterViews
    protected void init() {
        setSupportActionBar(toolbar);
        initListView();
    }

    protected void initListView() {
        adapter = new CustomerBookmarkAdapter(this, DatabaseController.getBookmarks(getBookmarkKind()), getBookmarkKind(), prefs);
        bookmarkList.setAdapter(adapter);
    }

    private String getType() {
        return getIntent().getStringExtra("type");
    }
    protected String getBookmarkKind(){
        return getType().equals(Constants.FRUIT) ? Constants.FRUIT_BOOKMARK : Constants.VEGETABLE_BOOKMARK;
    }

    @Click
    protected void back(){
        this.finish();
    }

    @Click
    protected void edit(){
        adapter.notifyDataSetInvalidated();
    }
}
