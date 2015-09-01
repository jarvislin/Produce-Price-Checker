package com.jarvislin.producepricechecker.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.adapter.CustomerAdapter;
import com.jarvislin.producepricechecker.model.ApiProduce;
import com.jarvislin.producepricechecker.util.ApiDataAdapter;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.DateUtil;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseController;
import database.Produce;

/**
 * Created by Jarvis Lin on 2015/6/15.
 */
@EActivity(R.layout.activity_customer)
public class CustomerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnMenuItemClickListener, OnMenuItemLongClickListener {

    private DialogFragment menuDialogFragment;
    private TextView actionBarTitle;
    private SearchView searchView;
    private ImageView menu;
    protected ArrayList<Produce> produces;
    protected CustomerAdapter adapter;
    protected ShareContent shareContent;
    @ViewById
    ListView dataList;
    @ViewById
    TextView bottomInfo;

    @RestService
    ApiClient client;

    @Pref
    Preferences_ prefs;

    @AfterViews
    protected void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menuDialogFragment = ContextMenuDialogFragment.newInstance((int) getResources().getDimension(R.dimen.tool_bar_height), getMenuObjects());
        actionBarTitle = (TextView) findViewById(R.id.title);

        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDialogFragment.show(getSupportFragmentManager(), "DropDownMenuFragment");
            }
        });

        searchView = (SearchView) findViewById(R.id.search);
        searchView.addOnLayoutChangeListener(searchExpandHandler);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                actionBarTitle.setVisibility(View.VISIBLE);
                menu.setVisibility(View.VISIBLE);
                return false;
            }
        });

        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search_white_36dp);

        shareContent = getCategory().equals(Constants.FRUIT) ? new Fruit(this) : new Vegetable(this);
        loadData();
    }

    protected void setBottomInfo() {
        String unitText = (prefs.unit().get() < 1 ? "元/台斤" : "元/公斤");
        bottomInfo.setText(DateUtil.getOffsetInWords(DateUtil.getOffset(produces.get(0).transactionDate)) + "　" + shareContent.getMarketName() + "　單位：" + unitText);
    }

    @Background
    protected void loadData() {
        ToolsHelper.showProgressDialog(this, false);
        //show
        String updateDate = shareContent.getUpdateDate();
        if (updateDate.equals(DateUtil.getCurrentDate())) {
            loadClientData();
        } else if (ToolsHelper.isNetworkAvailable(this)) {
            downloadData();
        } else if (DatabaseController.getProduces(getCategory()).size() > 0) {
            loadClientData();
        } else {
            handleData(null);
        }
        ToolsHelper.closeProgressDialog(false);
    }

    protected void downloadData() {
        MultiValueMap params = new LinkedMultiValueMap<String, String>();
        params.add("token", getString(R.string.token));
        params.add("market", shareContent.getMarketNumber());
        params.add("category", getCategory());
        ArrayList<ApiProduce> list = client.getData(params);
        ApiDataAdapter adapter = new ApiDataAdapter(list);
        handleData(adapter.getDataList());
        updateDatabase();
    }

    public void loadClientData() {
        produces = DatabaseController.getProduces(getCategory(), shareContent.getMarketNumber());
        handleData(produces);
    }

    @UiThread
    protected void handleData(ArrayList<Produce> list) {
        if (list == null || list.size() == 0) {
            ToolsHelper.showToast(this, R.string.error_network);
            finish();
        } else {
            produces = list;
            adapter = new CustomerAdapter(this, list, prefs, shareContent.getBookmarkCategory());
            dataList.setAdapter(adapter);
            dataList.setOnItemClickListener(itemClickListener);
            setBottomInfo();
        }
    }

    @Background
    protected void updateDatabase() {
        if (produces != null && !produces.isEmpty()) {
            DatabaseController.clearTable(getCategory(), shareContent.getMarketNumber());
            for (Produce produce : produces) {
                produce.save();
            }
            shareContent.updateDatabase(produces);
        }
    }

    @Override
    public void onMenuItemClick(View view, int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                GoogleAnalyticsSender.getInstance(this).send("click_update");
                loadData();
                break;
            case 2:
                GoogleAnalyticsSender.getInstance(this).send("click_convert_unit");
                float unit = prefs.unit().get();
                prefs.unit().put(unit < 1 ? 1.0f : 0.6f);
                adapter.notifyDataSetInvalidated();
                String unitText = (prefs.unit().get() < 1 ? "台斤" : "公斤");
                Toast.makeText(this, "目前重量單位為：" + unitText, Toast.LENGTH_SHORT).show();
                setBottomInfo();
                break;
            case 3:
                GoogleAnalyticsSender.getInstance(this).send("click_bookmark");
                openBookmark();
                break;
            case 4:
                GoogleAnalyticsSender.getInstance(this).send("click_share");
                ToolsHelper.shareText(this, "分享：", getString(R.string.share_text));
                break;
        }
    }

    protected void openBookmark() {
        Intent intent = new Intent();
        intent.putExtra("category", getCategory());
        intent.setClass(this, CustomerBookmarkActivity_.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMenuItemLongClick(View view, int i) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (null == produces) {
            Toast.makeText(this, "讀取資料中，請稍後再試。", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Produce> searchList = getSearchList(newText);
            adapter = new CustomerAdapter(this, searchList, prefs, shareContent.getBookmarkCategory());
            dataList.setAdapter(adapter);
        }
        return false;
    }

    private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();
        menuObjects.add(new MenuObject(R.drawable.ic_close_blue_36dp));
        menuObjects.add(new MenuObject(R.drawable.ic_refresh_blue_36dp, "重新整理"));
        menuObjects.add(new MenuObject(R.drawable.ic_swap_horiz_blue_36dp, "重量單位轉換"));
        menuObjects.add(new MenuObject(R.drawable.ic_list, "收藏清單"));
        menuObjects.add(new MenuObject(R.drawable.ic_share_blue_36dp, "分享給朋友"));
        return menuObjects;
    }

    private final View.OnLayoutChangeListener searchExpandHandler = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                                   int oldBottom) {
            SearchView searchView = (SearchView) v;
            if (searchView.isIconfiedByDefault() && !searchView.isIconified()) {
                // search got expanded from icon to search box, hide tabs to make space
                actionBarTitle.setVisibility(View.GONE);
                menu.setVisibility(View.GONE);
                GoogleAnalyticsSender.getInstance(CustomerActivity.this).send("click_search");
            }
        }
    };

    protected AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Produce object = (Produce) adapter.getItem(position);
            ColorDrawable color = (ColorDrawable) view.getBackground();

            if (getResources().getColor(R.color.highlight) == color.getColor()) {
                DatabaseController.delete(object.produceName, shareContent.getBookmarkCategory());
                Toast.makeText(CustomerActivity.this, R.string.remove_bookmark, Toast.LENGTH_SHORT).show();
            } else {
                DatabaseController.insertBookmark(object, shareContent.getBookmarkCategory());
                Toast.makeText(CustomerActivity.this, R.string.add_bookmark, Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();
        }
    };

    protected ArrayList<Produce> getSearchList(String newText) {
        ArrayList<Produce> list = new ArrayList<>();
        for (Produce data : produces) {
            if (data.produceName.contains(newText))
                list.add(data);
        }
        return list;
    }

    protected String getCategory() {
        return getIntent().getStringExtra("category");
    }

}
