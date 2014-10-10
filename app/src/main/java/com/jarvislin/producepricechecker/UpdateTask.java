package com.jarvislin.producepricechecker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Jarvis Lin on 2014/10/10.
 */
public class UpdateTask extends AsyncTask<Integer, Void, DataFetcher> {

    public UpdateTask(Context context){
        mContext = context;
    }

    private Context mContext;
    private ProgressDialog mProgressDialog;


    @Override
    protected DataFetcher doInBackground(Integer... type) {
        // TODO Auto-generated method stub
        //開始更新
        return new DataFetcher(type[0]);
    }

    @Override
    protected void onPreExecute(){
        //開啟更新畫面
        mProgressDialog = ProgressDialog.show(mContext, "更新資料", "更新中，請稍候...");
    }

    @Override
    protected void onPostExecute(DataFetcher result){
        //關掉更新畫面
        mProgressDialog.dismiss();
        if(mContext instanceof DataListActivity)
            ((DataListActivity)mContext).loadDataMap(result);
    }

}
