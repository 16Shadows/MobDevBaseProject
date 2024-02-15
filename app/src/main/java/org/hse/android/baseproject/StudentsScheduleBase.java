package org.hse.android.baseproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class StudentsScheduleBase extends ScheduleActivityBase {
    public static final String EXTRA_GROUP = "group";

    protected StudentsGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        group = (StudentsGroup)getIntent().getSerializableExtra(EXTRA_GROUP);

        if (group != null)
            toolbar.setTitle(group.getName());
    }

    @Override
    protected LessonViewHolder createLessonViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentsLessonViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_schedule_students_lesson, parent, false)
        );
    }

    protected class StudentsLessonViewHolder extends LessonViewHolder {
        protected TextView teacher;

        public StudentsLessonViewHolder(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.activity_schedule_students_lesson_startTime);
            endTime = itemView.findViewById(R.id.activity_schedule_students_lesson_endTime);
            type = itemView.findViewById(R.id.activity_schedule_students_lesson_type);
            name = itemView.findViewById(R.id.activity_schedule_students_lesson_name);
            location = itemView.findViewById(R.id.activity_schedule_students_lesson_location);
            teacher = itemView.findViewById(R.id.activity_schedule_students_lesson_teacher);
        }

        @Override
        public void bind(@Nullable ScheduleLesson lesson) {
            super.bind(lesson);
            if (lesson == null)
                teacher.setText(R.string.loading);
            else
                teacher.setText(lesson.getTeacherName());
        }
    }
}
