package com.jarvislin.producepricechecker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Jarvis Lin on 2014/10/11.
 */
public class Tools {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showNetworkErrorMessage(Context context){
        Toast.makeText(context, "偵測不到網路, 請確認是否連上網路.", Toast.LENGTH_LONG).show();
    }

    public static void showSiteErrorMessage(Context context){
        Toast.makeText(context,"連不上網站, 請稍候再試一次!", Toast.LENGTH_LONG).show();
    }
}
