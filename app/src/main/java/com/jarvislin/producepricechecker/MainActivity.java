package com.jarvislin.producepricechecker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.jarvislin.producepricechecker.page.Index.IndexPath;
import com.jarvislin.producepricechecker.page.Index.IndexPresenter;
import com.jarvislin.producepricechecker.path.FrameLayoutContainerView;
import com.jarvislin.producepricechecker.path.GsonParceler;
import com.jarvislin.producepricechecker.util.ToolsHelper;
import com.mikepenz.materialdrawer.DrawerBuilder;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @AfterViews
    public void afterViews() {
        setSupportActionBar(toolbar);
        initToolbar();
        FlowDelegate.NonConfigurationInstance nonConfig = (FlowDelegate.NonConfigurationInstance)
                getLastCustomNonConfigurationInstance();
        flowSupport = FlowDelegate.onCreate(nonConfig, getIntent(), savedInstanceState, new
                GsonParceler(), History.single(new IndexPath()), this);
    }

    private void initToolbar() {
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
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
    public void showToolbar(boolean showHomeAsUp) {
        toolbar.setVisibility(View.VISIBLE);
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
