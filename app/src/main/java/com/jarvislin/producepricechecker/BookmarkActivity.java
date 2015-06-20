package com.jarvislin.producepricechecker;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.jarvislin.producepricechecker.adapter.BookmarkListAdapter;
import com.jarvislin.producepricechecker.util.PreferenceUtil;


public class BookmarkActivity extends AppCompatActivity {

    private ListView mListView;
    private BookmarkListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((PreferenceUtil.isCustomerMode(this)) ? R.layout.activity_bookmark_customer : R.layout.activity_bookmark_general);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_bookmark_list);

        findView();
        init();
    }

    private void findView() {
        mListView = (ListView)findViewById(R.id.bookmark_list);
    }

    private void init() {
        mAdapter = new BookmarkListAdapter(this, PreferenceUtil.getBookmarkList(this, getType()), getType());
        mListView.setAdapter(mAdapter);
    }

    private String getType() {
        return getIntent().getStringExtra("type");
    }

    public void back(View v){
        this.finish();
    }

    public void edit(View v){
        mAdapter.notifyDataSetInvalidated();
    }

}
