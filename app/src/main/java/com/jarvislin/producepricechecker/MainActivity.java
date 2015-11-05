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
import android.widget.Spinner;

import com.jarvislin.producepricechecker.page.Index.IndexPath;
import com.jarvislin.producepricechecker.page.Questions.QuestionsPath;
import com.jarvislin.producepricechecker.path.FrameLayoutContainerView;
import com.jarvislin.producepricechecker.path.GsonParceler;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.ToolsHelper;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;
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

        drawer = getDrawer();
    }

    private Drawer getDrawer() {
        // init Drawer
        // Create the AccountHeader
        if(drawer == null) {
            AccountHeader header = new AccountHeaderBuilder()
                    .withActivity(MainActivity.this)
                    .withHeaderBackground(R.drawable.fruits_vegetable_food)
                    .withProfileImagesClickable(false)
                    .withProfileImagesVisible(false)
                    .withSelectionListEnabledForSingleProfile(false)
//                .addProfiles(
//                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com")
//
//                )
                    .build();

            final Drawer newDrawer = new DrawerBuilder()
                    .withAccountHeader(header)
                    .withActivity(MainActivity.this)
                    .withToolbar(toolbar)
                    .addDrawerItems(
                            new SecondaryDrawerItem().withName("行情表").withSetSelected(true).withSelectedTextColor(getResources().getColor(R.color.main_blue)).withIcon(new IconicsDrawable(this)
                                    .icon(CommunityMaterial.Icon.cmd_format_list_bulleted)
                                    .color(getResources().getColor(android.R.color.darker_gray))
                                    .sizeDp(18)),
                            new SecondaryDrawerItem().withName("歷史價格").withSelectable(false).withIcon(new IconicsDrawable(this)
                                            .icon(CommunityMaterial.Icon.cmd_calendar)
                                            .color(getResources().getColor(android.R.color.darker_gray))
                                            .sizeDp(18)
                            ),
                            new SecondaryDrawerItem().withName("常見問題").withSelectable(false).withIcon(new IconicsDrawable(this)
                                            .icon(CommunityMaterial.Icon.cmd_help)
                                            .color(getResources().getColor(android.R.color.darker_gray))
                                            .sizeDp(18)
                            ),
                            new DividerDrawerItem().withSelectable(false),
                            new SecondaryDrawerItem().withName("分享").withSelectable(false).withIcon(new IconicsDrawable(this)
                                    .icon(CommunityMaterial.Icon.cmd_share_variant)
                                    .color(getResources().getColor(android.R.color.darker_gray))
                                    .sizeDp(18)),
                            new SecondaryDrawerItem().withName("粉絲團").withSelectable(false).withIcon(new IconicsDrawable(this)
                                    .icon(CommunityMaterial.Icon.cmd_facebook)
                                    .color(getResources().getColor(android.R.color.darker_gray))
                                    .sizeDp(18)),
                            new SecondaryDrawerItem().withName("聯繫作者").withSelectable(false).withIcon(new IconicsDrawable(this)
                                    .icon(CommunityMaterial.Icon.cmd_comment_processing_outline)
                                    .color(getResources().getColor(android.R.color.darker_gray))
                                    .sizeDp(18))
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
            newDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    // do something with the clicked item :D
                    Log.e("POS", position + "");
                    switch (position) {
                        case 1:
                            GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_price_list");
                            break;
                        case 2:
                            GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_history");
                            EventBus.getDefault().post(new Events.onHistoryClicked());
                            break;
                        case 3:
                            GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_questions");
                            Flow.get(MainActivity.this).set(new QuestionsPath());
                            break;
                        case 4:

                            break;
                        case 5:
                            GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_share");
                            ToolsHelper.shareText(MainActivity.this, "分享：", MainActivity.this.getString(R.string.share_text));

                            break;
                        case 6:
                            GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_FB");
                            ToolsHelper.openUrl(MainActivity.this, "https://www.facebook.com/produce.price.checker");
                            break;
                        case 7:
                            GoogleAnalyticsSender.getInstance(MainActivity.this).send("click_contact");
                            ToolsHelper.openUrl(MainActivity.this, "http://jarvislin.com/contact/");
                    }
                    newDrawer.closeDrawer();
                    return true;
                }
            });
            return newDrawer;
        }
        return drawer;
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
        if(drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        }else if (!container.onBackPressed()) {
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
    public void showToolbar(boolean showSpinner) {
        toolbar.setVisibility(View.VISIBLE);
        Spinner spinner = (Spinner) toolbar.findViewById(R.id.spinner_nav);
        spinner.setVisibility(showSpinner ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showHamburger() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getDrawer().getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    @Override
    public void showArrow() {
        getDrawer().getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        container.onCreateOptionsMenu(this, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
