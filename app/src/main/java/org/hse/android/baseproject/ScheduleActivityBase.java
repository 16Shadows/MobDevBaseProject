package org.hse.android.baseproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class ScheduleActivityBase extends ToolbarActivityBase {

    public class LessonViewHolder extends RecyclerView.ViewHolder {

        protected TextView startTime;
        protected TextView endTime;
        protected TextView type;
        protected TextView name;
        protected TextView location;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(@Nullable ScheduleLesson lesson) {
            if (lesson == null) {
                startTime.setText(R.string.loading);
                endTime.setText(R.string.loading);
                type.setText(R.string.loading);
                name.setText(R.string.loading);
                location.setText(R.string.loading);
            }
            else {
                startTime.setText(lesson.getStartTime());
                endTime.setText(lesson.getEndTime());
                type.setText(lesson.getType());
                name.setText(lesson.getName());
                location.setText( String.format( getResources().getString(R.string.schedule_location), lesson.getRoom(), lesson.getBuilding() ) );
            }
        }
    }

    public class LessonsAdapter extends RecyclerView.Adapter<LessonViewHolder> {
        @Nullable
        protected List<ScheduleLesson> lessons;

        @SuppressLint("NotifyDataSetChanged")
        void setData(List<ScheduleLesson> lessons) {
            this.lessons = lessons;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return createLessonViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
            holder.bind(lessons == null ? null : lessons.get(position));
        }

        @Override
        public int getItemCount() {
            return lessons == null ? 0 : lessons.size();
        }
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {

        protected TextView currentDay;
        protected RecyclerView lessonsList;
        protected LessonsAdapter lessonsAdapter;

        public DayViewHolder(@NonNull View itemView, RecyclerView.RecycledViewPool lessonsPool) {
            super(itemView);

            currentDay = itemView.findViewById(R.id.activity_schedule_day_currentDay);
            lessonsList = itemView.findViewById(R.id.activity_schedule_day_Lessons);

            lessonsAdapter = new LessonsAdapter();

            lessonsList.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
            lessonsList.setRecycledViewPool(lessonsPool);
            lessonsList.setAdapter(lessonsAdapter);
            lessonsList.addItemDecoration(new DividerItemDecoration(itemView.getContext(), DividerItemDecoration.VERTICAL));
        }

        public void bind(@Nullable ScheduleDay day) {
            if (day != null) {
                currentDay.setText(day.getName());
                lessonsAdapter.setData(day.getLessons());
            }
        }
    }

    public class DaysAdapter extends RecyclerView.Adapter<DayViewHolder> {

        protected RecyclerView.RecycledViewPool lessonsPool;
        @Nullable
        protected List<ScheduleDay> days;

        public DaysAdapter(RecyclerView.RecycledViewPool lessonsPool) {
            this.lessonsPool = lessonsPool;
        }

        @SuppressLint("NotifyDataSetChanged")
        void setData(List<ScheduleDay> days) {
            this.days = days;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DayViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_schedule_day, parent, false),
                    lessonsPool
            );
        }

        @Override
        public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
            holder.bind(days == null ? null : days.get(position));
        }

        @Override
        public int getItemCount() {
            return days == null ? 0 : days.size();
        }
    }

    protected RecyclerView daysList;
    protected RecyclerView.RecycledViewPool lessonsPool;
    protected DaysAdapter daysAdapter;
    protected List<ScheduleDay> days;
    protected BroadcastReceiver timeTickReceiver;
    protected TextView timeLabel;

    public ScheduleActivityBase() {
        lessonsPool = new RecyclerView.RecycledViewPool();
        lessonsPool.setMaxRecycledViews(0, 8);
        days = new ArrayList<>();
    }

    @Override
    protected void initializeLayout() {
        setContentView(R.layout.activity_schedule_base);

        toolbar = findViewById(R.id.activity_schedule_Toolbar);
        daysList = findViewById(R.id.activity_schedule_days);
        timeLabel = findViewById(R.id.activity_schedule_now);

        daysAdapter = new DaysAdapter(lessonsPool);

        daysList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        daysList.setAdapter(daysAdapter);

        initializeData();

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
        TimeQuery.requestTime(date -> runOnUiThread(() -> setTime(date)));
    }

    protected void setTime(@Nullable Date moment) {
        if (moment == null)
            timeLabel.setText(R.string.loading);
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault());
            timeLabel.setText(formatter.format(moment));
        }
    }

    protected abstract void initializeData();
    protected abstract LessonViewHolder createLessonViewHolder(@NonNull ViewGroup parent, int viewType);
}