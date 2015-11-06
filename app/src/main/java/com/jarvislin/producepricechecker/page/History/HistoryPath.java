package com.jarvislin.producepricechecker.page.History;

import com.jarvislin.producepricechecker.database.Produce;

import java.io.Serializable;
import java.util.ArrayList;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/5.
 */
public class HistoryPath extends Path implements Serializable{
    private ArrayList<Produce> list;
    public HistoryPath(ArrayList<Produce> list) {
        this.list = list;
    }
    public ArrayList<Produce> getList() {
        return list;
    }
}
