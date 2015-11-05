package com.jarvislin.producepricechecker.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Jarvis Lin on 2015/10/31.
 */
public class HistoryDirectory implements Serializable {
    private History[] history;

    public History[] getHistory() {
        return history;
    }

    public void setHistory(History[] history) {
        this.history = history;
    }

    public Date getMinDate() {
        return history[0].getDate().get(0);
    }

    public Date getMaxDate() {
        int size = history[history.length - 1].getDate().size();
        return history[history.length - 1].getDate().get(size - 1);
    }

    public ArrayList<Date> getAllDates() {
        ArrayList<Date> list = new ArrayList<>();
        for (History history : this.history) {
            list.addAll(history.getDate());
        }
        return list;
    }

    @Override
    public String toString() {
        return "ClassPojo [history = " + history + "]";
    }

    public class History {
        private List<Date> list;
        private String year;

        private String[] date;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public List<Date> getDate() {
            if (list != null) return list;
            list = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            String[] dateArray;
            for (String dateText : date) {
                dateArray = dateText.split("\\.");
                if (dateArray.length == 2) {
                    calendar.set(Integer.valueOf(year) + 1911, Integer.valueOf(dateArray[0]) - 1, Integer.valueOf(dateArray[1]), 0, 0, 0);
                    list.add(calendar.getTime());
                }
            }
            calendar.clear();
            Collections.sort(list);
            return list;
        }

        public void setDate(String[] date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "ClassPojo [year = " + year + ", date = " + date + "]";
        }
    }
}
