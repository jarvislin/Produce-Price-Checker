package com.jarvislin.producepricechecker.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jarvislin.producepricechecker.R;
import com.jarvislin.producepricechecker.model.HistoryDirectory;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jarvis on 15/11/26.
 */
public class CalendarDialog implements OnDateSelectedListener {
    private Context context;
    private HistoryDirectory historyDirectory;
    private AlertDialog dialog;
    private MaterialCalendarView calendarView;
    private DisableDecorator decorator;
    private OnClickDateListener listener;

    public CalendarDialog(Context context, HistoryDirectory historyDirectory, OnClickDateListener listener) {
        this.context = context;
        this.historyDirectory = historyDirectory;
        this.listener = listener;
        init();
    }

    private void init() {
        View item = LayoutInflater.from(context).inflate(R.layout.dialog_calendar, null);
        dialog = new AlertDialog.Builder(context)
                .setNegativeButton("關閉", null)
                .setView(item).create();

    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
            calendarView = (MaterialCalendarView) dialog.findViewById(R.id.material_calendar);
            calendarView.setOnDateChangedListener(this);
            calendarView.setMinimumDate(historyDirectory.getMinDate());
            calendarView.setMaximumDate(new Date());
            calendarView.addDecorator(new DisableDecorator());
        }
    }

    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        listener.onDateClicked(date);
    }

    public void setHistory(HistoryDirectory directory) {
        historyDirectory = directory;
    }


    private class DisableDecorator implements DayViewDecorator {

        public DisableDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return !historyDirectory.exist(day.getYear(), day.getMonth(), day.getDay());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }

    }

    public interface OnClickDateListener {
        void onDateClicked(CalendarDay date);
    }
}
