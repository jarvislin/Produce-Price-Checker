package com.jarvislin.producepricechecker;

import java.util.List;

/**
 * Created by jarvis on 15/11/25.
 */
public class GitJson {
    private List<HistoryEntity> history;

    public void setHistory(List<HistoryEntity> history) {
        this.history = history;
    }

    public List<HistoryEntity> getHistory() {
        return history;
    }

    public static class HistoryEntity {
        private String year;
        private List<String> date;

        public void setYear(String year) {
            this.year = year;
        }

        public void setDate(List<String> date) {
            this.date = date;
        }

        public String getYear() {
            return year;
        }

        public List<String> getDate() {
            return date;
        }
    }
}
