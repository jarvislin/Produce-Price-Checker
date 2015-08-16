package com.jarvislin.producepricechecker.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jarvis Lin on 2015/8/15.
 */
public class DateUtil {
    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM.dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        int year = cal.get(Calendar.YEAR) - 1911;
        String date = year +"."+ dateFormat.format(cal.getTime());
        return date;
    }

    public static String getOffsetInWords(int offset) {
        return (offset > 0) ? " (" + String.valueOf(offset) + "天前)" : " (今天)";
    }

    public static int getOffset(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date beginDate = dateFormat.parse(date);
            Date endDate = dateFormat.parse(getCurrentDate());
            return (int) (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
