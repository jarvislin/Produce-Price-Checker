package com.jarvislin.producepricechecker.activity;

import android.content.Intent;
import android.widget.Toast;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.adapter.MerchantAdapter;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;

import database.Produce;

/**
 * Created by Jarvis Lin on 2015/6/20.
 */
@EActivity(R.layout.activity_merchant)
public class MerchantActivity extends CustomerActivity {

    @Override
    protected void handleData(ArrayList<Produce> list) {
        if (list == null || list.size() == 0) {
            ToolsHelper.showToast(this, R.string.error_network);
            finish();
        } else {
            produces = list;
            adapter = new MerchantAdapter(this, list, prefs, shareContent.getBookmarkCategory());
            dataList.setAdapter(adapter);
            dataList.setOnItemClickListener(itemClickListener);
            setBottomInfo();
        }
    }

    @Override
    protected void openBookmark() {
        Intent intent = new Intent();
        intent.putExtra("category", getCategory());
        intent.setClass(this, MerchantBookmarkActivity_.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (null == produces) {
            Toast.makeText(this, "讀取資料中，請稍後再試。", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Produce> searchList = getSearchList(newText);
            adapter = new MerchantAdapter(this, searchList, prefs, shareContent.getBookmarkCategory());
            dataList.setAdapter(adapter);
        }
        return false;
    }
}
