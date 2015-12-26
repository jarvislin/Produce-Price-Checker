package com.jarvislin.producepricechecker.page.PriceList;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.model.ProduceData;
import com.jarvislin.producepricechecker.path.PathLayout;

import flow.path.Path;

/**
 * Created by jarvis on 15/9/23.
 */
@PathLayout(R.layout.merchant_price)
public class MerchantPath extends Path implements ProduceDataGetter{
    private ProduceData data;

    public MerchantPath(ProduceData data){
        this.data = data;
    }

    @Override
    public ProduceData getData() {
        return data;
    }
}
