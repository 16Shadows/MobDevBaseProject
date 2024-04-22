package org.hse.android.baseproject;

import android.app.Application;
import android.icu.util.Calendar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TeachersScheduleViewModel extends AndroidViewModel {
    public TeachersScheduleViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<List<ScheduleLessonWithTeacher>> filterLessonsForTeacher(int teacherId, Calendar utcFrom, Calendar utcTo) {
        return DatabaseProvider.getInstance(getApplication()).getHseTimetableDatabase().hseTimetableDao().filterLessonsForTeacher(teacherId, utcFrom, utcTo);
    }
}
