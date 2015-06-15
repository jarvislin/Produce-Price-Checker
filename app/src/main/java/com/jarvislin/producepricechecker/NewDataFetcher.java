package com.jarvislin.producepricechecker;

import android.content.Context;

import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.androidannotations.annotations.EBean;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import database.Produce;

/**
 * Created by Jarvis Lin on 2015/6/15.
 */
@EBean(scope = EBean.Scope.Singleton)
public class NewDataFetcher extends Fetcher {
    Context context;
    int offset = 0;

    public NewDataFetcher build(Context context) {
        this.context = context;
        return this;
    }

    @Override
    public ArrayList<Produce> getProduces(String url) {
        return fetch(url);
    }

    private ArrayList<Produce> fetch(String url) {
        try {
            String[] date = ToolsHelper.getDateParam(offset);
            Connection.Response res = Jsoup.connect(url)
                    .data("mkno", String.valueOf(ToolsHelper.getMarketNumber(context)), "myy", date[0], "mmm", date[1], "mdd", date[2])
                    .method(Connection.Method.POST)
                    .execute();
            Elements elements = res.parse().select("td");
            if (elements.size() > 0) {
                return save(elements);
            } else if (offset > -7) {
                offset--;
                fetch(url);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return null;
        }
    }

    private ArrayList<Produce> save(Elements elements) {
        Produce produce;
        ArrayList<Produce> list = new ArrayList<>();
        for (int i = 16; i < elements.size(); i += 10) {
            produce = new Produce();
            produce.name = elements.get(i).text();
            produce.type = elements.get(i + 1).text();
            produce.topPrice = elements.get(i + 3).text();
            produce.mediumPrice = elements.get(i + 4).text();
            produce.lowPrice = elements.get(i + 5).text();
            produce.averagePrice = elements.get(i + 6).text();
            list.add(produce);
        }
        return list;
    }

    @Override
    public int getDataOffset() {
        return offset;
    }
}
