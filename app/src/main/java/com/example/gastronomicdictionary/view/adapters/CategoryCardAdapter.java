package com.example.gastronomicdictionary.view.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gastronomicdictionary.R;
import com.example.gastronomicdictionary.data.models.Category;

import java.util.List;

public class CategoryCardAdapter extends RecyclerView.Adapter<CategoryCardAdapter.MyViewHolder> {
    private List<Category> categories;
    private CategoryCardAdapterListener listener;

    public CategoryCardAdapter(List<Category> categories, CategoryCardAdapterListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        return new MyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (categories.size() != 0) {
            holder.bind(categories.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImage;
        private TextView categoryText;
        private CategoryCardAdapterListener listener;

        public MyViewHolder(@NonNull final View itemView, final CategoryCardAdapterListener listener) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryText = itemView.findViewById(R.id.categoryText);
            this.listener = listener;
        }

        void bind(final Category category) {
            int cardHeight = dpToPixel(150);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, cardHeight);
            int innerMargin = dpToPixel(5);
            int cardMarginBottom = dpToPixel(15);
            int horizontalEdgeMargin = dpToPixel(20);
            int initialMarginTop = dpToPixel(120);
            if (getAdapterPosition() == 0) {
                params.setMargins(horizontalEdgeMargin, initialMarginTop, innerMargin, cardMarginBottom);
            } else if (getAdapterPosition() == 1) {
                params.setMargins(innerMargin, initialMarginTop, horizontalEdgeMargin, cardMarginBottom);
            } else if (getAdapterPosition() % 2 == 0) {
                params.setMargins(horizontalEdgeMargin, 0, innerMargin, cardMarginBottom);
            } else {
                params.setMargins(innerMargin, 0, horizontalEdgeMargin, cardMarginBottom);
            }
            itemView.setLayoutParams(params);
            categoryImage.setImageResource(category.getCategoryImageResourceId());
            categoryText.setText(category.getCategoryName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemViewClick(category.getCategoryName());
                }
            });
        }

        private int dpToPixel(int dpValue) {
            return Math.round(new TypedValue().applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, itemView.getResources().getDisplayMetrics()));
        }
    }

    public interface CategoryCardAdapterListener {
        void onItemViewClick(String categoryName);
    }
}
