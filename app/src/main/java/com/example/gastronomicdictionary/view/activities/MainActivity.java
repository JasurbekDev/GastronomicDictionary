package com.example.gastronomicdictionary.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.gastronomicdictionary.data.models.Word;
import com.example.gastronomicdictionary.view.adapters.CategoryCardAdapter;
import com.example.gastronomicdictionary.viewmodels.MainActivityViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CategoryCardAdapter.CategoryCardAdapterListener {
    private MainActivityViewModel viewModel;
    private CategoryCardAdapter adapter;
    private RecyclerView categoryRecyclerView;
    private SearchView searchView;
    private ConstraintLayout mainActivityRootLayout;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        categoryRecyclerView = findViewById(R.id.wordListRecyclerView);
        searchView = findViewById(R.id.wordListSearchView);
        mainActivityRootLayout = findViewById(R.id.mainActivityRootLayout);

        setSearchViewOnClickListener(searchView, this);

        adapter = new CategoryCardAdapter(this, new ArrayList<Category>(), this);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setAdapter(adapter);

        final List<Integer> categoryImages = new ArrayList<>();
        categoryImages.add(R.drawable.cheese_category);
        categoryImages.add(R.drawable.root_vegetables_category);
        categoryImages.add(R.drawable.alfala_category);
        categoryImages.add(R.drawable.adzuki_beans_category);
        categoryImages.add(R.drawable.buttery_category);
        categoryImages.add(R.drawable.aluminum_saucepan_category);
        categoryImages.add(R.drawable.serve_with_____category);
        categoryImages.add(R.drawable.apple_category);
        categoryImages.add(R.drawable.fry_category);
        categoryImages.add(R.drawable.to_spray_pan_category);
        categoryImages.add(R.drawable.appliance_category);
        categoryImages.add(R.drawable.zirvak_category);
        categoryImages.add(R.drawable.cool_category);
        categoryImages.add(R.drawable.thin_or_thinly_category);
        categoryImages.add(R.drawable.soaking_category);
        categoryImages.add(R.drawable.to_sort_category);
        categoryImages.add(R.drawable.season_to_taste_with_salt_category);
        categoryImages.add(R.drawable.combine_with_or_mix_____with_____category);
        categoryImages.add(R.drawable.for_a_finite_period_of_time_category);

        final SharedPreferences sharedPreferences = getSharedPreferences("MainSharedPreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("hasLoadedAllData", false);

        if (!sharedPreferences.getBoolean("hasLoadedAllData", false)) {
            dialog = ProgressDialog.show(MainActivity.this, "",
                    "Loading. Please wait...", true);
        }


        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if (words.size() != 0) {
                    int categoryImagesIndex = -1;
                    List<Category> categories = new ArrayList<>();
                    String categoryName = "";
                    for (int i = 0; i < words.size(); i++) {
                        if (!categoryName.equals(words.get(i).getCategoryEn())) {
                            categoryImagesIndex++;
                            categoryName = words.get(i).getCategoryEn();
                            String wordEn = words.get(i).getEn();
                            String formattedName = SearchActivity.getFormattedName(wordEn);
                            int imageId = getResources().getIdentifier(formattedName, "drawable", getPackageName());
                            if (categoryImagesIndex == categoryImages.size() - 1) {
                                dialog.dismiss();
                                sharedPreferences.edit().putBoolean("hasLoadedAllData", true);
                            }
                            categories.add(new Category(categoryName, categoryImages.get(categoryImagesIndex)));
                        }
                    }
                    adapter.setCategories(categories);
                }
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

    @Override
    protected void onResume() {
        super.onResume();
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