package com.jarvislin.producepricechecker.page.Details;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.OpenData;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.path.PathLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import flow.path.Path;

/**
 * Created by Jarvis Lin on 2016/2/23.
 */
@PathLayout(R.layout.details)
public class DetailsPath extends Path implements Serializable {
    public String market;
    public String produce;
    public ArrayList<OpenData> list;
    public DetailsPath(String market, String produce, ArrayList<OpenData> list) {
        this.list = list;
        Collections.sort(this.list);
        this.produce = produce;
        this.market = market;
    }
}
