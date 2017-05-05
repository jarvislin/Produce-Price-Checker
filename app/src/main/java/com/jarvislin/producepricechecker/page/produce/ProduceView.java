package com.jarvislin.producepricechecker.page.produce;

import com.jarvislin.producepricechecker.base.BaseView;
import com.jarvislin.producepricechecker.java.util.ArrayList_ApiProduce;
import com.jarvislin.producepricechecker.model.ApiProduce;

import java.util.ArrayList;

/**
 * Created by Jarvis on 2017/4/29.
 */

interface ProduceView extends BaseView{
    void updateProduces(ArrayList<ApiProduce> produces);
}
