package com.jarvislin.producepricechecker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.util.Preferences_;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import java.util.ArrayList;

import database.DatabaseController;
import database.Produce;

/**
 * Created by Jarvis Lin on 2015/6/20.
 */
public class CustomerBookmarkAdapter extends BaseAdapter {

    protected ArrayList<Produce> list;
    protected Context context;
    protected String category;
    protected int isEditing = -1;
    protected Preferences_ prefs;

    public CustomerBookmarkAdapter(Context context, ArrayList<Produce> list, String category, Preferences_ pref) {
        this.list = list;
        this.context = context;
        this.category = category;
        prefs = pref;
    }

    @Override
    public void notifyDataSetInvalidated() {
        isEditing *= -1;
        super.notifyDataSetInvalidated();
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
            view = LayoutInflater.from(context).inflate(R.layout.cell_bookmark_customer, parent, false);
            holder = new ViewHolder();

            //find views
            holder.typeName = (TextView) view.findViewById(R.id.type_name);
            holder.delete = (ImageView) view.findViewById(R.id.delete);
            holder.cell = (LinearLayout) view.findViewById(R.id.cell);
            holder.rangeDate = (TextView) view.findViewById(R.id.range_date);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Produce data = list.get(position);

        //set views
        holder.cell.setBackgroundColor(context.getResources().getColor((position % 2 == 0) ? R.color.white : R.color.odd_row));
        holder.typeName.setText(data.produceName.replace("-", "\n"));
        holder.delete.setVisibility(isEditing > 0 ? View.VISIBLE : View.INVISIBLE);
        holder.delete.setOnClickListener(clickDelete(position));
        float avg = Float.valueOf(data.averagePrice);
        int low = Math.round(avg * prefs.unit().get() * (1 + prefs.lowProfit().get()));
        int high = Math.round(avg * prefs.unit().get() * (1 + prefs.hightProfit().get()));
        holder.rangeDate.setText(low + " - " + high + "\n" + ToolsHelper.getOffsetInWords(ToolsHelper.getOffset(data.transactionDate)));

        return view;
    }

    protected View.OnClickListener clickDelete(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Produce data = list.get(position);
//                DatabaseController.delete(data.produceName, category);
                list.remove(position);
                Toast.makeText(context, "移除成功", Toast.LENGTH_SHORT).show();
                CustomerBookmarkAdapter.this.notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        LinearLayout cell;
        TextView typeName;
        ImageView delete;
        TextView rangeDate;
    }
}