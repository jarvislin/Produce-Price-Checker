package com.jarvislin.producepricechecker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jarvislin.producepricechecker.database.ProduceDAO;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import java.util.HashMap;
import java.util.List;


public class DataListActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();
    private TableLayout mTable;
    private int mOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        update(null);

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.actionbar_data_table);
    }

    public void update(View view) {
        if(!ToolsHelper.isNetworkAvailable(this)) {
            ToolsHelper.showNetworkErrorMessage(this);
            finish();
        } else {
            setContentView((isCustomerMode()) ? R.layout.customer_data_list : R.layout.general_data_list);
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
        mOffset = dataFetcher.getOffset();
        if(dataMap == null)
            ToolsHelper.showSiteErrorMessage(this); //show error
        else{
            ProduceData tempProduceData;
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View row;
//            for(int i = 0 ; i < dataMap.size() ; i++ ){
//                row = ( (i + 1) % 2 == 1) ? inflater.inflate(R.layout.odd_row, null) : inflater.inflate(R.layout.even_row, null) ;
//                tempProduceData = dataMap.get(i);
//                if(isCustomerMode())
//                    addCustomerRow(row, tempProduceData);
//                else
//                    addGeneralRow(row, tempProduceData);
//            }
            ProduceDAO dao = new ProduceDAO(this);
            List<ProduceData> list =  dao.getAll();
            for(int i = 0; i<list.size();i++){
                row = ( (i + 1) % 2 == 1) ? inflater.inflate(R.layout.odd_row, null) : inflater.inflate(R.layout.even_row, null) ;
                tempProduceData = list.get(i);
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
        price = String.format("%.1f", tmpPrice * unit * 1.3) + " - " + String.format("%.1f", tmpPrice * unit * 1.5); // price * unit * profit
        return price;
    }

    private String getPriceWithUnit(String price){
        float tmpPrice = Float.valueOf(price);
        float unit = ToolsHelper.getUnit(this);
        return String.format("%.1f", tmpPrice * unit );
    }

    public void info(View view) {
        String[] date = ToolsHelper.getDate(mOffset);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("資訊");
        builder.setMessage(
                "資料日期：" + date[0] + "/" + date[1] + "/" + date[2] + ToolsHelper.getOffsetInWords(mOffset) + "\n" +
                "單位：" + ToolsHelper.getUnitInWords(ToolsHelper.getUnit(this)) + "\n" +
                "市場：" + ToolsHelper.getMarketName(ToolsHelper.getMarketNumber(this))
        );

        builder.setNeutralButton(getString(R.string.back), new AlertDialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });

        builder.show();
    }
}


