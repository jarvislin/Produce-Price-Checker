package com.jarvislin.producepricechecker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class IndexActivity extends Activity {
    protected View mFruitButton;
    protected View mVegetableButton;
    protected View mSettingButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().hide();
        setContentView(R.layout.activity_index);

        setNetwork();
        initViews();

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        if(!isNetworkAvailable()){
            Log.d("gg", "no net");
        }else
            Log.d("gg", "has net");

    }

    private void initViews() {
        mFruitButton = findViewById(R.id.fruit);
        mVegetableButton = findViewById(R.id.vegetable);
        mSettingButton = findViewById(R.id.setting);

        mFruitButton.setOnClickListener(clickFruit());
        mVegetableButton.setOnClickListener(clickVegetable());
        mSettingButton.setOnClickListener(clickSetting());
    }


    private void setNetwork(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
                .build());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private Button.OnClickListener clickFruit(){
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this,DataListActivity.class);
                intent.putExtra("type", -1);
                IndexActivity.this.startActivity(intent);
            }
        };
    }

    private Button.OnClickListener clickVegetable(){
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        };
    }

    private Button.OnClickListener clickSetting(){
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        };
    }


}
