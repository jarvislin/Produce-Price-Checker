package com.jarvislin.producepricechecker.page.History;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.adapter.CustomerAdapter;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.page.PageListener;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.DateUtil;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.Preferences_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Collections;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/6.
 */
@EView
abstract class HistoryPage extends RelativeLayout implements PageListener, CompoundButton.OnCheckedChangeListener {
    private CustomerAdapter adapter;
    @Pref
    protected Preferences_ preferences;
    @ViewById
    protected FloatingActionsMenu fab;
    @ViewById
    protected FloatingActionButton subcategoryFilter;
    @ViewById
    protected ListView dataList;
    @ViewById
    protected TextView bottomInfo;
    @Bean
    protected HistoryPresenter presenter;
    private ArrayList<Produce> list;
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private Dialog dialog;
    private Handler uiHandler;

    public HistoryPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
        componentHelper.showToolbar(false);
        componentHelper.showArrow();
        if (componentHelper.getActivity().getSupportActionBar() != null) {
            componentHelper.getActivity().getSupportActionBar().setTitle(presenter.getMarketName());
        }
        init();
    }

    @Override
    public void onCreateOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {
// init searchView
        componentHelper.getActivity().getMenuInflater().inflate(R.menu.search, menu);
        SearchManager manager = (SearchManager) componentHelper.getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(componentHelper.getActivity().getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (null == list) {
                    Toast.makeText(getContext(), "讀取資料中，請稍後再試。", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Produce> searchList = getSearchList(query);
                    adapter.updateList(searchList);
                }
                return false;
            }
        });
    }

    private void init() {
        list = presenter.getProduces();
        String bookmark = "";
        if (!presenter.getProduces().isEmpty()) {
            bookmark = presenter.getProduces().get(0).mainCategory.equals(Constants.FRUIT) ? Constants.FRUIT_BOOKMARK : Constants.VEGETABLE_BOOKMARK;
        }
        adapter = getAdapter(getContext(), list, preferences, bookmark);
        dataList.setAdapter(adapter);
        subcategoryFilter.setVisibility(presenter.getProduces().get(0).mainCategory.equals(Constants.VEGETABLE) ? VISIBLE : GONE);
    }

    abstract CustomerAdapter getAdapter(Context context, ArrayList<Produce> list, Preferences_ pref, String category);

    @AfterViews
    protected void initViews() {
        setBottomInfo();
        initFooter();
    }

    private void initFooter() {
        uiHandler = new Handler(getContext().getMainLooper());
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.price_footer, null);
        footer.setOnClickListener(null);
        dataList.addFooterView(footer);
        dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && fab.getVisibility() == GONE) {
                    uiHandler.postDelayed(showButton, 600);
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    uiHandler.removeCallbacks(showButton);
                    if (fab.getVisibility() == VISIBLE) {
                        uiHandler.post(hideButton);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Click
    protected void convertUnit() {
        GoogleAnalyticsSender.getInstance(getContext()).send("click_convert_unit");
        float unit = preferences.unit().get();
        preferences.unit().put(unit < 1 ? 1.0f : 0.6f);
        adapter.notifyDataSetInvalidated();
        String unitText = (preferences.unit().get() < 1 ? "台斤" : "公斤");
        Toast.makeText(getContext(), "目前重量單位為：" + unitText, Toast.LENGTH_SHORT).show();
        setBottomInfo();
        fab.collapse();
    }


    @UiThread
    protected void setBottomInfo() {
        String unitText = (preferences.unit().get() < 1 ? "元/台斤" : "元/公斤");
        bottomInfo.setText("日期：" + presenter.getDate() + "　單位：" + unitText);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        list.clear();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                list.addAll(getListBySubcategory(checkBox.getId()));
            }
        }
        Collections.sort(list);
        adapter.updateList(list);
    }

    public ArrayList<Produce> getListBySubcategory(int id) {
        String subcategory = "";
        ArrayList<Produce> list = new ArrayList<>();
        switch (id) {
            case R.id.root:
                subcategory = "root";
                break;
            case R.id.leaf:
                subcategory = "leaf";
                break;
            case R.id.flower_fruit:
                subcategory = "flower_fruit";
                break;
            case R.id.mushroom:
                subcategory = "mushroom";
                break;
            case R.id.pickle:
                subcategory = "pickle";
                break;
        }
        for (Produce produce : presenter.getProduces()) {
            if (produce.subCategory.equals(subcategory)) {
                list.add(produce);
            }
        }
        return list;
    }

    private Runnable showButton = new Runnable() {
        @Override
        public void run() {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
            fab.startAnimation(animation);
            fab.setVisibility(VISIBLE);
        }
    };

    private Runnable hideButton = new Runnable() {
        @Override
        public void run() {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);
            fab.startAnimation(animation);
            fab.setVisibility(GONE);
        }
    };

    @Click
    protected void subcategoryFilter() {
        GoogleAnalyticsSender.getInstance(getContext()).send("click_filter");
        // show filter dialog
        if (dialog == null) {
            dialog = new Dialog(getContext(), R.style.alertDialog);
            dialog.setContentView(R.layout.dialog_filter);
            Button submit = (Button) dialog.findViewById(R.id.dismiss);
            submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        }
        CheckBox root = (CheckBox) dialog.findViewById(R.id.root);
        CheckBox leaf = (CheckBox) dialog.findViewById(R.id.leaf);
        CheckBox flowerFruit = (CheckBox) dialog.findViewById(R.id.flower_fruit);
        CheckBox mushroom = (CheckBox) dialog.findViewById(R.id.mushroom);
        CheckBox pickle = (CheckBox) dialog.findViewById(R.id.pickle);

        root.setOnCheckedChangeListener(this);
        leaf.setOnCheckedChangeListener(this);
        flowerFruit.setOnCheckedChangeListener(this);
        mushroom.setOnCheckedChangeListener(this);
        pickle.setOnCheckedChangeListener(this);

        checkBoxes.clear();
        checkBoxes.add(root);
        checkBoxes.add(leaf);
        checkBoxes.add(flowerFruit);
        checkBoxes.add(mushroom);
        checkBoxes.add(pickle);

        dialog.show();
        fab.collapse();
    }

    protected ArrayList<Produce> getSearchList(String newText) {
        ArrayList<Produce> list = new ArrayList<>();
        for (Produce data : this.list) {
            if (data.produceName.contains(newText))
                list.add(data);
        }
        return list;
    }
}
