package com.jarvislin.producepricechecker.page.History;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.path.PathLayout;

import java.util.ArrayList;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/5.
 */
@PathLayout(R.layout.customer_price)
public class CustomerHistoryPath extends Path implements ProduceListGetter {
    private ArrayList<Produce> list;
    public CustomerHistoryPath(ArrayList<Produce> list) {
        this.list = list;
    }
    @Override
    public ArrayList<Produce> getProduces() {
        return list;
    }
}
