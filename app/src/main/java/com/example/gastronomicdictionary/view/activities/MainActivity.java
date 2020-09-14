package com.example.gastronomicdictionary.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gastronomicdictionary.R;
import com.example.gastronomicdictionary.data.models.Category;
import com.example.gastronomicdictionary.view.adapters.CategoryCardAdapter;
import com.example.gastronomicdictionary.viewmodels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CategoryCardAdapter.CategoryCardAdapterListener {
    private MainActivityViewModel viewModel;
    private CategoryCardAdapter adapter;
    private RecyclerView categoryRecyclerView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryRecyclerView = findViewById(R.id.wordListRecyclerView);
        searchView = findViewById(R.id.wordListSearchView);

        setSearchViewOnClickListener(searchView, this);

        adapter = new CategoryCardAdapter(new ArrayList<Category>(), this);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        categoryRecyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getCategoryNames().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                adapter.setCategories(categories);
            }
        });

//        searchView.clearFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        hideKeyboard(this, searchView.getApplicationWindowToken());
//        toggleKeyboard(this);
    }

//    public static void hideKeyboard(Context context, IBinder token) {
//        InputMethodManager imm = (InputMethodManager) context
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if(imm.isActive()){
//            imm.hideSoftInputFromWindow(token,0);
//        }
//    }

//    public static void toggleKeyboard(Context context) {
//        InputMethodManager imm = (InputMethodManager) context
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
//            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }

    public static void setSearchViewOnClickListener(View v, View.OnClickListener listener) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout || child instanceof RelativeLayout) {
                    setSearchViewOnClickListener(child, listener);
                }

                if (child instanceof TextView) {
                    TextView text = (TextView) child;
                    text.setFocusable(false);
                }
                child.setOnClickListener(listener);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int sdk = Build.VERSION.SDK_INT;
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        if (sdk >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, searchView, ViewCompat.getTransitionName(searchView));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onItemViewClick(String categoryName) {
        Intent intent = new Intent(MainActivity.this, WordListActivity.class);
        intent.putExtra("categoryName", categoryName);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}