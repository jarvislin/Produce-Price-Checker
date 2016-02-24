package com.jarvislin.producepricechecker.page.Details;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.database.Produce;
import com.jarvislin.producepricechecker.model.OpenData;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.path.PathLayout;

import java.util.ArrayList;

import flow.path.Path;

/**
 * Created by Jarvis Lin on 2016/2/23.
 */
@PathLayout(R.layout.details)
public class DetailsPath extends Path {
    public Produce data;
    public ArrayList<OpenData> list;
    public DetailsPath(Produce data, ArrayList<OpenData> list) {
        this.list = list;
        this.data = data;
    }
}
