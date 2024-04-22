package org.hse.android.baseproject;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import java.util.Locale;

@Entity(tableName = "lesson")
public class ScheduleLesson {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public Calendar startMomentUtc = Calendar.getInstance(TimeZone.GMT_ZONE);

    @NonNull
    public Calendar endMomentUtc = Calendar.getInstance(TimeZone.GMT_ZONE);

    @NonNull
    public String name = "UNKNOWN";

    @NonNull
    public String type = "UNKNOWN";

    @NonNull
    public String room = "UNKNOWN";

    @NonNull
    public String building = "UNKNOWN";

    public int teacherId;

    public int groupId;
}

class ScheduleLessonWithTeacher {
    @Embedded
    public ScheduleLesson lesson;
    @Relation(
            parentColumn = "teacherId",
            entityColumn = "id"
    )
    public Teacher teacher;
}
