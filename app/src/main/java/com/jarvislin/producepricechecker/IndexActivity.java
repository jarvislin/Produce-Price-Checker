package com.jarvislin.producepricechecker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.jarvislin.producepricechecker.util.Constants;
import com.jarvislin.producepricechecker.util.GoogleAnalyticsSender;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_index)
public class IndexActivity extends AppCompatActivity {
    @ViewById
    Button vegetable;
    @ViewById
    Button fruit;
    @ViewById
    Button settings;

    @AfterViews
    protected void init(){
        getSupportActionBar().hide();
    }

    @Click
    protected void fruit() {
        GoogleAnalyticsSender.getInstance(this).send("click_fruit");
        Intent intent = new Intent(IndexActivity.this, DataListActivity.class);
        intent.putExtra("type", Constants.FRUIT);
        IndexActivity.this.startActivity(intent);
    }

    @Click
    public void vegetable(View view) {
        GoogleAnalyticsSender.getInstance(this).send("click_vegetable");
        Intent intent = new Intent(IndexActivity.this, DataListActivity.class);
        intent.putExtra("type", Constants.VEGETABLE);
        IndexActivity.this.startActivity(intent);
    }

    @Click
    public void settings(View view) {
        GoogleAnalyticsSender.getInstance(this).send("click_settings");
        Intent intent = new Intent(IndexActivity.this, SettingsActivity.class);
        IndexActivity.this.startActivity(intent);
    }
}
