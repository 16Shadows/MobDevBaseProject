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
import android.text.Html;
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

public class StudentsTimetableActivity extends AppCompatActivity {
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

    private BroadcastReceiver timeTickReceiver;
    private TextView timeLabel;
    private TextView subjectLabel;
    private TextView roomLabel;
    private TextView buildingLabel;
    private TextView statusLabel;
    private TextView teacherLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_timetable);

        Toolbar toolbar = findViewById(R.id.activity_students_timetable_Toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        timeLabel = findViewById(R.id.activity_students_timetable_timeLabel);
        subjectLabel = findViewById(R.id.activity_students_timetable_subjectLabel);
        roomLabel = findViewById(R.id.activity_students_timetable_roomLabel);
        buildingLabel = findViewById(R.id.activity_students_timetable_buildingLabel);
        statusLabel = findViewById(R.id.activity_students_timetable_statusLabel);
        teacherLabel = findViewById(R.id.activity_students_timetable_teacherLabel);

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
        setTeacher("Undefined");
    }

    private void setTime(Date moment) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EEEE", Locale.getDefault());
        timeLabel.setText(Html.fromHtml(String.format(getResources().getString(R.string.time_s), formatter.format(moment)), Html.FROM_HTML_MODE_COMPACT));
    }

    private void setSubject(CharSequence name) {
        subjectLabel.setText(Html.fromHtml(String.format(getResources().getString(R.string.subject_s), name), Html.FROM_HTML_MODE_COMPACT));
    }

    private void setRoom(CharSequence room) {
        roomLabel.setText(Html.fromHtml(String.format(getResources().getString(R.string.room_s), room), Html.FROM_HTML_MODE_COMPACT));
    }

    private void setBuilding(CharSequence building) {
        buildingLabel.setText(Html.fromHtml(String.format(getResources().getString(R.string.building_s), building), Html.FROM_HTML_MODE_COMPACT));
    }

    private void setStatus(Boolean ongoingLesson) {
        int id;
        if (ongoingLesson)
            id = R.string.status_ongoing_lesson;
        else
            id = R.string.status_no_lesson;
        statusLabel.setText(id);
    }

    private void setTeacher(CharSequence teacher) {
        teacherLabel.setText(String.format(getResources().getString(R.string.teacher_s), teacher));
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