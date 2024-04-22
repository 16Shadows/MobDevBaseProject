package org.hse.android.baseproject;

import android.app.Application;
import android.icu.util.Calendar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StudentsInfoViewModel extends AndroidViewModel {
    public StudentsInfoViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<List<StudentsGroup>> getGroups() {
        return DatabaseProvider.getInstance(getApplication()).getHseTimetableDatabase().hseTimetableDao().getGroups();
    }

    public LiveData<ScheduleLessonWithTeacher> getLessonByGroupAndTime(int groupId, Calendar utcTime) {
        return DatabaseProvider.getInstance(getApplication()).getHseTimetableDatabase().hseTimetableDao().getLessonByGroupAndTime(groupId, utcTime);
    }
}
