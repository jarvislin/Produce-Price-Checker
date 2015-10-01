package com.jarvislin.producepricechecker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jarvislin.producepricechecker.page.Index.IndexPath;
import com.jarvislin.producepricechecker.page.Questions.QuestionsPath;
import com.jarvislin.producepricechecker.path.FrameLayoutContainerView;
import com.jarvislin.producepricechecker.path.GsonParceler;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.ToolsHelper;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import flow.Flow;
import flow.FlowDelegate;
import flow.History;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements Flow.Dispatcher, ActivityComponentHelper {
    private FlowDelegate flowSupport;
    private Bundle savedInstanceState;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.container)
    FrameLayoutContainerView container;
    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @AfterViews
    public void afterViews() {
        setSupportActionBar(toolbar);
        FlowDelegate.NonConfigurationInstance nonConfig = (FlowDelegate.NonConfigurationInstance)
                getLastCustomNonConfigurationInstance();
        flowSupport = FlowDelegate.onCreate(nonConfig, getIntent(), savedInstanceState
                , new GsonParceler(), History.single(new IndexPath()), this);

        initDrawer();
    }

    private void initDrawer() {
        // init Drawer
        // Create the AccountHeader
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(MainActivity.this)
                .withHeaderBackground(R.drawable.index_background)
                .withProfileImagesClickable(false)
                .withProfileImagesVisible(false)
                .withSelectionListEnabledForSingleProfile(false)
//                .addProfiles(
//                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com")
//
//                )
                .build();

        drawer = new DrawerBuilder()
                .withAccountHeader(header)
                .withActivity(MainActivity.this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("行情表").withSetSelected(true),
                        new PrimaryDrawerItem().withName("書籤"),
                        new PrimaryDrawerItem().withName("常見問題"),
                        new DividerDrawerItem().withSelectable(false),
                        new SecondaryDrawerItem().withName("分享").withSelectable(false),
                        new SecondaryDrawerItem().withName("評分").withSelectable(false),
                        new SecondaryDrawerItem().withName("粉絲團").withSelectable(false),
                        new SecondaryDrawerItem().withName("聯繫作者").withDescription("hihi").withSelectable(false)
                )
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                        //if the back arrow is shown. close the activity
                        Flow.get(getActivity()).goBack();
                        //return true if we have consumed the event
                        return true;
                    }
                })
                .build();
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                // do something with the clicked item :D
                Log.e("POS", position + "");
                switch (position) {
                    case 1:
                        GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_price_list");
                        break;
                    case 2:
                        GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_bookmark");
//                        openBookmark();
                        break;
                    case 3:
                        GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_questions");
                        Flow.get(MainActivity.this).set(new QuestionsPath());
//                        Flow.get(getContext()).setHistory(History.single(new QuestionsPath()), Flow.Direction.REPLACE);
                        break;
                    case 4:

                        break;
                    case 5:
                        GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_share");
                        ToolsHelper.shareText(MainActivity.this, "分享：", MainActivity.this.getString(R.string.share_text));

                        break;
                    case 6:
                        GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_FB");
                        break;
                }
                drawer.closeDrawer();
                return true;
            }
        });
    }


    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return flowSupport.onRetainNonConfigurationInstance();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        flowSupport.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flowSupport.onResume();
        container.onPageResume();
    }

    @Override
    protected void onPause() {
        flowSupport.onPause();
        super.onPause();
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        Object service = null;
        if (flowSupport != null) {
            service = flowSupport.getSystemService(name);
        }
        return service != null ? service : super.getSystemService(name);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        flowSupport.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (!container.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showHamburger() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    @Override
    public void showArrow() {
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void dispatch(Flow.Traversal traversal, final Flow.TraversalCallback callback) {
        ToolsHelper.hideSoftKeyboard(this);
        container.dispatch(traversal, new Flow.TraversalCallback() {
            @Override
            public void onTraversalCompleted() {
                invalidateOptionsMenu();
                callback.onTraversalCompleted();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        container.onPrepareOptionsMenu(this, menu);
        return super.onPrepareOptionsMenu(menu);
    }
}
