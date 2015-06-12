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

import com.jarvislin.producepricechecker.ProduceData;
import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.util.PreferenceUtil;
import com.jarvislin.producepricechecker.util.ToolsHelper;

import java.util.ArrayList;

/**
 * Created by Jarvis Lin on 2014/12/14.
 */
public class BookmarkListAdapter extends BaseAdapter {

    private ArrayList<ProduceData> mList;
    private Context mContext;
    private int mType;
    private int isEditing = -1;

    public BookmarkListAdapter(Context context, ArrayList<ProduceData> list, int type)  {
        mList = list;
        mContext = context;
        mType = type;
    }

    @Override
    public void notifyDataSetInvalidated() {
        isEditing *= -1;
        super.notifyDataSetInvalidated();
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
            view = LayoutInflater.from(mContext).inflate((PreferenceUtil.isCustomerMode(mContext)) ? R.layout.cell_bookmark_customer : R.layout.cell_bookmark_general, parent, false);
            holder = new ViewHolder();

            //find views
            holder.typeName = (TextView) view.findViewById(R.id.type_name);
            holder.delete = (ImageView) view.findViewById(R.id.delete);
            holder.cell = (LinearLayout)view.findViewById(R.id.cell);

            if(PreferenceUtil.isCustomerMode(mContext)){
                holder.rangeDate = (TextView) view.findViewById(R.id.range_date);
            } else {
                holder.topMid = (TextView) view.findViewById(R.id.top_mid);
                holder.lowAvg = (TextView) view.findViewById(R.id.low_avg);
                holder.date = (TextView) view.findViewById(R.id.date);
            }

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        ProduceData data = mList.get(position);

        //set views
        holder.cell.setBackgroundColor(mContext.getResources().getColor((position % 2 == 0) ? R.color.white : R.color.odd_row));
        holder.typeName.setText((data.getType().length() <= 1) ? data.getName() : data.getType() + "\n" + data.getName());
        holder.delete.setVisibility(isEditing > 0 ? View.VISIBLE : View.INVISIBLE);
        holder.delete.setOnClickListener(clickDelete(position));

        if(PreferenceUtil.isCustomerMode(mContext)){
            holder.rangeDate.setText(ToolsHelper.getPriceRange(data.getAvgPrice(), mContext) + "\n" + ToolsHelper.getOffsetInWords(ToolsHelper.getOffset(data.getDate())));
        } else {
            holder.topMid.setText(data.getTopPrice() + "\n" + data.getMidPrice());
            holder.lowAvg.setText(data.getLowPrice() + "\n" + data.getAvgPrice());
            holder.date.setText(ToolsHelper.getOffsetInWords(ToolsHelper.getOffset(data.getDate())));
            holder.date.setVisibility(isEditing > 0 ? View.INVISIBLE : View.VISIBLE);
        }

        return view;
    }

    private View.OnClickListener clickDelete(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.removeBookmark(mContext, mList, position, mType);
                Toast.makeText(mContext, "移除成功", Toast.LENGTH_SHORT).show();
                BookmarkListAdapter.this.notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        //both
        LinearLayout cell;
        TextView typeName;
        ImageView delete;

        //customer
        TextView rangeDate;

        //general
        TextView topMid;
        TextView lowAvg;
        TextView date;
    }
}
