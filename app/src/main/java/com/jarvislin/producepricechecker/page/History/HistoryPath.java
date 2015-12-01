package com.jarvislin.producepricechecker.page.History;

import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.ApiProduce;
import com.jarvislin.producepricechecker.adapter.ApiDataAdapter;

import java.io.Serializable;
import java.util.ArrayList;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/5.
 */
public class HistoryPath extends Path implements Serializable{
    private ArrayList<ApiProduce> list;
    public HistoryPath(ArrayList<ApiProduce> list) {
        this.list = list;
    }
    public ArrayList<Produce> getList() {
        return new ApiDataAdapter(list).getDataList();
    }
}
