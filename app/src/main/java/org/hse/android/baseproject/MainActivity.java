package org.hse.android.baseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main_StudentsTimetableButton)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(this, StudentsTimetableActivity.class);
                    startActivity(intent);
                });

        findViewById(R.id.activity_main_TeachersTimetableButton)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(this, TeachersTimetableActivity.class);
                    startActivity(intent);
                });
    }
}