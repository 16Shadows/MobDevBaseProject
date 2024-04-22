package org.hse.android.baseproject;

import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class TeachersInfoActivity extends InfoActivityBase {
    protected TeachersInfoViewModel viewModel;
    protected Spinner teachersList;

    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_teachers_info);
        toolbar = findViewById(R.id.activity_teachers_info_Toolbar);

        timeLabel = findViewById(R.id.activity_teachers_info_time);
        subjectLabel = findViewById(R.id.activity_teachers_info_subject);
        roomLabel = findViewById(R.id.activity_teachers_info_room);
        buildingLabel = findViewById(R.id.activity_teachers_info_building);
        statusLabel = findViewById(R.id.activity_teachers_info_status);

        teachersList = findViewById(R.id.activity_teachers_info_teacherselect);

        ArrayAdapter<Teacher> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        teachersList.setAdapter(adapter);

        teachersList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Teacher teacher = (Teacher) parent.getAdapter().getItem(position);
                if (teacher != null)
                    InstantToast.show(view.getContext(), teacher.name, Toast.LENGTH_SHORT);
                updateTick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.activity_teachers_info_daily).setOnClickListener(v -> {
            Intent intent = new Intent(this, TeachersScheduleDaily.class);
            intent.putExtra(TeacherScheduleBase.EXTRA_TEACHER, (Teacher) teachersList.getSelectedItem());
            startActivity(intent);
        });

        findViewById(R.id.activity_teachers_info_weekly).setOnClickListener(v -> {
            Intent intent = new Intent(this, TeachersScheduleWeekly.class);
            intent.putExtra(TeacherScheduleBase.EXTRA_TEACHER, (Teacher) teachersList.getSelectedItem());
            startActivity(intent);
        });

        ViewModelProvider vmProvider = new ViewModelProvider(this);
        viewModel = vmProvider.get(TeachersInfoViewModel.class);

        viewModel.getTeachers().observe(this, teachers -> {
            adapter.clear();
            adapter.addAll(teachers);
        });
    }

    @Override
    protected void updateTick() {
        super.updateTick();

        if (teachersList.getSelectedItem() != null)
        {
            viewModel.getLessonByTeacherAndTime(((Teacher)teachersList.getSelectedItem()).id, Calendar.getInstance(TimeZone.GMT_ZONE)).observe(this, lesson -> {
                if (lesson == null)
                {
                    setSubject(null);
                    setRoom(null);
                    setBuilding(null);
                    setStatus(false);
                }
                else
                {
                    setSubject(lesson.name);
                    setRoom(lesson.room);
                    setBuilding(lesson.building);
                    setStatus(true);
                }
            });
        }
        else
        {
            setSubject(null);
            setRoom(null);
            setBuilding(null);
            setStatus(false);
        }
    }
}
