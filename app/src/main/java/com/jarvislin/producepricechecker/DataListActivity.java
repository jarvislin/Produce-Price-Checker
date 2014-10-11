package com.jarvislin.producepricechecker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.HashMap;


public class DataListActivity extends Activity {

    private UpdateTask mUpdateTask = new UpdateTask(this);
    private TableLayout mTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_data_list);

        mUpdateTask.execute(getType());

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        findViews();


    }

    private void findViews() {
        mTable = (TableLayout)findViewById(R.id.tableLayout);
    }

    private int getType() {
        return getIntent().getIntExtra("type", -1);
    }

    public void loadDataMap(DataFetcher dataFetcher){
        HashMap<Integer, ProduceData> dataMap = (dataFetcher.mDataExist) ? dataFetcher.getProduceDataMap() : null;
        if(dataMap == null)
            Tools.showSiteErrorMessage(this);//site error
        else{
            ProduceData tempProduceData;
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View row;
            for(int i = 0 ; i < dataMap.size() ; i++ ){
                row = inflater.inflate(R.layout.data_row, null);
                tempProduceData = dataMap.get(i);
                Log.d("gg", dataMap.get(i).getName());
                addRow(row, tempProduceData);
            }
        }
    }

    private void addRow(View row, ProduceData produceData) {
        TextView name = (TextView)row.findViewById(R.id.name);
//        TextView type = (TextView)row.findViewById(R.id.type);
        TextView topPrice = (TextView)row.findViewById(R.id.topPrice);
        TextView midPrice = (TextView)row.findViewById(R.id.midPrice);
        TextView lowPrice = (TextView)row.findViewById(R.id.lowPrice);
        TextView avgPrice = (TextView)row.findViewById(R.id.avgPrice);

        String completedName = (produceData.getType().length() <= 1) ? produceData.getName() : produceData.getType() + "\n" + produceData.getName() ;
        name.setText(completedName);
//        type.setText("");
        topPrice.setText(produceData.getTopPrice());
        midPrice.setText(produceData.getMidPrice());
        lowPrice.setText((produceData.getLowPrice()));
        avgPrice.setText(produceData.getAvgPrice());

        mTable.addView(row);
    }
}


