package org.hse.android.baseproject;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import java.util.List;
import java.util.Locale;

public class ScheduleDay {
    protected List<ScheduleLesson> lessons;
    protected Calendar day;

    public ScheduleDay(Calendar day, List<ScheduleLesson> lessons) {
        this.day = day;
        this.lessons = lessons;
    }

    public List<ScheduleLesson> getLessons() {
        return lessons;
    }

    public String getName() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault());
        return formatter.format(day);
    }
}
