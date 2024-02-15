package org.hse.android.baseproject;

import android.content.Intent;

public class MainActivity extends ActivityBase {

    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main_StudentsTimetableButton)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(this, StudentsInfoActivity.class);
                    startActivity(intent);
                });

        findViewById(R.id.activity_main_TeachersTimetableButton)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(this, TeachersInfoActivity.class);
                    startActivity(intent);
                });

        findViewById(R.id.activity_main_SettingsButton)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                });
    }
}