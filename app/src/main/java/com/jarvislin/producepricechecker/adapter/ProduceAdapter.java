package com.jarvislin.producepricechecker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.model.ApiProduce;

import java.util.ArrayList;

/**
 * Created by Jarvis on 2017/4/29.
 */

public class ProduceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MODE_AVERAGE = 0;
    public static final int MODE_DETAIL = 1;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_DETAIL = 1;
    private static final int TYPE_AVERAGE = 2;
    private ArrayList<ApiProduce> produces;
    private int mode = MODE_AVERAGE;

    public ProduceAdapter(ArrayList<ApiProduce> produces) {
        this.produces = produces;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_AVERAGE:
                return new AverageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_data_customer, parent, false));
            case TYPE_DETAIL:
                return new DetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_data_merchant, parent, false));
            case TYPE_FOOTER:
                return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.price_footer, parent, false));
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return produces.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == produces.size()) {
            return TYPE_FOOTER;
        } else {
            if (mode == MODE_DETAIL) {
                return TYPE_DETAIL;
            } else if (mode == MODE_AVERAGE) {
                return TYPE_AVERAGE;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    public int getMode() {
        return mode;
    }

    public void updateMode(int mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }

    public void updateProduces(ArrayList<ApiProduce> produces) {
        this.produces = produces;
        notifyDataSetChanged();
    }

    class AverageViewHolder extends RecyclerView.ViewHolder {
        public AverageViewHolder(View itemView) {
            super(itemView);
        }
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        public DetailViewHolder(View itemView) {
            super(itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
