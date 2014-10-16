package com.jarvislin.producepricechecker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class IndexActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        setNetwork();

        if(!ToolsHelper.isNetworkAvailable(this))
            ToolsHelper.showNetworkErrorMessage(this);

    }

    private void setNetwork(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
                .build());
    }

    public void fruit(View view) {
        Intent intent = new Intent(IndexActivity.this,DataListActivity.class);
        intent.putExtra("type", -1);
        IndexActivity.this.startActivity(intent);
    }

    public void vegetable(View view) {
        Intent intent = new Intent(IndexActivity.this,DataListActivity.class);
        intent.putExtra("type", 1);
        IndexActivity.this.startActivity(intent);
    }

    public void settings(View view) {
        Intent intent = new Intent(IndexActivity.this,SettingsActivity.class);
        IndexActivity.this.startActivity(intent);
    }

    public void leave(View view) {
        this.finish();
    }


}
