package com.example.course_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.course_mobile.R;
import com.example.course_mobile.model.lesson.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {
    private List<Lesson> lessonList;
    private OnClickLesson onClickLesson;

    public void setLessonList(List<Lesson> lessonList){
        this.lessonList = lessonList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_layout,parent,false);

        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);
        if (lesson == null){
            return;
        }
        holder.tvLessonName.setText(lesson.getSubject());
        holder.cvLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickLesson != null){
                    onClickLesson.onClick(lesson);
                }
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (lessonList != null){
            return  lessonList.size();
        }
        return 0;
    }
    public void setOnClickLesson(OnClickLesson onClickLesson){
        this.onClickLesson = onClickLesson;
    }

    public interface OnClickLesson{
        void onClick(Lesson lesson);
    }


    class LessonViewHolder extends RecyclerView.ViewHolder{
        private CardView cvLayout;
        private TextView tvLessonName;
        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            cvLayout = itemView.findViewById(R.id.cvLayout);
            tvLessonName = itemView.findViewById(R.id.tvLessonName);
        }
    }
}
