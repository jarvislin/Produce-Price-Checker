package com.jarvislin.producepricechecker;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jarvislin.producepricechecker.util.PreferenceUtil;


public class BookmarkActivity extends Activity {

    private ListView mListView;
    private BookmarkListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((PreferenceUtil.isCustomerMode(this)) ? R.layout.activity_bookmark_customer : R.layout.activity_bookmark_general);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar_bookmark_list);

        findView();
        init();
    }

    @Override
    protected void onDestroy (){
        super.onDestroy();
        GlobalVariable.isEditMode = -1;
    }

    private void findView() {
        mListView = (ListView)findViewById(R.id.bookmark_list);
    }

    private void init() {
        mAdapter = new BookmarkListAdapter(this, PreferenceUtil.getBookmarkList(this, getType()), getType());
        mListView.setAdapter(mAdapter);
    }

    private int getType() {
        return getIntent().getIntExtra("type", -1);
    }

    public void back(View v){
        this.finish();
    }

    public void edit(View v){
        GlobalVariable.isEditMode *= -1;
        mAdapter.notifyDataSetInvalidated();
    }




}
