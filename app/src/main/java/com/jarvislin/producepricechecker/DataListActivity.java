package com.jarvislin.producepricechecker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;


public class DataListActivity extends Activity {

    private UpdateTask mUpdateTask = new UpdateTask(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_data_list);

        mUpdateTask.execute(getType());

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        //findViews


    }

    private int getType() {
        return getIntent().getIntExtra("type", -1);
    }

    public void loadDataMap(DataFetcher dataFetcher){
        HashMap<Integer, ProduceData> dataMap = (dataFetcher.mDataExist) ? dataFetcher.getProduceDataMap() : null;
        if(dataMap == null)
            ;//error
        else
            ;//keep doing
    }
}


