package com.jarvislin.producepricechecker;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;


public class DataListActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();
    private UpdateTask mUpdateTask = new UpdateTask(this);
    private TableLayout mTable;
    private TextView mCurrentDate;
    private TextView mDataDate;
    private TextView mMarketName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView((isCustomerMode()) ? R.layout.customer_data_list : R.layout.general_data_list);
        mUpdateTask.execute(getType());

        findViews();

    }

    private void findViews() {
        mTable = (TableLayout)findViewById(R.id.tableLayout);
        mCurrentDate = (TextView)findViewById(R.id.currentDate);
        mDataDate = (TextView)findViewById(R.id.dataDate);
        mMarketName = (TextView)findViewById(R.id.marketName);
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
            Tools.showSiteErrorMessage(this);//site error
        else{
            editViewInfo(dataFetcher.getOffset());
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

    private void editViewInfo(int offset) {
        String[] tempDate = Tools.getDate(0);
        mCurrentDate.setText(tempDate[0] + "/" + tempDate[1]+ "/" + tempDate[2]);

        tempDate = Tools.getDate(offset);
        mDataDate.setText(tempDate[0] + "/" + tempDate[1]+ "/" + tempDate[2]);

        mMarketName.setText(Tools.getMarketName(Integer.valueOf(Tools.getMarketNumber(this))));
    }

    private void addGeneralRow(View row, ProduceData produceData) {
        TextView name = (TextView)row.findViewById(R.id.name);
        TextView topPrice = (TextView)row.findViewById(R.id.topPrice);
        TextView midPrice = (TextView)row.findViewById(R.id.midPrice);
        TextView lowPrice = (TextView)row.findViewById(R.id.lowPrice);
        TextView avgPrice = (TextView)row.findViewById(R.id.avgPrice);

        String completedName = (produceData.getType().length() <= 1) ? produceData.getName() : produceData.getType() + "\n" + produceData.getName() ;
        name.setText(completedName);
        topPrice.setText(produceData.getTopPrice());
        midPrice.setText(produceData.getMidPrice());
        lowPrice.setText((produceData.getLowPrice()));
        avgPrice.setText(produceData.getAvgPrice());

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
        price = String.format("%.1f",tmpPrice * 0.6 * 1.3) + " - " + String.format("%.1f", tmpPrice * 0.6 * 1.5); //price * unit(0.6kg) * profit
        return price;
    }
}


