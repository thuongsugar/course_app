package com.example.course_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.course_mobile.R;
import com.example.course_mobile.model.quiz.Quiz;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private List<Quiz> quizList;
    private OnClickQuiz onClickQuiz;

    public void setQuizList(List<Quiz> quizList){
        this.quizList = quizList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_layout,parent,false);

        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        if (quiz == null) return;
        holder.tvName.setText(quiz.getName());
        holder.tvNumQues.setText(quiz.getNumberQuestion() +"cau");
        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickQuiz !=  null){
                    onClickQuiz.onClick(quiz);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(quizList != null){
            return quizList.size();
        }
        return 0;
    }
    public void setOnClickQuiz(OnClickQuiz onClickQuiz){
        this.onClickQuiz = onClickQuiz;
    }

    public interface OnClickQuiz{
        void onClick(Quiz quiz);
    }

    class QuizViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName,tvNumQues;
        private Button btnStart;
        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvQuizName);
            tvNumQues = itemView.findViewById(R.id.tvNumberQuestionQuiz);
            btnStart = itemView.findViewById(R.id.btnStartQuiz);
        }
    }
}
