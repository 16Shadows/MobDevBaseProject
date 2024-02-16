package org.hse.android.baseproject;

import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.Random;

public class StudentsScheduleWeekly extends StudentsScheduleBase {
    @Override
    protected void initializeData() {

        ScheduleDay day;
        ScheduleLesson lesson;
        Calendar calendar, lesStart, lesEnd;
        int lesCount;
        Random rnd = new Random();
        int daysCount = rnd.nextInt(5) + 1;
        for (int i = 0; i < daysCount; i++) {
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, i);
            day = new ScheduleDay(calendar, new ArrayList<>());

            lesCount = rnd.nextInt(5) + 1;
            for (int j = 0; j < lesCount; j++) {
                lesStart = (Calendar)calendar.clone();
                lesStart.add(Calendar.HOUR, j);
                lesEnd = (Calendar)lesStart.clone();
                lesEnd.add(Calendar.HOUR, 1);

                lesson = new ScheduleLesson(
                        lesStart,
                        lesEnd,
                        "Lesson " + j,
                        "Lesson Type",
                        "Room " + rnd.nextInt(15),
                        "Building " + rnd.nextInt(5),
                        new Teacher(rnd.nextInt(15)));

                day.getLessons().add(lesson);
            }

            days.add(day);
        }
        daysAdapter.setData(days);
    }
}
