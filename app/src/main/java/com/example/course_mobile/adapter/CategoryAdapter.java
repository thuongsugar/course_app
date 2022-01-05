package com.example.course_mobile.adapter;

import android.content.Context;
import android.content.LocusId;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.course_mobile.R;
import com.example.course_mobile.model.category.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private OnClickCategory onClickCategory;

    public void setCategoryList(List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category == null){
            return;
        }
        holder.tvCategoryName.setText("#"+category.getName());
        holder.tvCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickCategory != null){
                    onClickCategory.onClick(category);
                }
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(categoryList != null){
            return categoryList.size();
        }
        return 0;
    }

    public void onClickCategory(OnClickCategory onClickCategory){
        this.onClickCategory = onClickCategory;
    }
    public interface OnClickCategory{
        void onClick(Category categoryClicked);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCategoryName;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
