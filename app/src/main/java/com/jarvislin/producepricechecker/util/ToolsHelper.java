package com.jarvislin.producepricechecker.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import com.jarvislin.producepricechecker.R;

import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Jarvis Lin on 2014/10/11.
 */
public class ToolsHelper {

    private static Handler handler = new Handler(Looper.getMainLooper());
    private static ProgressDialog progressDialog;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showToast(Context context, int resId) {
        Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG).show();
    }

    public static String getUnitInWords(float digit) {
        return (digit < 1) ? "台斤/元" : "公斤/元";
    }

    public static void showProgressDialog(final Context context, boolean isNowOnUiThread) {
        if (isNowOnUiThread) {
            showProgressDialog(context);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    showProgressDialog(context);
                }
            });
        }
    }

    private static void showProgressDialog(Context context) {
        closeProgressDialog();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void closeProgressDialog(boolean isNowOnUiThread) {
        if (isNowOnUiThread) {
            closeProgressDialog();
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    closeProgressDialog();
                }
            });
        }
    }

    private static void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }
}
