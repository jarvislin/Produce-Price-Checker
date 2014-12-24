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
        //updating
        return new DataFetcher(type[0], mContext);
    }

    @Override
    protected void onPreExecute(){
        //open ProgressDialog
        mProgressDialog = new ProgressDialog(mContext);;
        mProgressDialog.setTitle("更新資料");
        mProgressDialog.setMessage("更新中，請稍候...");
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(DataFetcher result){
        if(mContext instanceof DataListActivity)
            ((DataListActivity)mContext).loadDataList(result);
        //close ProgressDialog
        if(mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();

    }

}
