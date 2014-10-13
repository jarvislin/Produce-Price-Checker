package com.jarvislin.producepricechecker;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Jarvis Lin on 2014/10/10.
 */
public class DataFetcher {
    private final String FRUIT_URL = "http://amis.afa.gov.tw/t-asp/v102r.asp";
    private final String VEGETABLE_URL = "http://amis.afa.gov.tw/v-asp/v102r.asp";
    private Context mContext;
    private int retryCount = 0;
    private boolean mDataExist = false;
    private HashMap<Integer, ProduceData> mProduceDataMap = new HashMap<Integer, ProduceData>();

    public DataFetcher(int type, Context context) {
        mContext = context;
        int offset = 0;
        do {
            fetchData(Tools.getDate(offset), type);
            offset++;
        } while(!mDataExist && offset < 5 && retryCount < 5);
    }

    public boolean hasData(){
        return mDataExist;
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
            mDataExist = (doc.select("td").size() == 0) ? false : true ;
            if(mDataExist)
                saveData(doc.select("td"));
            else
                Log.d("gg","No data detected.");
        }catch (Exception ex){
            Log.d("gg","Fetching data failed! Try again.");
            fetchData(date, type); //retry
            retryCount++;
        }
    }

    private String getMarketNumber() {
        Log.d("gg", "market num = " + PreferenceManager.getDefaultSharedPreferences(mContext).getString("market_list","109"));
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString("market_list","109");
    }

    private void saveData(Elements elements) {
        Log.d("gg", "count" + String.valueOf(elements.size()));
        for(int i = 16, count = 0 ; i < elements.size() ; i += 10){
            String[] data = new String[6];
            data[0] = elements.get(i).text();
            data[1] = elements.get(i + 1).text();
            data[2] = elements.get(i + 3).text();
            data[3] = elements.get(i + 4).text();
            data[4] = elements.get(i + 5).text();
            data[5] = elements.get(i + 6).text();
            mProduceDataMap.put(count, new ProduceData(data));
            count++;
        }
    }
}
