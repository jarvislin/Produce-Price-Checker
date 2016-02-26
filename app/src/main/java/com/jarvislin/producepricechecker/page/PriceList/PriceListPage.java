package com.jarvislin.producepricechecker.page.PriceList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.Events;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.adapter.CustomerAdapter;
import com.jarvislin.producepricechecker.custom.CalendarDialog;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.HistoryDirectory;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.page.Details.DetailsPath;
import com.jarvislin.producepricechecker.page.PageListener;
import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.DateUtil;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import de.greenrobot.event.EventBus;
import flow.Flow;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by jarvis on 15/9/25.
 */
@EView
public abstract class PriceListPage extends RelativeLayout implements PageListener, CompoundButton.OnCheckedChangeListener, CalendarDialog.OnClickDateListener {
    private Activity activity;
    @Bean
    protected PriceListPresenter presenter;
    @ViewById
    protected ListView dataList;
    @ViewById
    protected TextView bottomInfo;
    @ViewById
    protected FloatingActionsMenu fab;
    @ViewById
    protected FloatingActionButton subcategoryFilter;
    @ViewById
    protected FloatingActionButton update;
    @Pref
    protected Preferences_ prefs;


    private ArrayList<Produce> produces;
    private ArrayList<Produce> filterList;
    private CustomerAdapter adapter;

    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    private Dialog dialog;
    private Handler uiHandler;
    private boolean hasInitSpinner = false;
    private CalendarDialog historyDialog;

    public PriceListPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    abstract protected CustomerAdapter getAdapter(Context context, ArrayList<Produce> list, Preferences_ prefs, String bookmarkCategory);

    abstract protected boolean enableSpinner();

    abstract protected boolean enableRefresh();

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
        activity = componentHelper.getActivity();
        EventBus.getDefault().register(this);
        componentHelper.showToolbar(true);
        componentHelper.showHamburger();
        componentHelper.getToolbar().setTitle("");
        update.setVisibility(enableRefresh() ? VISIBLE : GONE);
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
                if (null == produces) {
                    Toast.makeText(getContext(), "讀取資料中，請稍後再試。", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Produce> searchList = getSearchList(query);
                    adapter = getAdapter(getContext(), searchList, prefs, presenter.getProduceData().getBookmarkCategory());
                    dataList.setAdapter(adapter);
                }
                return false;
            }
        });

        if (enableSpinner()&&!hasInitSpinner) {
            // init Spinner
            Spinner spinner = (Spinner) componentHelper.getToolbar().findViewById(R.id.spinner_nav);
            String[] array = getContext().getResources().getStringArray(presenter.getProduceData().getMarketsTitleResId());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner, array);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ToolsHelper.showProgressDialog(getContext(), true);
                    search.setIconified(true);
                    presenter.loadData(getResources().getStringArray(presenter.getProduceData().getMarketNumbersResId())[position]);
                    ToolsHelper.closeProgressDialog(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            String marketNumber = "";
            if (presenter.isLoaderAlive()) {
                marketNumber = presenter.getMarketNumber();
            }
            //this will trigger onItemSelected
            spinner.setSelection(presenter.getProduceData().getMarketTitlePosition(getContext(), marketNumber));
            hasInitSpinner = true;
        }

    }


    protected ArrayList<Produce> getSearchList(String newText) {
        ArrayList<Produce> list = new ArrayList<>();
        for (Produce data : filterList) {
            if (data.produceName.contains(newText))
                list.add(data);
        }
        return list;
    }

    @Click
    protected void convertUnit() {
        GoogleAnalyticsSender.getInstance(getContext()).send("click_convert_unit");
        float unit = prefs.unit().get();
        prefs.unit().put(unit < 1 ? 1.0f : 0.6f);
        adapter.notifyDataSetInvalidated();
        String unitText = (prefs.unit().get() < 1 ? "台斤" : "公斤");
        Toast.makeText(getContext(), "目前重量單位為：" + unitText, Toast.LENGTH_SHORT).show();
        setBottomInfo();
        fab.collapse();
    }

    @Click
    protected void update() {
        GoogleAnalyticsSender.getInstance(getContext()).send("click_update");
        presenter.loadData();
        fab.collapse();
    }

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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        filterList.clear();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                filterList.addAll(getListBySubcategory(checkBox.getId()));
            }
        }
        Collections.sort(filterList);
        adapter = getAdapter(getContext(), filterList, prefs, presenter.getProduceData().getBookmarkCategory());
        dataList.setAdapter(adapter);
    }

    @AfterViews
    protected void initFooter() {
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

    @UiThread
    protected void handleData(ArrayList<Produce> list) {
        if (list == null || list.size() == 0) {
            Toast.makeText(getContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
            Flow.get(getContext()).goBack();
        } else {
            Collections.sort(list);
            produces = list;
            filterList = new ArrayList<>(list);
            adapter = getAdapter(getContext(), list, prefs, presenter.getProduceData().getBookmarkCategory());
            dataList.setAdapter(adapter);
            dataList.setOnItemClickListener(itemClickListener(presenter.getProduceData()));
            dataList.setOnItemLongClickListener(itemLongClickListener(presenter.getProduceData()));
            setBottomInfo();
            subcategoryFilter.setVisibility(presenter.getProduceData().getCategory().equals(Constants.VEGETABLE) ? VISIBLE : GONE);
            //reset dialog status
            resetFilter();
        }
    }

    private AdapterView.OnItemLongClickListener itemLongClickListener(final ProduceData data) {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Produce object = (Produce) adapter.getItem(position);
                ColorDrawable color = (ColorDrawable) view.getBackground();

                if (getResources().getColor(R.color.highlight) == color.getColor()) {
                    DatabaseController.delete(object.produceName, data.getBookmarkCategory());
                    Toast.makeText(getContext(), R.string.remove_bookmark, Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseController.insertBookmark(object, data.getBookmarkCategory());
                    Toast.makeText(getContext(), R.string.add_bookmark, Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
                return true;
            }
        };
    }

    private void resetFilter() {
        for (CheckBox box : checkBoxes) {
            box.setChecked(true);
        }
    }

    protected AdapterView.OnItemClickListener itemClickListener(final ProduceData data) {
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produce object = (Produce) adapter.getItem(position);
                presenter.getChartItems(object);
            }
        };
    }



    public void onEvent(Events.onHistoryClicked event) {
        presenter.fetchHistoryDirectory();
    }

    @UiThread
    protected void setBottomInfo() {
        String unitText = (prefs.unit().get() < 1 ? "元/台斤" : "元/公斤");
        bottomInfo.setText("日期：" + DateUtil.getOffsetInWords(DateUtil.getOffset(produces.get(0).transactionDate)) + "　單位：" + unitText);
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
        for (Produce produce : produces) {
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

    @UiThread
    public void showHistoryDialog(final HistoryDirectory directory) {
        if (historyDialog == null) {
            historyDialog = new CalendarDialog(getContext(), directory, this);
        } else {
            historyDialog.setHistory(directory);
            historyDialog.resetDate();
        }
        historyDialog.show();
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void onDateClicked(CalendarDay date) {
        String month = String.valueOf(date.getMonth() + 1);
        String day = String.valueOf(date.getDay());
        month = month.length() < 2 ? "0" + month : month;
        day = day.length() < 2 ? "0" + day : day;
        presenter.fetchHistory(String.valueOf(date.getYear() - 1911), month + "." + day);
    }
}
