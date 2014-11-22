package com.jarvislin.producepricechecker;

import android.content.Context;

import com.jarvislin.producepricechecker.util.ToolsHelper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.HashMap;

/**
 * Created by Jarvis Lin on 2014/10/10.
 */
public class DataFetcher {
    private final String FRUIT_URL = "http://amis.afa.gov.tw/t-asp/v102r.asp";
    private final String VEGETABLE_URL = "http://amis.afa.gov.tw/v-asp/v102r.asp";
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private int mOffset = 0;
    private int mRetryCount = 0;
    private boolean mDataExist = false;
    private HashMap<Integer, ProduceData> mProduceDataMap = new HashMap<Integer, ProduceData>();

    public DataFetcher(int type, Context context) {
        mContext = context;
        do {
            fetchData(ToolsHelper.getDate(mOffset), type);
        } while(!mDataExist && mOffset < 5 && mRetryCount < 3);
    }

    public boolean hasData(){
        return mDataExist;
    }

    public HashMap getProduceDataMap(){
        return mProduceDataMap;
    }

    public int getOffset(){
        return mOffset - 1;
    }

    private void fetchData(String[] date, int type) {
        mOffset++;
        String url = (type < 0) ? FRUIT_URL : VEGETABLE_URL;
        try {
            Connection.Response res = Jsoup.connect(url)
                    .data("mkno", String.valueOf(ToolsHelper.getMarketNumber(mContext)), "myy", date[0], "mmm", date[1], "mdd", date[2])
                    .method(Connection.Method.POST)
                    .execute();

            Elements elements = res.parse().select("td");
            mDataExist = (elements.size() == 0) ? false : true ;
            if(mDataExist)
                saveData(elements);
            else {
//                Log.d(TAG, "No data detected.");
            }
        }catch (Exception ex){
            ex.printStackTrace();
            mOffset--;
            mRetryCount++;
//            Log.d(TAG, "Fetching data failed!");
        }
    }

    private void saveData(Elements elements) {
//        Log.d(TAG, "Size = " + String.valueOf(elements.size()));
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
