package com.jarvislin.producepricechecker.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;



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

    private static final HashMap<String, String> MARKET_MAP = new HashMap<String, String>(){{
        put("104", "中山區 台北二市");
        put("109", "萬華區 台北一市");
        put("241", "新北市 三重區");
        put("260", "宜蘭市");
        put("338", "桃園縣");
        put("400", "台中市");
        put("423", "台中市 東勢區");
        put("540", "南投市");
        put("600", "嘉義市");
        put("800", "高雄市");
        put("830", "高雄市 鳳山區");
        put("930", "台東市");
    }};

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
        Toast.makeText(context, "連不上網站, 請稍候再重新整理一次.", Toast.LENGTH_LONG).show();
    }

    public static String[] getDate(int offset) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -offset);

        String[] date = dateFormat.format(cal.getTime()).split("-");
        date[0] = String.valueOf(Integer.valueOf(date[0]) - 1911);
        return date;
    }

    public static String getFullDate(int offset) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -offset);

        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);

        String date = dateFormat.format(cal.getTime());
        return date;
    }

    public static String getOffsetInWords(int offset) {
        return (offset > 0) ? "(" + String.valueOf(offset) + "天前)" : "(今天)";
    }

    public static int getOffset(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date beginDate= dateFormat.parse(date);
            Date endDate= dateFormat.parse(getCurrentDate());
            return (int)(endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getMarketName(String key){
        return MARKET_MAP.get(key);
    }

    public static String getMarketNumber(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("market_list", "109");
    }

    public static float getUnit(Context context) {
        String temp = PreferenceManager.getDefaultSharedPreferences(context).getString("unit", "1.0");
        return Float.valueOf(temp);
    }

    public static String getUnitInWords(float digit) {
        return (digit < 1) ? "台斤/元" : "公斤/元";
    }

    public static String getPriceRange(String price, Context context){
        float tmpPrice = Float.valueOf(price);
        float unit = ToolsHelper.getUnit(context);

        if(tmpPrice * unit * 1.6 > 1000)
            price = String.format("%.0f", tmpPrice * unit * 1.3) + " - " + String.format("%.0f", tmpPrice * unit * 1.6); // price * unit * profit
        else
            price = String.format("%.1f", tmpPrice * unit * 1.3) + " - " + String.format("%.1f", tmpPrice * unit * 1.6); // price * unit * profit

        return price;
    }

    public static String getPriceWithUnit(String price, Context context){
        float tmpPrice = Float.valueOf(price);
        float unit = ToolsHelper.getUnit(context);

        if(tmpPrice * unit > 1000)
            return String.format("%.0f", tmpPrice * unit );
        else
            return String.format("%.1f", tmpPrice * unit );
    }
}
