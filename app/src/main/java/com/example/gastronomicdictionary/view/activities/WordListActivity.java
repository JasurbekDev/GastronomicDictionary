package com.example.gastronomicdictionary.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gastronomicdictionary.R;
import com.example.gastronomicdictionary.data.models.Word;
import com.example.gastronomicdictionary.view.WordListBottomSheetDialog;
import com.example.gastronomicdictionary.view.adapters.WordListAdapter;
import com.example.gastronomicdictionary.viewmodels.WordListViewModel;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity implements WordListAdapter.WordListAdapterListener, View.OnClickListener, WordListBottomSheetDialog.BottomSheetListener {
    private WordListViewModel viewModel;
    private WordListAdapter adapter;
    private RecyclerView recyclerView;
    private WordListBottomSheetDialog bottomSheet;
    private SearchView searchView;
    private int sdk = Build.VERSION.SDK_INT;
    private View.OnClickListener onClickListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        recyclerView = findViewById(R.id.wordListRecyclerView);
        searchView = findViewById(R.id.wordListSearchView);

        bottomSheet = new WordListBottomSheetDialog();
        WordDetailsActivity.bottomSheet = bottomSheet;

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryName");

        adapter = new WordListAdapter(new ArrayList<Word>(), this, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(WordListViewModel.class);
        viewModel.getWordsByCategoryName(categoryName).observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                adapter.setWordList(words);
            }
        });

        setSearchViewOnClickListener(searchView, onClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(bottomSheet != null) {
//            bottomSheet.dismiss();
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onWordSelected(String wordUz, String wordRu, String wordEn) {
        Bundle bundle = new Bundle();
        bundle.putString("wordUz", wordUz);
        bundle.putString("wordRu", wordRu);
        bundle.putString("wordEn", wordEn);
        bottomSheet.setArguments(bundle);
        bottomSheet.show(getSupportFragmentManager(), "wordListBottomSheet");
    }

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
        Intent intent = new Intent(WordListActivity.this, SearchActivity.class);
        if (sdk >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(WordListActivity.this, searchView, ViewCompat.getTransitionName(searchView));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onBottomSheetButtonClick(ImageView wordImage, String wordUz, String wordRu, String wordEn, TextView wordRuTextView, TextView wordEnTextView) {
        Intent intent = new Intent(WordListActivity.this, WordDetailsActivity.class);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(WordListActivity.this, wordImage, ViewCompat.getTransitionName(wordImage));
        intent.putExtra("wordUz", wordUz);
        intent.putExtra("wordRu", wordRu);
        intent.putExtra("wordEn", wordEn);

        if (sdk >= Build.VERSION_CODES.LOLLIPOP) {
            Pair wordImageAnim = Pair.create(wordImage, ViewCompat.getTransitionName(wordImage));
            Pair wordRuAnim = Pair.create(wordRuTextView, ViewCompat.getTransitionName(wordRuTextView));
            Pair wordEnAnim = Pair.create(wordEnTextView, ViewCompat.getTransitionName(wordEnTextView));
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WordListActivity.this, wordImageAnim, wordRuAnim, wordEnAnim);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

//                bottomSheet.dismiss();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }, 200);
    }
}

//build listener to WordDetailsActivity and call method in onStart to dismiss dialog