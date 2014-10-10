package com.jarvislin.producepricechecker;

import android.content.Context;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jarvis Lin on 2014/10/10.
 */
public class DataFetcher {
    private final String FRUIT_URL = "http://amis.afa.gov.tw/t-asp/v102q.asp";
    private final String VEGETABLE_URL = "http://amis.afa.gov.tw/v-asp/v101q.asp";
    public boolean mDataExist = false;
    private HashMap<Integer, ProduceData> mProduceDataMap = new HashMap<Integer, ProduceData>();


    public DataFetcher(int type) {
        int offset = 0;
        do {
            fetchData(getDate(offset), type);
            offset++;
        } while(!mDataExist && offset < 5);
    }

    public HashMap getProduceDataMap(){
        return mProduceDataMap;
    }

    private void fetchData(String[] date, int type) {
        String url = (type < 0) ? FRUIT_URL : VEGETABLE_URL;
        try {
            Connection.Response res = Jsoup.connect(url)
                    .data("mkno", getMarketNumber(), "myy", date[0], "mmm", date[1], "mdd", date[2])
                    .method(Connection.Method.POST)
                    .execute();

            Document doc = res.parse();
            mDataExist = (doc.select("p").first().text() == "查無結果!") ? false : true ;
            if(mDataExist)
                savaData(doc);
        }catch (Exception ex){
            Log.d("Checker","Fetching data failed!");
        }
    }

    private String getMarketNumber() {
        return "241";
    }

    private void savaData(Document document) {
        String gg = document.select("td").first().text();
    }


    private String[] getDate(int offset) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -offset);
        String[] date = dateFormat.format(cal.getTime()).split("/");
        date[0] = String.valueOf(Integer.valueOf(date[0]) - 1911);
        return date;
    }



}
