package com.jarvislin.producepricechecker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.PreferenceUtil;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import java.util.ArrayList;


public class DataListActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();
    private ListView mListView;
    private ProduceListAdapter mAdapter;
    private int mOffset;
    private GoogleAnalyticsSender mSender;
    private boolean hasInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((PreferenceUtil.isCustomerMode(this)) ? R.layout.customer_data_list : R.layout.general_data_list);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar_data_table);
        mSender = new GoogleAnalyticsSender(this);
    }

    @Override
    public void onWindowFocusChanged(boolean focus) {
        super.onWindowFocusChanged(focus);
        // ContentView has loaded
        if(!hasInitialized){
            hasInitialized = true;
            initUI();
            update(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAdapter.notifyDataSetChanged();
    }

    private void initUI() {
        if(PreferenceUtil.isCustomerMode(this)){
            //find views
            TextView type = (TextView)findViewById(R.id.type);
            TextView name = (TextView)findViewById(R.id.name);
            TextView avg = (TextView)findViewById(R.id.range);

            //save width
            GlobalVariable.fiveCharsWidth = type.getWidth();
            GlobalVariable.rangeWidth = avg.getWidth();

            type.setText("品種");
            name.setText("名稱");
            avg.setText("平均價格區間");

            //set visible
            type.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            avg.setVisibility(View.VISIBLE);

            //set width
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(GlobalVariable.fiveCharsWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            type.setLayoutParams(params);
            name.setLayoutParams(params);

            params = new LinearLayout.LayoutParams(GlobalVariable.rangeWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            avg.setLayoutParams(params);

        } else{
            //find views
            TextView name = (TextView)findViewById(R.id.type_name);
            TextView top = (TextView)findViewById(R.id.top);
            TextView mid = (TextView)findViewById(R.id.mid);
            TextView low = (TextView)findViewById(R.id.low);
            TextView avg = (TextView)findViewById(R.id.avg);

            //save width
            GlobalVariable.fiveCharsWidth = name.getWidth();
            GlobalVariable.fourDigitsWidth = top.getWidth();

            name.setText("品種/名稱");
            top.setText("上價");
            mid.setText("中價");
            low.setText("下價");
            avg.setText("平均");

            //set visible
            name.setVisibility(View.VISIBLE);
            top.setVisibility(View.VISIBLE);
            mid.setVisibility(View.VISIBLE);
            low.setVisibility(View.VISIBLE);
            avg.setVisibility(View.VISIBLE);

            //set width
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(GlobalVariable.fiveCharsWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            name.setLayoutParams(params);

            params = new LinearLayout.LayoutParams(GlobalVariable.fourDigitsWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            top.setLayoutParams(params);
            mid.setLayoutParams(params);
            low.setLayoutParams(params);
            avg.setLayoutParams(params);
        }
    }

    public void bookmark(View view) {
        mSender.send("click_bookmark");
        Intent intent = new Intent();
        intent.putExtra("type", getType());
        intent.setClass(this, BookmarkActivity.class);
        startActivityForResult(intent, 0);
    }

    public void update(View view) {
        mSender.send("update");
        if(!ToolsHelper.isNetworkAvailable(this)) {
            ToolsHelper.showNetworkErrorMessage(this);
            finish();
        } else {

            new UpdateTask(this).execute(getType());
            findViews();
        }
    }

    public void back(View view){
        finish();
    }

    private void findViews() {
        mListView = (ListView)findViewById(R.id.data_list);
    }

    private int getType() {
        return getIntent().getIntExtra("type", -1);
    }

    public void loadDataList(DataFetcher dataFetcher){
        ArrayList<ProduceData> dataList = (dataFetcher.hasData()) ? dataFetcher.getProduceDataList() : null;
        mOffset = dataFetcher.getOffset();
        if(dataList == null)
            ToolsHelper.showSiteErrorMessage(this); //show error
        else{
            mAdapter = new ProduceListAdapter(this, dataList, getType());
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(itemClickListener);
        }
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //set bookmark status
            ProduceData object = (ProduceData)mAdapter.getItem(position);
            ArrayList<ProduceData> bookmarkList = PreferenceUtil.getBookmarkList(DataListActivity.this, getType());
            ColorDrawable color = (ColorDrawable)view.getBackground();

            if(getResources().getColor(R.color.highlight) == color.getColor()) {
                PreferenceUtil.removeBookmark(DataListActivity.this, bookmarkList, object, getType());
                Toast.makeText(DataListActivity.this, "已從清單移除項目", Toast.LENGTH_SHORT).show();
            } else {
                PreferenceUtil.addBookmark(DataListActivity.this, bookmarkList, object, getType());
                Toast.makeText(DataListActivity.this, "已將項目加入清單", Toast.LENGTH_SHORT).show();
            }

            mAdapter.notifyDataSetChanged();
        }
    };

    public void info(View view) {
        mSender.send("click_info");
        String[] date = ToolsHelper.getDate(mOffset);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("資訊");
        builder.setMessage(
                "資料日期：" + date[0] + "/" + date[1] + "/" + date[2] + ToolsHelper.getOffsetInWords(mOffset) + "\n" +
                "單位：" + ToolsHelper.getUnitInWords(ToolsHelper.getUnit(this)) + "\n" +
                "市場：" + ToolsHelper.getMarketName(ToolsHelper.getMarketNumber(this))
        );

        builder.setNeutralButton(getString(R.string.back), null);

        builder.show();
    }
}


