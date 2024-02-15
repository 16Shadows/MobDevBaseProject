package org.hse.android.baseproject;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import java.util.Locale;

public class ScheduleLesson {
    protected final Calendar startMomentUtc;
    protected final Calendar endMomentUtc;
    protected final String name;
    protected final String type;
    protected final String room;
    protected final String building;
    protected final Teacher teacher;

    public ScheduleLesson(Calendar startMomentUtc, Calendar endMomentUtc, String name, String type, String room, String building, Teacher teacher) {
        this.startMomentUtc = startMomentUtc;
        this.endMomentUtc = endMomentUtc;
        this.name = name;
        this.type = type;
        this.room = room;
        this.building = building;
        this.teacher = teacher;
    }

    protected static SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    public String getStartTime() {
        return getDateFormatter().format(startMomentUtc);
    }

    public String getEndTime() {
        return getDateFormatter().format(endMomentUtc);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRoom() {
        return room;
    }

    public String getBuilding() {
        return building;
    }

    public String getTeacherName() {
        return teacher.getName();
    }
}
