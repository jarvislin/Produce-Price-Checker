package com.jarvislin.producepricechecker.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jarvislin.producepricechecker.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jarvis on 15/5/26.
 */
public class ToolsHelper {
    private static ProgressDialog progressDialog;
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static int getCurrentUnixTime() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static ArrayList<String> getStrideRange() {
        ArrayList<String> stride = new ArrayList<String>();
        for (int i = 37; i <= 92; i++) {
            stride.add(String.valueOf(i));
        }
        return stride;
    }

    public static ArrayList<String> getHeightRange() {
        ArrayList<String> height = new ArrayList<String>();
        for (double i = 90.0; i <= 220.0; i += 0.5) {
            height.add(String.valueOf(i));
        }
        return height;
    }

    public static SpannableString getValueWithUnit(String value, String unit, int valueTextSize, int unitTextSize) {
        if(TextUtils.isEmpty(value)|| TextUtils.isEmpty(unit)){
            return new SpannableString("");
        }
        int length = value.length() + unit.length();
        SpannableString spannableString = new SpannableString(value + unit);
        spannableString.setSpan(new AbsoluteSizeSpan(valueTextSize, true), 0, length - unit.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(unitTextSize, true), length - unit.length(), length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myFitty");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("myFitty", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return Uri.fromFile(mediaFile);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getFormattedDate(String unformattedDate) {
        SimpleDateFormat input = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat output = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date date = input.parse(unformattedDate);
            return output.format(date);
        } catch (ParseException e) {
        }
        return unformattedDate;
    }

    public static boolean hasImageCaptureBug() {

        // list of known devices that have the bug
        ArrayList<String> devices = new ArrayList<String>();
        devices.add("android-devphone1/dream_devphone/dream");
        devices.add("generic/sdk/generic");
        devices.add("vodafone/vfpioneer/sapphire");
        devices.add("tmobile/kila/dream");
        devices.add("verizon/voles/sholes");
        devices.add("google_ion/google_ion/sapphire");

        return devices.contains(Build.BRAND + "/" + Build.PRODUCT + "/" + Build.DEVICE);

    }

    /**
     * Method for return file path of Gallery image
     *
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */
    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(
            Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
