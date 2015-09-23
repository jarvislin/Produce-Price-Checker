package com.jarvislin.producepricechecker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.database.DatabaseController;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.util.Preferences_;

import java.util.ArrayList;

/**
 * Created by Jarvis Lin on 2015/6/15.
 */
public class CustomerAdapter extends BaseAdapter {

    protected ArrayList<Produce> list;
    protected Context context;
    protected Preferences_ prefs;
    protected String category;

    public CustomerAdapter(Context context, ArrayList<Produce> list, Preferences_ pref, String category) {
        this.list = list;
        prefs = pref;
        this.context = context;
        this.category = category;
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
            view = LayoutInflater.from(context).inflate(R.layout.cell_data_customer, parent, false);
            holder = new ViewHolder();
            holder.cell = (LinearLayout) view.findViewById(R.id.cell);
            holder.type = (TextView) view.findViewById(R.id.type);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.avgPrice = (TextView) view.findViewById(R.id.avgPrice);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Produce data = list.get(position);

        holder.cell.setBackgroundColor(context.getResources().getColor(
                DatabaseController.isBookmark(data.produceName, this.category) ? R.color.highlight : (position % 2 == 0) ? R.color.white : R.color.odd_row));

        //set views
        String[] name = data.produceName.split("-");
        if(name.length > 1) {
            holder.name.setText(name[0]);
            holder.type.setText(name[1]);
        } else {
            holder.name.setText(data.produceName);
            holder.type.setText(null);
        }

        float avg = Float.valueOf(data.averagePrice);
        int low = Math.round(avg * prefs.unit().get() * (1 + prefs.lowProfit().get()));
        int high = Math.round(avg * prefs.unit().get() * (1 + prefs.hightProfit().get()));
        holder.avgPrice.setText(low + " - " + high);

        return view;
    }

    private class ViewHolder {
        LinearLayout cell;
        TextView type;
        TextView name;
        TextView avgPrice;
    }
}
