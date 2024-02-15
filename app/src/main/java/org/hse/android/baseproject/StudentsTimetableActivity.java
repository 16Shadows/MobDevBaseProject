package org.hse.android.baseproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentsTimetableActivity extends BaseTimetableActivity {
    public static class Group {
        private final int id;
        private final String name;

        public Group(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @NonNull
        @Override
        public String toString() {
            return getName();
        }
    }

    private TextView teacherLabel;

    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_students_timetable);
        toolbar = findViewById(R.id.activity_students_timetable_Toolbar);

        timeLabel = findViewById(R.id.activity_students_timetable_time);
        subjectLabel = findViewById(R.id.activity_students_timetable_subject);
        roomLabel = findViewById(R.id.activity_students_timetable_room);
        buildingLabel = findViewById(R.id.activity_students_timetable_building);
        statusLabel = findViewById(R.id.activity_students_timetable_status);
        teacherLabel = findViewById(R.id.activity_students_timetable_teacher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Group> groupsList = new ArrayList<>();
        initGroupsList(groupsList);

        Spinner spinner = findViewById(R.id.activity_students_timetable_groupselect);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Group group = (Group) parent.getAdapter().getItem(position);
                if (group != null)
                    InstantToast.show(view.getContext(), group.getName(), Toast.LENGTH_SHORT);
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
        setTeacher(null);
    }

    protected void setTeacher(@Nullable CharSequence teacher) {
        if (teacher == null)
            teacher = getResources().getString(R.string.loading);

        teacherLabel.setText(teacher);
    }

    private void initGroupsList(List<Group> list) {
        int id = 0;

        String[] courses = {"ПИ", "БИ", "И", "Э", "ИЯ"};
        Integer[] years = {20, 21, 22, 23};
        Integer[] groups = {1, 2, 3};

        for (String course : courses) {
            for (Integer year :
                    years) {
                for (Integer group :
                        groups) {
                    list.add(new Group(id++, String.format(Locale.getDefault(), "%s-%d-%d", course, year, group)));
                }
            }
        }
    }
}