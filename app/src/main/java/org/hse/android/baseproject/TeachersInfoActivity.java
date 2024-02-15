package org.hse.android.baseproject;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TeachersInfoActivity extends InfoActivityBase {
    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_teachers_info);
        toolbar = findViewById(R.id.activity_teachers_info_Toolbar);

        timeLabel = findViewById(R.id.activity_teachers_info_time);
        subjectLabel = findViewById(R.id.activity_teachers_info_subject);
        roomLabel = findViewById(R.id.activity_teachers_info_room);
        buildingLabel = findViewById(R.id.activity_teachers_info_building);
        statusLabel = findViewById(R.id.activity_teachers_info_status);

        List<Teacher> teachersList = new ArrayList<>();
        initTeachersList(teachersList);

        Spinner spinner = findViewById(R.id.activity_teachers_info_teacherselect);

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

        findViewById(R.id.activity_teachers_info_daily).setOnClickListener(v -> {
            Intent intent = new Intent(this, TeachersScheduleDaily.class);
            intent.putExtra(TeacherScheduleBase.EXTRA_TEACHER, (Teacher)spinner.getSelectedItem());
            startActivity(intent);
        });

        findViewById(R.id.activity_teachers_info_weekly).setOnClickListener(v -> {
            Intent intent = new Intent(this, TeachersScheduleWeekly.class);
            intent.putExtra(TeacherScheduleBase.EXTRA_TEACHER, (Teacher)spinner.getSelectedItem());
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
    }

    private void initTeachersList(List<Teacher> list) {
        for (int i = 0; i < 15; i++)
            list.add(new Teacher(i));
    }
}