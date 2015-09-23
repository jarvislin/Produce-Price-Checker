package com.jarvislin.producepricechecker.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvislin.producepricechecker.R;

/**
 * Created by jarvis on 15/5/26.
 */
public class ToolsHelper {
    private static ProgressDialog progressDialog;
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static Toast showToast(Toast toast, Context context, String string, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, string, duration);
        } else {
            toast.setText(string);
            toast.setDuration(duration);
        }
        toast.show();
        return toast;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static ProgressDialog getProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
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

    private static void showProgressDialog(Context context) {
        closeProgressDialog();
        getProgressDialog(context).show();
    }

    private static void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }

    public static void showDialog(Context context, String titleText, String messageText){
        final Dialog dialog = new Dialog(context, R.style.alertDialog);
        dialog.setContentView(R.layout.dialog_info);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView message = (TextView) dialog.findViewById(R.id.info_text);

        title.setText(titleText);
        message.setText(messageText);

        Button dismiss = (Button) dialog.findViewById(R.id.info_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


}
