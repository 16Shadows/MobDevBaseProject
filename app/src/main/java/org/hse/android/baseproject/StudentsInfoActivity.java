package org.hse.android.baseproject;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentsInfoActivity extends InfoActivityBase {
    private TextView teacherLabel;

    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_students_info);
        toolbar = findViewById(R.id.activity_students_info_Toolbar);

        timeLabel = findViewById(R.id.activity_students_info_time);
        subjectLabel = findViewById(R.id.activity_students_info_subject);
        roomLabel = findViewById(R.id.activity_students_info_room);
        buildingLabel = findViewById(R.id.activity_students_info_building);
        statusLabel = findViewById(R.id.activity_students_info_status);
        teacherLabel = findViewById(R.id.activity_students_info_teacher);

        List<StudentsGroup> groupsList = new ArrayList<>();
        initGroupsList(groupsList);

        Spinner spinner = findViewById(R.id.activity_students_info_groupselect);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StudentsGroup group = (StudentsGroup) parent.getAdapter().getItem(position);
                if (group != null)
                    InstantToast.show(view.getContext(), group.getName(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.activity_students_info_daily).setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentsScheduleDaily.class);
            intent.putExtra(StudentsScheduleBase.EXTRA_GROUP, (StudentsGroup)spinner.getSelectedItem());
            startActivity(intent);
        });

        findViewById(R.id.activity_students_info_weekly).setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentsScheduleWeekly.class);
            intent.putExtra(StudentsScheduleBase.EXTRA_GROUP, (StudentsGroup)spinner.getSelectedItem());
            startActivity(intent);
        });
    }

    @Override
    protected void updateTick() {
        super.updateTick();

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

    private void initGroupsList(List<StudentsGroup> list) {
        int id = 0;

        String[] courses = {"ПИ", "БИ", "И", "Э", "ИЯ"};
        Integer[] years = {20, 21, 22, 23};
        Integer[] groups = {1, 2, 3};

        for (String course : courses) {
            for (Integer year :
                    years) {
                for (Integer group :
                        groups) {
                    list.add(new StudentsGroup(id++, String.format(Locale.getDefault(), "%s-%d-%d", course, year, group)));
                }
            }
        }
    }
}