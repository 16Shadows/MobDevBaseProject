package org.hse.android.baseproject;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeachersScheduleDaily extends TeacherScheduleBase {
    @Override
    protected void onCreate(@Nullable Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        Calendar now = Calendar.getInstance();
        Calendar end = (Calendar)now.clone();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        end.set(Calendar.HOUR, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        viewModel.filterLessonsForTeacher(teacher.id, now, end).observe(this, lessons -> {
            ScheduleDay day = new ScheduleDay(now);
            for (ScheduleLessonWithTeacher lesson: lessons) {
                day.addLesson(lesson);
            }
            List<ScheduleDay> days = new ArrayList<>();
            days.add(day);
            daysAdapter.setData(days);
        });
    }
}
