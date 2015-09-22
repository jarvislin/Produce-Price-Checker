package com.jarvislin.producepricechecker.page.Index;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jarvislin.producepricechecker.ActivityComponentHelper;
import com.jarvislin.producepricechecker.page.PageListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;

import flow.path.Path;

/**
 * Created by jarvis on 15/9/22.
 */
@EView
public class IndexPage extends LinearLayout implements PageListener {

    @Bean
    protected IndexPresenter presenter;

    public IndexPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path.get(context);
    }

    @Override
    public void onPageStart(ActivityComponentHelper componentHelper) {
        presenter.setView(this);
//        componentHelper.hideToolbar();
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("HOME");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("SEC");

//create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(componentHelper.getActivity())
                .withToolbar(componentHelper.getToolbar())
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem().withName("NAME")
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

    }
}
