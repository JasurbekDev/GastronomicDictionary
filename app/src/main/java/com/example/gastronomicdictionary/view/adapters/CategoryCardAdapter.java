package com.example.gastronomicdictionary.view.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.example.gastronomicdictionary.view.activities.SearchActivity;

import java.util.List;

import static android.content.ContentValues.TAG;

public class CategoryCardAdapter extends RecyclerView.Adapter<CategoryCardAdapter.MyViewHolder> {
    private List<Category> categories;
    private CategoryCardAdapterListener listener;
    private Context mContext;

    public CategoryCardAdapter(Context context, List<Category> categories, CategoryCardAdapterListener listener) {
        this.mContext = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        return new MyViewHolder(mContext, view, listener);
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
        private Context mContext;

        public MyViewHolder(Context context, @NonNull final View itemView, final CategoryCardAdapterListener listener) {
            super(itemView);
            this.mContext = context;
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
            int initialMarginTop = dpToPixel(10);
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

            try {
                if (mContext.getResources().getDrawable(category.getCategoryImageResourceId()) != null) {
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
                    Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
                            category.getCategoryImageResourceId(), o);
                    int w = bmp.getWidth();
                    int h = bmp.getHeight();

                    Bitmap resBitmap = ((BitmapDrawable) mContext.getResources().getDrawable(category.getCategoryImageResourceId())).getBitmap();
//            Drawable drawable = mContext.getResources().getDrawable(category.getCategoryImageResourceId());
                    Bitmap scaled = Bitmap.createScaledBitmap(resBitmap, w / 2, h / 2, true);

                    categoryImage.setImageBitmap(scaled);
                }
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "bind: ", e);
            }





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
