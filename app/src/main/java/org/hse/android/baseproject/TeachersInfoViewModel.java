package org.hse.android.baseproject;

import android.app.Application;
import android.icu.util.Calendar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TeachersInfoViewModel extends AndroidViewModel {
    public TeachersInfoViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<List<Teacher>> getTeachers() {
        return DatabaseProvider.getInstance(getApplication()).getHseTimetableDatabase().hseTimetableDao().getTeachers();
    }

    public LiveData<ScheduleLesson> getLessonByTeacherAndTime(int teacherId, Calendar utcTime) {
        return DatabaseProvider.getInstance(getApplication()).getHseTimetableDatabase().hseTimetableDao().getLessonByTeacherAndTime(teacherId, utcTime);
    }
}
