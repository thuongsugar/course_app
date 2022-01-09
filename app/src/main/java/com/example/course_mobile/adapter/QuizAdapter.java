package com.example.course_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.course_mobile.R;
import com.example.course_mobile.model.quiz.Quiz;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private List<Quiz> quizList;
    private OnClickQuiz onClickQuiz;
    private String imvQuiz;
    private Context context;

    public void setQuizList(List<Quiz> quizList,String imvQuiz){
        this.quizList = quizList;
        this.imvQuiz = imvQuiz;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_layout,parent,false);
        context = parent.getContext();
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        if (quiz == null) return;
        Glide.with(context)
                .load(imvQuiz)
                .centerCrop()
                .placeholder(R.drawable.course_defaut)
                .into(holder.imvQuiz);

        holder.tvName.setText(quiz.getName());
        holder.tvNumQues.setText(quiz.getNumberQuestion() +" câu hỏi");
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
        private ImageView imvQuiz;
        private TextView tvName,tvNumQues;
        private Button btnStart;
        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            imvQuiz = itemView.findViewById(R.id.imvQuiz);
            tvName = itemView.findViewById(R.id.tvQuizName);
            tvNumQues = itemView.findViewById(R.id.tvNumberQuestionQuiz);
            btnStart = itemView.findViewById(R.id.btnStartQuiz);
        }
    }
}
