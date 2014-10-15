package com.jarvislin.producepricechecker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class IndexActivity extends Activity {
    protected View mFruitButton;
    protected View mVegetableButton;
    protected View mSettingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        setNetwork();
        initViews();

        if(!ToolsHelper.isNetworkAvailable(this))
            ToolsHelper.showNetworkErrorMessage(this);

    }

    private void initViews() {
        //find views
        mFruitButton = findViewById(R.id.fruit);
        mVegetableButton = findViewById(R.id.vegetable);
        mSettingButton = findViewById(R.id.setting);

        //set listeners
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
                Intent intent = new Intent(IndexActivity.this,DataListActivity.class);
                intent.putExtra("type", 1);
                IndexActivity.this.startActivity(intent);
            }
        };
    }

    private Button.OnClickListener clickSetting(){
        return new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this,SettingsActivity.class);
                IndexActivity.this.startActivity(intent);
            }
        };
    }


}
