package com.jarvislin.producepricechecker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.PreferenceUtil;
import com.jarvislin.producepricechecker.util.ToolsHelper;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;


import java.util.ArrayList;
import java.util.List;


public class DataListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnMenuItemClickListener, OnMenuItemLongClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private ListView mListView;
    private ProduceListAdapter mAdapter;
    private int mOffset;
    private ArrayList<ProduceData> mDataList;
    private boolean isInitialized = false;
    private FragmentManager mFragmentManager;
    private DialogFragment mMenuDialogFragment;
    private TextView mTitle;
    private SearchView mSearch;
    private ImageView mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((PreferenceUtil.isCustomerMode(this)) ? R.layout.customer_data_list : R.layout.general_data_list);

        mFragmentManager = getSupportFragmentManager();
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance((int) getResources().getDimension(R.dimen.tool_bar_height), getMenuObjects());
        initToolbar();

    }

    private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();
        menuObjects.add(new MenuObject(R.drawable.ic_close_blue_36dp));
        menuObjects.add(new MenuObject(R.drawable.ic_info_outline_blue_36dp, "資訊"));
        menuObjects.add(new MenuObject(R.drawable.ic_refresh_blue_36dp, "重新整理"));
        menuObjects.add(new MenuObject(R.drawable.ic_swap_horiz_blue_36dp, "重量單位轉換"));
        menuObjects.add(new MenuObject(R.drawable.ic_list, "收藏清單"));
        return menuObjects;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle =  (TextView)findViewById(R.id.title);

        mMenu = (ImageView)findViewById(R.id.menu);
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuDialogFragment.show(mFragmentManager, "DropDownMenuFragment");
            }
        });

        mSearch = (SearchView)findViewById(R.id.search);
        mSearch.addOnLayoutChangeListener(searchExpandHandler);
        mSearch.setOnQueryTextListener(this);
        mSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mTitle.setVisibility(View.VISIBLE);
                mMenu.setVisibility(View.VISIBLE);
                return false;
            }
        });

        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) mSearch.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search_white_36dp);

    }

    private final View.OnLayoutChangeListener searchExpandHandler = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                                   int oldBottom) {
            SearchView searchView = (SearchView)v;
            if (searchView.isIconfiedByDefault() && !searchView.isIconified())            {
                // search got expanded from icon to search box, hide tabs to make space
                mTitle.setVisibility(View.GONE);
                mMenu.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onWindowFocusChanged(boolean focus) {
        super.onWindowFocusChanged(focus);
        // ContentView has loaded
        if(!isInitialized){
            isInitialized = true;
            initUI();
            update(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
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
        GoogleAnalyticsSender.getInstance(this).send("click_bookmark");
        Intent intent = new Intent();
        intent.putExtra("type", getType());
        intent.setClass(this, BookmarkActivity.class);
        startActivityForResult(intent, 0);
    }

    public void update(View view) {
        GoogleAnalyticsSender.getInstance(this).send("update");
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
        mDataList = (dataFetcher.hasData()) ? dataFetcher.getProduceDataList() : null;
        mOffset = dataFetcher.getOffset();
        if(mDataList == null)
            ToolsHelper.showSiteErrorMessage(this); //show error
        else{
            mAdapter = new ProduceListAdapter(this, mDataList, getType());
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
        GoogleAnalyticsSender.getInstance(this).send("click_info");
        String[] date = ToolsHelper.getDate(mOffset);

        final Dialog dialog = new Dialog(this, R.style.alertDialog);
        dialog.setContentView(R.layout.dialog_info);

        TextView message = (TextView) dialog.findViewById(R.id.info_text);
        message.setText(
                "資料日期：" + date[0] + "/" + date[1] + "/" + date[2] + ToolsHelper.getOffsetInWords(mOffset) + "\n" +
                "單位：" + ToolsHelper.getUnitInWords(PreferenceUtil.getUnit(this)) + "\n" +
                "市場：" + ToolsHelper.getMarketName(ToolsHelper.getMarketNumber(this))
        );

        Button dismiss = (Button) dialog.findViewById(R.id.info_dismiss);
        dismiss.setOnClickListener(closeDialog(dialog));

        dialog.show();
    }

    private View.OnClickListener closeDialog(final Dialog dialog){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        };
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if(null == mDataList) {
            Toast.makeText(this, "讀取資料中，請稍後再試。", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<ProduceData> searchList = getSearchList(newText);
            mAdapter = new ProduceListAdapter(this, searchList, getType());
            mListView.setAdapter(mAdapter);
        }

        return false;
    }

    private ArrayList<ProduceData> getSearchList(String newText) {
        ArrayList<ProduceData> list = new ArrayList<ProduceData>();
        for(ProduceData data : mDataList){
            if(data.getName().contains(newText) || data.getType().contains(newText))
                list.add(data);
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {

        switch (position) {
            case 0:
                break;
            case 1:
                this.info(null);
                break;
            case 2:
                this.update(null);
                break;
            case 3:
                float unit = PreferenceUtil.getUnit(this);
                PreferenceUtil.setUnit(this, unit < 1 ? "1.0" : "0.6");
                mAdapter.notifyDataSetInvalidated();
                Toast.makeText(this, "目前重量單位為：" + (unit < 1 ? "公斤" : "台斤"), Toast.LENGTH_SHORT).show();
                break;
            case 4:
                this.bookmark(null);
                break;
        }
    }


    @Override
    public void onMenuItemLongClick(View view, int i) {
        //do nothing
    }
}


