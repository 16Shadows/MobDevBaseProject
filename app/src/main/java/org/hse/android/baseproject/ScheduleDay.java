package org.hse.android.baseproject;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ScheduleDay {
    protected final List<ScheduleLessonWithTeacher> lessons = new ArrayList<>();
    protected final Calendar dateUTC;
    protected final Calendar dateLocal;

    public ScheduleDay(Calendar dateLocal) {
        this.dateLocal = (Calendar)dateLocal.clone();
        this.dateLocal.setTimeZone(TimeZone.getDefault());
        this.dateUTC = (Calendar)dateLocal.clone();
        this.dateUTC.setTimeZone(TimeZone.GMT_ZONE);
    }

    public String getDisplayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault());
        SimpleDateFormatEx.capitalizeWeekdays(formatter);
        return formatter.format(dateLocal);
    }

    public boolean isLessonInDay(ScheduleLessonWithTeacher lesson) {
        return dateUTC.get(Calendar.DAY_OF_YEAR) == lesson.lesson.startMomentUtc.get(Calendar.DAY_OF_YEAR) &&
               dateUTC.get(Calendar.YEAR) == lesson.lesson.startMomentUtc.get(Calendar.YEAR);
    }

    public void addLesson(ScheduleLessonWithTeacher lesson) {
        if (!isLessonInDay((lesson)))
            return;

        lessons.add(lesson);
    }

    public List<ScheduleLessonWithTeacher> getLessons() {
        return lessons;
    }
}
