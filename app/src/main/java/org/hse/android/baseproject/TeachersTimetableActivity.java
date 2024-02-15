package org.hse.android.baseproject;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeachersTimetableActivity extends BaseTimetableActivity {
    public static class Teacher {
        private final int id;

        public Teacher(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return "Teacher " + id;
        }

        @NonNull
        @Override
        public String toString() {
            return getName();
        }
    }

    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_teachers_timetable);
        toolbar = findViewById(R.id.activity_teachers_timetable_Toolbar);

        timeLabel = findViewById(R.id.activity_teachers_timetable_time);
        subjectLabel = findViewById(R.id.activity_teachers_timetable_subject);
        roomLabel = findViewById(R.id.activity_teachers_timetable_room);
        buildingLabel = findViewById(R.id.activity_teachers_timetable_building);
        statusLabel = findViewById(R.id.activity_teachers_timetable_status);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Teacher> teachersList = new ArrayList<>();
        initTeachersList(teachersList);

        Spinner spinner = findViewById(R.id.activity_teachers_timetable_teacherselect);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teachersList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Teacher teacher = (Teacher)parent.getAdapter().getItem(position);
                if (teacher != null)
                    InstantToast.show(view.getContext(), teacher.getName(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void updateTick() {
        setTime(new Date());

        //Dummy implementation
        setSubject(null);
        setRoom(null);
        setBuilding(null);
        setStatus(null);
    }

    private void initTeachersList(List<Teacher> list) {
        for (int i = 0; i < 15; i++)
            list.add(new Teacher(i));
    }
}