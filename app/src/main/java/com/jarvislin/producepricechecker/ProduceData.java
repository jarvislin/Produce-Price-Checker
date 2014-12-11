package com.jarvislin.producepricechecker;

/**
 * Created by Jarvis Lin on 2014/10/10.
 */
public class ProduceData {
    private String[] mData;
    private boolean mBookmark = false;
    public ProduceData(String[] data){
        mData = data;
    }
    public String getName(){
        return mData[0];
    }
    public String getType(){
        return mData[1];
    }
    public String getTopPrice(){
        return mData[2];
    }
    public String getMidPrice(){
        return mData[3];
    }
    public String getLowPrice(){
        return mData[4];
    }
    public String getAvgPrice(){
        return mData[5];
    }
    public void setBookmark(boolean status){
        mBookmark = status;
    }
    public boolean isBookmark() {
        return mBookmark;
    }
}
