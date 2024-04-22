package org.hse.android.baseproject;

import android.icu.util.Calendar;
import android.icu.util.TimeZone;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Transaction;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Database(entities = {StudentsGroup.class, Teacher.class, ScheduleLesson.class}, version = 1, exportSchema = false)
@TypeConverters(HseTimetableDatabase.CalendarConverter.class)
public abstract class HseTimetableDatabase extends RoomDatabase {
    public static class CalendarConverter {
        @TypeConverter
        public static Calendar fromTimestamp(Long value) {
            Calendar c = Calendar.getInstance(TimeZone.GMT_ZONE);
            c.setTimeInMillis(value);
            return c;
        }

        @TypeConverter
        public static Long toTimestamp(Calendar value) {
            return value.getTimeInMillis();
        }
    }

    public static final String DATABASE_NAME = "hse_timetable";

    @Dao
    public interface HseTimetableDao {
        @Insert
        void insertGroup(StudentsGroup group);

        @Query("SELECT * FROM `group`")
        LiveData<List<StudentsGroup>> getGroups();

        @Transaction
        @Query("SELECT * FROM `lesson` WHERE groupId = :groupId AND startMomentUtc >= :utcFrom AND startMomentUtc <= :utcTo ORDER BY startMomentUtc")
        LiveData<List<ScheduleLessonWithTeacher>> filterLessonsForGroup(int groupId, Calendar utcFrom, Calendar utcTo);

        @Insert
        void insertTeacher(Teacher teacher);

        @Query("SELECT * FROM `teacher`")
        LiveData<List<Teacher>> getTeachers();

        @Transaction
        @Query("SELECT * FROM `lesson` WHERE teacherId = :teacherId AND startMomentUtc >= :utcFrom AND startMomentUtc <= :utcTo ORDER BY startMomentUtc")
        LiveData<List<ScheduleLessonWithTeacher>> filterLessonsForTeacher(int teacherId, Calendar utcFrom, Calendar utcTo);

        @Insert
        void insertLesson(ScheduleLesson lesson);

        @Query("SELECT * FROM `lesson` WHERE teacherId = :teacherId AND startMomentUtc <= :utcTime AND endMomentUtc >= :utcTime")
        LiveData<ScheduleLesson> getLessonByTeacherAndTime(int teacherId, Calendar utcTime);

        @Transaction
        @Query("SELECT * FROM `lesson` WHERE teacherId = :groupId AND startMomentUtc <= :utcTime AND endMomentUtc >= :utcTime")
        LiveData<ScheduleLessonWithTeacher> getLessonByGroupAndTime(int groupId, Calendar utcTime);
    }

    public abstract HseTimetableDao hseTimetableDao();

    public static void initializeDummyData(HseTimetableDao dao) {
        final int teachersCount = 5;
        final int groupsCount = 5;
        final int lessonsCount = 5;

        for (int i = 0; i < teachersCount; i++) {
            Teacher teacher = new Teacher();
            teacher.name = "Teacher " + i;
            dao.insertTeacher(teacher);
        }

        int totalCounter = 0;
        for (int i = 0; i < groupsCount; i++) {
            StudentsGroup group = new StudentsGroup();
            group.name = "Group " + i;
            dao.insertGroup(group);

            Calendar timeStart = Calendar.getInstance(TimeZone.GMT_ZONE);
            Calendar timeEnd = (Calendar)timeStart.clone();
            timeEnd.add(Calendar.HOUR, 2);
            for (int j = 0; j < lessonsCount; j++) {
                ScheduleLesson lesson = new ScheduleLesson();
                lesson.startMomentUtc = (Calendar)timeStart.clone();
                lesson.endMomentUtc = (Calendar)timeEnd.clone();
                lesson.building = "Main building";
                lesson.room = "Some room";
                lesson.groupId = i+1;
                lesson.teacherId = ThreadLocalRandom.current().nextInt(teachersCount)+1;
                lesson.name = "Suffering " + (++totalCounter);
                lesson.type = "Seminar";
                timeStart.add(Calendar.HOUR, 2);
                timeEnd.add(Calendar.HOUR, 2);
                dao.insertLesson(lesson);
            }
        }
    }
}