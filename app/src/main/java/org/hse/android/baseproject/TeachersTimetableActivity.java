package org.hse.android.baseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.SimpleDateFormat;
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

public class TeachersTimetableActivity extends AppCompatActivity {
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

    private BroadcastReceiver timeTickReceiver;
    private TextView timeLabel;
    private TextView subjectLabel;
    private TextView roomLabel;
    private TextView buildingLabel;
    private TextView statusLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_timetable);

        Toolbar toolbar = findViewById(R.id.activity_teachers_timetable_Toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        timeLabel = findViewById(R.id.activity_teachers_timetable_time);
        subjectLabel = findViewById(R.id.activity_teachers_timetable_subject);
        roomLabel = findViewById(R.id.activity_teachers_timetable_room);
        buildingLabel = findViewById(R.id.activity_teachers_timetable_building);
        statusLabel = findViewById(R.id.activity_teachers_timetable_status);

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

        updateTick();

        timeTickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateTick();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(timeTickReceiver);
    }

    private void updateTick() {
        setTime(new Date());

        //Dummy implementation
        setSubject("Undefined");
        setRoom("Undefined");
        setBuilding("Undefined");
        setStatus(false);
    }

    private void setTime(Date moment)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EEE", Locale.getDefault());
        timeLabel.setText( String.format( getResources().getString(R.string.time_s),  formatter.format(moment) ) );
    }

    private void setSubject(CharSequence name) {
        subjectLabel.setText( String.format( getResources().getString(R.string.subject_s), name ) );
    }

    private void setRoom(CharSequence room) {
        roomLabel.setText( String.format( getResources().getString(R.string.room_s), room ) );
    }

    private void setBuilding(CharSequence building) {
        buildingLabel.setText( String.format( getResources().getString(R.string.subject_s), building ) );
    }

    private void setStatus(Boolean ongoingLesson) {
        int id;
        if (ongoingLesson)
            id = R.string.status_ongoing_lesson;
        else
            id = R.string.status_no_lesson;
        statusLabel.setText(id);
    }

    private void initTeachersList(List<Teacher> list) {
        for (int i = 0; i < 15; i++)
            list.add(new Teacher(i));
    }
}