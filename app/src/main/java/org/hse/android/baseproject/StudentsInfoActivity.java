package org.hse.android.baseproject;

import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class StudentsInfoActivity extends InfoActivityBase {
    private TextView teacherLabel;
    protected StudentsInfoViewModel viewModel;
    protected Spinner groupsList;

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

        groupsList = findViewById(R.id.activity_students_info_groupselect);

        ArrayAdapter<StudentsGroup> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        groupsList.setAdapter(adapter);

        groupsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StudentsGroup group = (StudentsGroup) parent.getAdapter().getItem(position);
                if (group != null)
                    InstantToast.show(view.getContext(), group.name, Toast.LENGTH_SHORT);
                updateTick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.activity_students_info_daily).setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentsScheduleDaily.class);
            intent.putExtra(StudentsScheduleBase.EXTRA_GROUP, (StudentsGroup) groupsList.getSelectedItem());
            startActivity(intent);
        });

        findViewById(R.id.activity_students_info_weekly).setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentsScheduleWeekly.class);
            intent.putExtra(StudentsScheduleBase.EXTRA_GROUP, (StudentsGroup) groupsList.getSelectedItem());
            startActivity(intent);
        });

        ViewModelProvider vmProvider = new ViewModelProvider(this);
        viewModel = vmProvider.get(StudentsInfoViewModel.class);

        viewModel.getGroups().observe(this, groups -> {
            adapter.clear();
            adapter.addAll(groups);
        });
    }

    @Override
    protected void updateTick() {
        super.updateTick();

        if (groupsList.getSelectedItem() != null)
        {
            viewModel.getLessonByGroupAndTime(((StudentsGroup)groupsList.getSelectedItem()).id, Calendar.getInstance(TimeZone.GMT_ZONE)).observe(this, lesson -> {
                if (lesson == null)
                {
                    setSubject(null);
                    setRoom(null);
                    setBuilding(null);
                    setStatus(false);
                }
                else
                {
                    setSubject(lesson.lesson.name);
                    setRoom(lesson.lesson.room);
                    setBuilding(lesson.lesson.building);
                    setTeacher(lesson.teacher.name);
                    setStatus(true);
                }
            });
        }
        else
        {
            setSubject(null);
            setRoom(null);
            setBuilding(null);
            setTeacher(null);
            setStatus(false);
        }
    }

    protected void setTeacher(@Nullable CharSequence teacher) {
        if (teacher == null)
            teacher = getResources().getString(R.string.nothing);

        teacherLabel.setText(teacher);
    }
}
