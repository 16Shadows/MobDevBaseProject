package org.hse.android.baseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toast _LastToast;

    private void InstantToast(String message, int duration)
    {
        if (_LastToast != null)
            _LastToast.cancel();

        _LastToast = Toast.makeText(this, message, duration);
        _LastToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main_StudentsTimetableButton)
                .setOnClickListener(view -> InstantToast( ((Button)view).getText().toString() , Toast.LENGTH_SHORT));

        findViewById(R.id.activity_main_TeachersTimetableButton)
                .setOnClickListener(view -> InstantToast( ((Button)view).getText().toString(), Toast.LENGTH_SHORT));
    }

}