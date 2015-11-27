package com.jarvislin.producepricechecker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.jarvislin.producepricechecker.page.History.HistoryPath;
import com.jarvislin.producepricechecker.path.FrameLayoutContainerView;
import com.jarvislin.producepricechecker.path.GsonParceler;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import flow.Flow;
import flow.FlowDelegate;
import flow.History;

@EActivity(R.layout.activity_blank)
public class BlankActivity extends AppCompatActivity implements Flow.Dispatcher, ActivityComponentHelper {
    private FlowDelegate flowSupport;
    private Bundle savedInstanceState;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.container)
    FrameLayoutContainerView container;
    @Extra
    protected HistoryPath historyPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        overridePendingTransition(R.anim.anim_page_left_in, R.anim.anim_page_left_out);
    }

    @AfterViews
    public void afterViews() {
        setSupportActionBar(toolbar);
        FlowDelegate.NonConfigurationInstance nonConfig = (FlowDelegate.NonConfigurationInstance)
                getLastCustomNonConfigurationInstance();

        if(historyPath == null) this.finish();
        flowSupport = FlowDelegate.onCreate(nonConfig, getIntent(), savedInstanceState
                , new GsonParceler(), History.single(historyPath), this);
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
    }

    @Override
    public void showArrow() {
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_page_right_in, R.anim.anim_page_right_out);
    }
}
