package org.hse.android.baseproject;

import android.app.Application;
import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class DatabaseProvider {
    @Database(entities = {StudentsGroup.class, Teacher.class, ScheduleLesson.class}, version = 1, exportSchema = false)
    @TypeConverters(HseTimetableDatabase.CalendarConverter.class)
    static abstract class HseTimetableDatabase extends RoomDatabase {
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

            @Query("SELECT * FROM `lesson` WHERE groupId = :groupId AND startMomentUtc >= :utcFrom AND startMomentUtc <= :utcTo ORDER BY startMomentUtc")
            LiveData<List<ScheduleLessonWithTeacher>> filterLessonsForGroup(int groupId, Calendar utcFrom, Calendar utcTo);

            @Insert
            void insertTeacher(Teacher teacher);

            @Query("SELECT * FROM `teacher`")
            LiveData<List<Teacher>> getTeachers();

            @Query("SELECT * FROM `lesson` WHERE teacherId = :teacherId AND startMomentUtc >= :utcFrom AND startMomentUtc <= :utcTo ORDER BY startMomentUtc")
            LiveData<List<ScheduleLessonWithTeacher>> filterLessonsForTeacher(int teacherId, Calendar utcFrom, Calendar utcTo);

            @Insert
            void insertLesson(ScheduleLesson lesson);

            @Query("SELECT * FROM `lesson` WHERE teacherId = :teacherId AND startMomentUtc <= :utcTime AND endMomentUtc >= :utcTime")
            LiveData<ScheduleLesson> getLessonByTeacherAndTime(int teacherId, Calendar utcTime);

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

    private final HseTimetableDatabase hseTimetableDatabase;

    private DatabaseProvider(Context context) {
        context.deleteDatabase(HseTimetableDatabase.DATABASE_NAME);
        hseTimetableDatabase = Room.databaseBuilder(context, HseTimetableDatabase.class, HseTimetableDatabase.DATABASE_NAME)
                                   .addCallback(new RoomDatabase.Callback() {
                                       @Override
                                       public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                           Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                               @Override
                                               public void run() {
                                                   new Thread(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           HseTimetableDatabase.initializeDummyData(getHseTimetableDatabase().hseTimetableDao());
                                                       }
                                                   }).start();
                                               }
                                           });
                                       }
                                   })
                                   .build();
    }

    public HseTimetableDatabase getHseTimetableDatabase() {
        return hseTimetableDatabase;
    }

    private static volatile DatabaseProvider instance;

    public static DatabaseProvider getInstance(Context ctx) {
        if (instance == null) {
            synchronized (DatabaseProvider.class) {
                if (instance == null) {
                    instance = new DatabaseProvider(ctx);
                }
            }
        }
        return instance;
    }
}
