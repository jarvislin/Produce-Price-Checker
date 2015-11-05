package com.jarvislin.producepricechecker.page.History;

import com.jarvislin.producepricechecker.database.Produce;

import java.util.ArrayList;

import flow.path.Path;

/**
 * Created by jarvis on 15/11/5.
 */
public interface ProduceListGetter {
    ArrayList<Produce> getProduces();
}
