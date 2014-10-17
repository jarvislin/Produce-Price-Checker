package com.jarvislin.producepricechecker;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;


public class DataListActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();
    private TableLayout mTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((isCustomerMode()) ? R.layout.customer_data_list : R.layout.general_data_list);
        update(null);

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar_refresh);
    }

    public void update(View view) {
        if(!ToolsHelper.isNetworkAvailable(this)) {
            ToolsHelper.showNetworkErrorMessage(this);
            this.finish();
        } else {
            new UpdateTask(this).execute(getType());
            findViews();
        }
    }

    public void back(View view){
        finish();
    }


    private void findViews() {
        mTable = (TableLayout)findViewById(R.id.tableLayout);
    }

    private boolean isCustomerMode(){
//        Log.d(TAG, "CustomerMode = " + String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("role", false)));
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("role", false);
    }

    private int getType() {
        return getIntent().getIntExtra("type", -1);
    }

    public void loadDataMap(DataFetcher dataFetcher){
        HashMap<Integer, ProduceData> dataMap = (dataFetcher.hasData()) ? dataFetcher.getProduceDataMap() : null;
        if(dataMap == null)
            ToolsHelper.showSiteErrorMessage(this);//site error
        else{
            ProduceData tempProduceData;
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View row;
            for(int i = 0 ; i < dataMap.size() ; i++ ){
                row = ( (i + 1) % 2 == 1) ? inflater.inflate(R.layout.odd_row, null) : inflater.inflate(R.layout.even_row, null) ;
                tempProduceData = dataMap.get(i);
                if(isCustomerMode())
                    addCustomerRow(row, tempProduceData);
                else
                    addGeneralRow(row, tempProduceData);
            }
        }
    }

    private void addGeneralRow(View row, ProduceData produceData) {
        TextView name = (TextView)row.findViewById(R.id.name);
        TextView topPrice = (TextView)row.findViewById(R.id.topPrice);
        TextView midPrice = (TextView)row.findViewById(R.id.midPrice);
        TextView lowPrice = (TextView)row.findViewById(R.id.lowPrice);
        TextView avgPrice = (TextView)row.findViewById(R.id.avgPrice);

        String completedName = (produceData.getType().length() <= 1) ? produceData.getName() : produceData.getType() + "\n" + produceData.getName() ;
        name.setText(completedName);
        topPrice.setText(getPriceWithUnit(produceData.getTopPrice()));
        midPrice.setText(getPriceWithUnit(produceData.getMidPrice()));
        lowPrice.setText(getPriceWithUnit(produceData.getLowPrice()));
        avgPrice.setText(getPriceWithUnit(produceData.getAvgPrice()));

        mTable.addView(row);
    }

    private void addCustomerRow(View row, ProduceData produceData) {
        TextView type = (TextView)row.findViewById(R.id.name);
        TextView name = (TextView)row.findViewById(R.id.topPrice);
        TextView avgPrice = (TextView)row.findViewById(R.id.avgPrice);
        TextView midPrice = (TextView)row.findViewById(R.id.midPrice);
        TextView lowPrice = (TextView)row.findViewById(R.id.lowPrice);

        ((TableRow)row).removeView(lowPrice);
        ((TableRow)row).removeView(midPrice);

        type.setText(produceData.getType());
        name.setText(produceData.getName());
        avgPrice.setText(getPriceRange(produceData.getAvgPrice()));

        mTable.addView(row);
    }

    private String getPriceRange(String price){
        float tmpPrice = Float.valueOf(price);
        float unit = ToolsHelper.getUnit(this);
        price = String.format("%.1f", tmpPrice * unit * 1.3) + " - " + String.format("%.1f", tmpPrice * unit * 1.5); //price * unit * profit
        return price;
    }

    private String getPriceWithUnit(String price){
        float tmpPrice = Float.valueOf(price);
        float unit = ToolsHelper.getUnit(this);
        return String.format("%.1f", tmpPrice * unit );
    }
}


