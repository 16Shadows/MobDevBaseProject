package org.hse.android.baseproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Locale;

public abstract class BaseTimetableActivity extends BaseToolbarActivity {
    protected TextView timeLabel;
    protected TextView subjectLabel;
    protected TextView roomLabel;
    protected TextView buildingLabel;
    protected TextView statusLabel;

    protected BroadcastReceiver timeTickReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    protected abstract void updateTick();

    protected void setTime(Date moment) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EEEE", Locale.getDefault());
        timeLabel.setText(formatter.format(moment));
    }

    protected void setSubject(@Nullable CharSequence name) {
        if (name == null)
            name = getResources().getString(R.string.loading);

        subjectLabel.setText(name);
    }

    protected void setRoom(@Nullable CharSequence room) {
        if (room == null)
            room = getResources().getString(R.string.loading);

        roomLabel.setText(room);
    }

    protected void setBuilding(@Nullable CharSequence building) {
        if (building == null)
            building = getResources().getString(R.string.loading);

        buildingLabel.setText(building);
    }

    protected void setStatus(@Nullable Boolean ongoingLesson) {
        int id;
        if (ongoingLesson == null)
            id = R.string.loading;
        else if (ongoingLesson)
            id = R.string.status_ongoing_lesson;
        else
            id = R.string.status_no_lesson;
        statusLabel.setText(id);
    }
}
