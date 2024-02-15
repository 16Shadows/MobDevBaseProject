package org.hse.android.baseproject;

import android.icu.util.Calendar;

import java.util.ArrayList;

public class StudentsScheduleWeekly extends StudentsScheduleBase {
    @Override
    protected void initializeData() {
        ScheduleDay day = new ScheduleDay(Calendar.getInstance(), new ArrayList<>());

        day.getLessons().add(new ScheduleLesson(
                Calendar.getInstance(),
                Calendar.getInstance(),
                "Тестовый предмет",
                "Тест",
                "к. 205",
                "б. Гагарина 33",
                new Teacher(3))
        );

        day.getLessons().add(new ScheduleLesson(
                Calendar.getInstance(),
                Calendar.getInstance(),
                "Тестовый предмет 2",
                "Тест 2",
                "к. 207",
                "б. Гагарина 22",
                new Teacher(4))
        );

        days.add(day);

        day = new ScheduleDay(Calendar.getInstance(), new ArrayList<>());

        day.getLessons().add(new ScheduleLesson(
                Calendar.getInstance(),
                Calendar.getInstance(),
                "Тестовый предмет",
                "Тест",
                "к. 205",
                "б. Гагарина 33",
                new Teacher(2))
        );

        day.getLessons().add(new ScheduleLesson(
                Calendar.getInstance(),
                Calendar.getInstance(),
                "Тестовый предмет 2",
                "Тест 2",
                "к. 207",
                "б. Гагарина 22",
                new Teacher(1))
        );

        days.add(day);

        day = new ScheduleDay(Calendar.getInstance(), new ArrayList<>());

        day.getLessons().add(new ScheduleLesson(
                Calendar.getInstance(),
                Calendar.getInstance(),
                "Тестовый предмет",
                "Тест",
                "к. 205",
                "б. Гагарина 33",
                new Teacher(12))
        );

        day.getLessons().add(new ScheduleLesson(
                Calendar.getInstance(),
                Calendar.getInstance(),
                "Тестовый предмет 2",
                "Тест 2",
                "к. 207",
                "б. Гагарина 22",
                new Teacher(6))
        );

        days.add(day);

        daysAdapter.setData(days);
    }
}
