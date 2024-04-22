package org.hse.android.baseproject;

import android.app.Application;
import android.icu.util.Calendar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StudentsScheduleViewModel extends AndroidViewModel {
    public StudentsScheduleViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<List<ScheduleLessonWithTeacher>> filterLessonsForGroup(int groupId, Calendar utcFrom, Calendar utcTo) {
        return DatabaseProvider.getInstance(getApplication()).getHseTimetableDatabase().hseTimetableDao().filterLessonsForGroup(groupId, utcFrom, utcTo);
    }
}
