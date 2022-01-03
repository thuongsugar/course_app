package com.example.course_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.course_mobile.R;
import com.example.course_mobile.model.course.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courseList;

    public void setDataCourses(List<Course> dataCourses){
        this.courseList = dataCourses;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_layout,parent,false);

        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        if (course == null) return;

//        holder.imvCourse.setImageResource(course.getImage());
        holder.tvRegisterNumber.setText(course.getRegisterNumber()+" nguoi dang ky");
        holder.tvTitleCourse.setText(course.getSubject());
        holder.tvDesCourse.setText(course.getDes());

    }

    @Override
    public int getItemCount() {
        if (courseList != null){
            return courseList.size();
        }
        return 0;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder  {
        private CardView cardViewCourse;
        private ImageView imvCourse;
        private TextView tvRegisterNumber;
        private TextView tvTitleCourse;
        private TextView tvDesCourse;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewCourse = itemView.findViewById(R.id.cvCourse);
            imvCourse = itemView.findViewById(R.id.imvCourse);
            tvRegisterNumber = itemView.findViewById(R.id.tvRegisterNumber);
            tvTitleCourse = itemView.findViewById(R.id.tvTitle);
            tvDesCourse = itemView.findViewById(R.id.tvDecs);

        }
    }
}
