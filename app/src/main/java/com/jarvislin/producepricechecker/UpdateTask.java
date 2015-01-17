package com.jarvislin.producepricechecker;

import android.app.Dialog;
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
    private Dialog mProgressDialog;


    @Override
    protected DataFetcher doInBackground(Integer... type) {
        // TODO Auto-generated method stub
        //updating
        return new DataFetcher(type[0], mContext);
    }

    @Override
    protected void onPreExecute(){
        //open ProgressDialog
        mProgressDialog = new Dialog(mContext, R.style.alertDialog);;
        mProgressDialog.setContentView(R.layout.dialog_update);
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(DataFetcher result){
        if(mContext instanceof DataListActivity)
            ((DataListActivity)mContext).loadDataList(result);
        //close Dialog
        if(mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();

    }

}
