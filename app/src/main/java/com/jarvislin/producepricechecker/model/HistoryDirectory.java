package com.jarvislin.producepricechecker.model;

import java.io.Serializable;

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

    @Override
    public String toString() {
        return "ClassPojo [history = " + history + "]";
    }

    public class History {
        private String year;

        private String[] date;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String[] getDate() {
            return date;
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
