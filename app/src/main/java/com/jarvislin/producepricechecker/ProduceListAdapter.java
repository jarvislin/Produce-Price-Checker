package com.jarvislin.producepricechecker;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarvislin.producepricechecker.util.ToolsHelper;

import java.util.ArrayList;

/**
 * Created by Jarvis Lin on 2014/12/8.
 */
public class ProduceListAdapter extends BaseAdapter {

    private  ArrayList<ProduceData> mList;
    private Context mContext;
    private boolean mIsCustomerMode;

    public ProduceListAdapter(Context context, ArrayList<ProduceData> list, boolean isCustomerMode)  {
        mList = list;
        mContext = context;
        mIsCustomerMode = isCustomerMode;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if(convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.cell, parent, false);
            holder = new ViewHolder();

            //find views
            holder.cell = (LinearLayout) view.findViewById(R.id.cell);
            holder.type = (TextView) view.findViewById(R.id.type);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.topPrice = (TextView) view.findViewById(R.id.topPrice);
            holder.midPrice = (TextView) view.findViewById(R.id.midPrice);
            holder.lowPrice = (TextView) view.findViewById(R.id.lowPrice);
            holder.avgPrice = (TextView) view.findViewById(R.id.avgPrice);

            view.setTag(holder);

        } else {
            //get better performance, don't inflate when convertView != null, that will OOM
            holder = (ViewHolder) view.getTag();
        }

        holder.cell.setBackgroundColor(mContext.getResources().getColor((position % 2 == 0) ? R.color.white : R.color.odd_row));

        ProduceData data = mList.get(position);

        if (mIsCustomerMode) {

            //set views
            holder.type.setText(data.getType());
            holder.name.setText(data.getName());
            holder.avgPrice.setText(getPriceRange(data.getAvgPrice()));

            //hide views
            holder.topPrice.setVisibility(View.GONE);
            holder.midPrice.setVisibility(View.GONE);
            holder.lowPrice.setVisibility(View.GONE);

            //set width
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(GlobalVariable.fiveCharsWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            holder.type.setLayoutParams(params);
            holder.name.setLayoutParams(params);

            params = new LinearLayout.LayoutParams(GlobalVariable.rangeWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            holder.avgPrice.setLayoutParams(params);
        } else {

            //set views
            String completedName = (data.getType().length() <= 1) ? data.getName() : data.getType() + "\n" + data.getName();
            holder.name.setText(completedName);
            holder.topPrice.setText(getPriceWithUnit(data.getTopPrice()));
            holder.midPrice.setText(getPriceWithUnit(data.getMidPrice()));
            holder.lowPrice.setText(getPriceWithUnit(data.getLowPrice()));
            holder.avgPrice.setText(getPriceWithUnit(data.getAvgPrice()));

            //hide views
            holder.type.setVisibility(View.GONE);

            //set width
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(GlobalVariable.fiveCharsWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            holder.name.setLayoutParams(params);

            params = new LinearLayout.LayoutParams(GlobalVariable.fourDigitsWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            holder.topPrice.setLayoutParams(params);
            holder.midPrice.setLayoutParams(params);
            holder.lowPrice.setLayoutParams(params);
            holder.avgPrice.setLayoutParams(params);
        }

        return view;
    }

    private String getPriceRange(String price){
        float tmpPrice = Float.valueOf(price);
        float unit = ToolsHelper.getUnit(mContext);

        if(tmpPrice * unit * 1.6 > 1000)
            price = String.format("%.0f", tmpPrice * unit * 1.3) + " - " + String.format("%.0f", tmpPrice * unit * 1.6); // price * unit * profit
        else
            price = String.format("%.1f", tmpPrice * unit * 1.3) + " - " + String.format("%.1f", tmpPrice * unit * 1.6); // price * unit * profit

        return price;
    }

    private String getPriceWithUnit(String price){
        float tmpPrice = Float.valueOf(price);
        float unit = ToolsHelper.getUnit(mContext);

        if(tmpPrice * unit > 1000)
            return String.format("%.0f", tmpPrice * unit );
        else
            return String.format("%.1f", tmpPrice * unit );
    }

    private class ViewHolder {
        LinearLayout cell;
        TextView type;
        TextView name;
        TextView topPrice;
        TextView midPrice;
        TextView lowPrice;
        TextView avgPrice;
    }
}
