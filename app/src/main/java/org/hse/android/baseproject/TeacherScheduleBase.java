package org.hse.android.baseproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;


abstract class TeacherScheduleBase extends ScheduleActivityBase {
    public static final String EXTRA_TEACHER = "teacher";

    protected TeachersScheduleViewModel viewModel;

    protected Teacher teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        teacher = (Teacher)getIntent().getSerializableExtra(EXTRA_TEACHER);

        if (teacher != null)
            toolbar.setTitle(teacher.name);

        ViewModelProvider vmProvider = new ViewModelProvider(this);
        viewModel = vmProvider.get(TeachersScheduleViewModel.class);
    }

    @Override
    protected LessonViewHolder createLessonViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeacherLessonViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_schedule_teacher_lesson, parent, false)
        );
    }

    protected class TeacherLessonViewHolder extends LessonViewHolder {

        public TeacherLessonViewHolder(@NonNull View itemView) {
            super(itemView);

            startTime = itemView.findViewById(R.id.activity_schedule_teacher_lesson_startTime);
            endTime = itemView.findViewById(R.id.activity_schedule_teacher_lesson_endTime);
            type = itemView.findViewById(R.id.activity_schedule_teacher_lesson_type);
            name = itemView.findViewById(R.id.activity_schedule_teacher_lesson_name);
            location = itemView.findViewById(R.id.activity_schedule_teacher_lesson_location);
        }
    }
}

