package com.jarvislin.producepricechecker.page.PriceList;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.ApiClient;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.ShareContent;
import com.jarvislin.producepricechecker.adapter.CustomerAdapter;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.page.PageListener;
import com.jarvislin.producepricechecker.util.DateUtil;
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
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

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
 * Created by jarvis on 15/9/23.
 */
@EView
public class CustomerPage extends RelativeLayout implements PageListener {
    @Bean
    CustomerPresenter presenter;
    @ViewById
    ListView dataList;
    @ViewById
    TextView bottomInfo;

    @RestService
    ApiClient client;

    @Pref
    Preferences_ prefs;
    private ArrayList<Produce> produces;
    private CustomerAdapter adapter;

    public CustomerPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

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

        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(componentHelper.getActivity())
                .withToolbar(componentHelper.getToolbar())
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("111"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("gg"),
                        new SecondaryDrawerItem().withName("hihihi")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return true;
                    }
                })
                .build();

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

    @UiThread
    protected void handleData(ArrayList<Produce> list, ProduceData shareContent) {
        if (list == null || list.size() == 0) {
            Toast.makeText(getContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
            Flow.get(getContext()).goBack();
        } else {
            produces = list;
            adapter = new CustomerAdapter(getContext(), list, prefs, shareContent.getBookmarkCategory());
            dataList.setAdapter(adapter);
            dataList.setOnItemClickListener(itemClickListener(shareContent));
            setBottomInfo(shareContent);
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

    protected void setBottomInfo(ProduceData data) {
        String unitText = (prefs.unit().get() < 1 ? "元/台斤" : "元/公斤");
        bottomInfo.setText(DateUtil.getOffsetInWords(DateUtil.getOffset(produces.get(0).transactionDate)) + "　" + data.getMarketName(getContext()) + "　單位：" + unitText);
    }


}
