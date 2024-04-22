package org.hse.android.baseproject;

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

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

abstract class InfoActivityBase extends ToolbarActivityBase {
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

    protected void updateTick() {
        TimeQuery.requestTime().observe(this, this::setTime);
    }

    protected void setTime(@Nullable Date moment) {
        if (moment == null)
            timeLabel.setText(R.string.loading);
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EEEE", Locale.getDefault());
            SimpleDateFormatEx.capitalizeWeekdays(formatter);
            timeLabel.setText(formatter.format(moment));
        }
    }

    protected void setSubject(@Nullable CharSequence name) {
        if (name == null)
            name = getResources().getString(R.string.nothing);

        subjectLabel.setText(name);
    }

    protected void setRoom(@Nullable CharSequence room) {
        if (room == null)
            room = getResources().getString(R.string.nothing);

        roomLabel.setText(room);
    }

    protected void setBuilding(@Nullable CharSequence building) {
        if (building == null)
            building = getResources().getString(R.string.nothing);

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

