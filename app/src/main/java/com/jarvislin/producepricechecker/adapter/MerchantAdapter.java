package com.jarvislin.producepricechecker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.util.Preferences_;

import java.util.ArrayList;

import database.DatabaseController;
import database.Produce;

/**
 * Created by Jarvis Lin on 2014/12/8.
 */
public class MerchantAdapter extends CustomerAdapter {

    public MerchantAdapter(Context context, ArrayList<Produce> list, Preferences_ pref, String kind) {
        super(context, list, pref, kind);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.cell_data_merchant, parent, false);
            holder = new ViewHolder();

            //find views
            holder.cell = (LinearLayout) view.findViewById(R.id.cell);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.topPrice = (TextView) view.findViewById(R.id.topPrice);
            holder.midPrice = (TextView) view.findViewById(R.id.midPrice);
            holder.lowPrice = (TextView) view.findViewById(R.id.lowPrice);
            holder.avgPrice = (TextView) view.findViewById(R.id.avgPrice);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        Produce data = list.get(position);

        holder.cell.setBackgroundColor(context.getResources().getColor(DatabaseController.isBookmark(data.produceName, category) ? R.color.highlight : (position % 2 == 0) ? R.color.white : R.color.odd_row));

        //set views
        holder.name.setText(data.produceName.replace("-","\n"));
        holder.topPrice.setText(String.valueOf(Math.round(Float.valueOf(data.topPrice) * prefs.unit().get())));
        holder.midPrice.setText(String.valueOf(Math.round(Float.valueOf(data.middlePrice) * prefs.unit().get())));
        holder.lowPrice.setText(String.valueOf(Math.round(Float.valueOf(data.lowPrice) * prefs.unit().get())));
        holder.avgPrice.setText(String.valueOf(Math.round(Float.valueOf(data.averagePrice) * prefs.unit().get())));

        return view;
    }


    private class ViewHolder {
        LinearLayout cell;
        TextView name;
        TextView topPrice;
        TextView midPrice;
        TextView lowPrice;
        TextView avgPrice;
    }
}
