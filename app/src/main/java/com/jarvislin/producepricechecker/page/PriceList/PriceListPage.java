package com.jarvislin.producepricechecker.page.PriceList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.adapter.CustomerAdapter;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.page.PageListener;
import com.jarvislin.producepricechecker.path.HandlesBack;
import com.jarvislin.producepricechecker.util.DateUtil;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import flow.Flow;
import flow.path.Path;

/**
 * Created by jarvis on 15/9/25.
 */
@EView
public abstract class PriceListPage extends RelativeLayout implements PageListener, HandlesBack {
    @Bean
    PriceListPresenter presenter;
    @ViewById
    ListView dataList;
    @ViewById
    TextView bottomInfo;

    @Pref
    Preferences_ prefs;

    private ArrayList<Produce> produces;
    private CustomerAdapter adapter;
    private ProduceData data;
    private Drawer result;

    public PriceListPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    abstract protected CustomerAdapter getAdapter(Context context, ArrayList<Produce> list, Preferences_ prefs, String bookmarkCategory);

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
        componentHelper.showToolbar(false);


        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(componentHelper.getActivity())
                .withHeaderBackground(R.drawable.index_background)
                .withProfileImagesClickable(false)
                .withProfileImagesVisible(false)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com")

                )
                .build();

        result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(componentHelper.getActivity())
                .withToolbar(componentHelper.getToolbar())
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("行情表").withSetSelected(true),
                        new PrimaryDrawerItem().withName("Refresh").withSelectable(false).withSetSelected(false),
                        new DividerDrawerItem().withSelectable(false),
                        new PrimaryDrawerItem().withName("Transfer").withSelectable(false),
                        new PrimaryDrawerItem().withName("Bookmark").withSelectable(false),
                        new SecondaryDrawerItem().withName("Share").withSelectable(false),
                        new SecondaryDrawerItem().withName("Rating").withSelectable(false),
                        new SecondaryDrawerItem().withName("Facebook").withSelectable(false),
                        new SecondaryDrawerItem().withName("Q&A").withSelectable(false),
                        new SecondaryDrawerItem().withName("Contact").withSelectable(true)
                )
                .build();
        result.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                // do something with the clicked item :D
                switch (position) {
                    case 2:
                        GoogleAnalyticsSender.getInstance(getContext()).send("click_update");
                        presenter.loadData(presenter.getMarketNumber());
                        break;
                    case 3:
                        GoogleAnalyticsSender.getInstance(getContext()).send("click_convert_unit");
                        float unit = prefs.unit().get();
                        prefs.unit().put(unit < 1 ? 1.0f : 0.6f);
                        adapter.notifyDataSetInvalidated();
                        String unitText = (prefs.unit().get() < 1 ? "台斤" : "公斤");
                        Toast.makeText(getContext(), "目前重量單位為：" + unitText, Toast.LENGTH_SHORT).show();
                        setBottomInfo();
                        break;
                    case 4:
                        GoogleAnalyticsSender.getInstance(getContext()).send("click_bookmark");
                        openBookmark();
                        break;
                    case 5:
                        GoogleAnalyticsSender.getInstance(getContext()).send("click_share");
                        ToolsHelper.shareText(getContext(), "分享：", getContext().getString(R.string.share_text));
                        break;
                }
                result.closeDrawer();
                return true;
            }
        });

    }

    @Override
    public void onPrepareOptionsMenu(ActivityComponentHelper componentHelper, Menu menu) {
        componentHelper.getActivity().getMenuInflater().inflate(R.menu.search, menu);
        SearchManager manager = (SearchManager) componentHelper.getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(componentHelper.getActivity().getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
//                loadHistory(query);
                return true;
            }
        });
    }

    @AfterViews
    protected void initFooter(){
        dataList.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.price_footer, null));
    }

    @UiThread
    protected void handleData(ArrayList<Produce> list, ProduceData data) {
        if (list == null || list.size() == 0) {
            Toast.makeText(getContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
            Flow.get(getContext()).goBack();
        } else {
            produces = list;
            this.data = data;
            adapter = getAdapter(getContext(), list, prefs, data.getBookmarkCategory());
            dataList.setAdapter(adapter);
            dataList.setOnItemClickListener(itemClickListener(data));
            setBottomInfo();
        }
    }

    protected AdapterView.OnItemClickListener itemClickListener(final ProduceData data) {
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
            }
        };
    }

    protected void openBookmark() {
//        Intent intent = new Intent();
//        intent.putExtra("category", getCategory());
//        intent.setClass(this, CustomerBookmarkActivity_.class);
//        startActivityForResult(intent, 0);
    }

    @UiThread
    protected void setBottomInfo() {
        String unitText = (prefs.unit().get() < 1 ? "元/台斤" : "元/公斤");
        bottomInfo.setText(DateUtil.getOffsetInWords(DateUtil.getOffset(produces.get(0).transactionDate)) + "　" + data.getMarketName(getContext(), produces.get(0).marketNumber) + "　單位：" + unitText);
    }

    @Override
    public boolean onBackPressed() {
        if(result.isDrawerOpen()) {
            result.closeDrawer();
            return true;
        } else {
            return Flow.get(getContext()).goBack();
        }
    }
}
