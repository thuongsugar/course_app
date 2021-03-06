package com.example.course_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.course_mobile.R;
import com.example.course_mobile.model.course.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courseList;
    private Context context;
    private OnClickCourse onClickCourse;

    public void setDataCourses(List<Course> dataCourses){
        this.courseList = dataCourses;
        notifyDataSetChanged();
    }
    public CourseAdapter(){
    }
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_layout,parent,false);
        context = parent.getContext();
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        if (course == null) return;

//        holder.imvCourse.setImageResource(course.getImage());
        Glide.with(this.context)
                .load(course.getImage())
                .centerCrop()
                .placeholder(R.drawable.course_defaut)
                .into(holder.imvCourse);
        holder.tvRegisterNumber.setText(course.getRegisterNumber()+" " +"người đăng ký");
        holder.tvTitleCourse.setText(course.getSubject());
        holder.tvDesCourse.setText(course.getDes());

        holder.cvCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickCourse != null){
                    onClickCourse.onClick(course);
                }
                return;
            }
        });


    }
    public void onClickCourse(OnClickCourse onClickCourse){
        this.onClickCourse = onClickCourse;
    }
    public interface OnClickCourse{
        void onClick(Course courseClicked);
    }

    @Override
    public int getItemCount() {
        if (courseList != null){
            return courseList.size();
        }
        return 0;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder  {
        private ImageView imvCourse;
        private TextView tvRegisterNumber;
        private TextView tvTitleCourse;
        private TextView tvDesCourse;
        private CardView cvCourse;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            imvCourse = itemView.findViewById(R.id.imvCourse);
            tvRegisterNumber = itemView.findViewById(R.id.tvRegisterNumber);
            tvTitleCourse = itemView.findViewById(R.id.tvTitle);
            tvDesCourse = itemView.findViewById(R.id.tvDecs);
            cvCourse = itemView.findViewById(R.id.cvCourse);
        }
    }
}
