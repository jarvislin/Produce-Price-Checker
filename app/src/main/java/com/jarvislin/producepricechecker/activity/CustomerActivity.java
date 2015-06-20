package com.jarvislin.producepricechecker.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvislin.producepricechecker.DataFetcher;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.adapter.CustomerAdapter;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

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
    protected ArrayList<Produce> produces = new ArrayList<>();
    protected CustomerAdapter adapter;
    @ViewById
    ListView dataList;
    @Bean
    DataFetcher dataFetcher;

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

        loadData();
    }

    @Background
    protected void loadData() {
        ToolsHelper.showProgressDialog(this, false);
        //show
        String updateDate = getType().equals(Constants.FRUIT) ? prefs.fruitUpdateDate().get() : prefs.vegetableUpdateDate().get();
        if (updateDate.equals(ToolsHelper.getCurrentDate())) {
            loadClientData();
        } else if (ToolsHelper.isNetworkAvailable(this)) {
            downloadData();
        } else if (DatabaseController.getProduces(getType()).size() > 0) {
            loadClientData();
        } else {
            handleData(null);
        }
        ToolsHelper.closeProgressDialog(false);
    }

    protected void downloadData() {
        String url = (getType().equals(Constants.FRUIT)) ? Constants.FRUIT_URL : Constants.VEGETABLE_URL;
        handleData(dataFetcher.getProduces(url, getType()));
        updateDatabase();
    }

    public void loadClientData() {
        produces = DatabaseController.getProduces(getType());
        handleData(produces);
    }

    @UiThread
    protected void handleData(ArrayList<Produce> list) {
        if (list == null) {
            ToolsHelper.showNetworkErrorMessage(this);
            finish();
        } else {
            produces = list;
            adapter = new CustomerAdapter(this, list, prefs, getBookmarkKind());
            dataList.setAdapter(adapter);
            dataList.setOnItemClickListener(itemClickListener);
        }
    }

    @Background
    protected void updateDatabase() {
        if (produces != null && !produces.isEmpty()) {
            DatabaseController.clearTable(getType());
            for (Produce produce : produces) {
                produce.save();
            }
            if (getType().equals(Constants.FRUIT)) {
                prefs.fruitUpdateDate().put(ToolsHelper.getCurrentDate());
                DatabaseController.updateBookmark(produces, Constants.FRUIT_BOOKMARK);
                prefs.fruitUpdateDate().put(produces.get(0).date);
            } else {
                prefs.vegetableUpdateDate().put(ToolsHelper.getCurrentDate());
                DatabaseController.updateBookmark(produces, Constants.VEGETABLE_BOOKMARK);
                prefs.vegetableUpdateDate().put(produces.get(0).date);
            }
        }
    }

    @Override
    public void onMenuItemClick(View view, int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                GoogleAnalyticsSender.getInstance(this).send("click_info");
                showInfo();
                break;
            case 2:
                GoogleAnalyticsSender.getInstance(this).send("click_update");
                loadData();
                break;
            case 3:
                GoogleAnalyticsSender.getInstance(this).send("click_convert_unit");
                float unit = prefs.unit().get();
                prefs.unit().put(unit < 1 ? 1.0f : 0.6f);
                adapter.notifyDataSetInvalidated();
                Toast.makeText(this, "目前重量單位為：" + (prefs.unit().get() < 1 ? "台斤" : "公斤"), Toast.LENGTH_SHORT).show();
                break;
            case 4:
                GoogleAnalyticsSender.getInstance(this).send("click_bookmark");
                openBookmark();
                break;
        }
    }

    public void showInfo() {
        String[] date = ToolsHelper.getDateParam(ToolsHelper.getOffset(produces.get(0).date));

        final Dialog dialog = new Dialog(this, R.style.alertDialog);
        dialog.setContentView(R.layout.dialog_info);

        TextView message = (TextView) dialog.findViewById(R.id.info_text);
        message.setText(
                "資料日期：" + date[0] + "/" + date[1] + "/" + date[2] + ToolsHelper.getOffsetInWords(ToolsHelper.getOffset(produces.get(0).date)) + "\n" +
                        "單位：" + ToolsHelper.getUnitInWords(prefs.unit().get()) + "\n" +
                        "市場：" + ToolsHelper.getMarketName(prefs.marketList().get())
        );

        Button dismiss = (Button) dialog.findViewById(R.id.info_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void openBookmark() {
        Intent intent = new Intent();
        intent.putExtra("type", getType());
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
            adapter = new CustomerAdapter(this, searchList, prefs, getBookmarkKind());
            dataList.setAdapter(adapter);
        }
        return false;
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
                DatabaseController.delete(object.name, object.type, getBookmarkKind());
                Toast.makeText(CustomerActivity.this, "已從清單移除項目", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseController.insertBookmark(object, getBookmarkKind());
                Toast.makeText(CustomerActivity.this, "已將項目加入清單", Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();
        }
    };

    protected ArrayList<Produce> getSearchList(String newText) {
        ArrayList<Produce> list = new ArrayList<>();
        for (Produce data : produces) {
            if (data.name.contains(newText) || data.type.contains(newText))
                list.add(data);
        }
        return list;
    }

    protected String getType() {
        return getIntent().getStringExtra("type");
    }

    protected String getBookmarkKind() {
        return getType().equals(Constants.FRUIT) ? Constants.FRUIT_BOOKMARK : Constants.VEGETABLE_BOOKMARK;
    }
}
