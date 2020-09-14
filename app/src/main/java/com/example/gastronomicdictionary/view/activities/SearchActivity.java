package com.example.gastronomicdictionary.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Pair;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gastronomicdictionary.R;
import com.example.gastronomicdictionary.data.models.Word;
import com.example.gastronomicdictionary.view.WordListBottomSheetDialog;
import com.example.gastronomicdictionary.view.adapters.WordListAdapter;
import com.example.gastronomicdictionary.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements WordListAdapter.WordListAdapterListener, WordListBottomSheetDialog.BottomSheetListener {
    private SearchView searchView;
    private RecyclerView searchActivityRecyclerView;
    private WordListAdapter adapter;
    private SearchViewModel viewModel;
    private WordListBottomSheetDialog bottomSheet;
    private int sdk = Build.VERSION.SDK_INT;
    private TextView wordNotFoundTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        if (sdk >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(enterTransition());
            getWindow().setSharedElementReturnTransition(returnTransition());
        }

        searchView = findViewById(R.id.searchActivitySearchView);
        searchActivityRecyclerView = findViewById(R.id.searchActivityRecyclerView);
        wordNotFoundTextView = findViewById(R.id.wordNotFoundTextView);

        wordNotFoundTextView.setVisibility(View.GONE);

        bottomSheet = new WordListBottomSheetDialog();
        WordDetailsActivity.bottomSheet = bottomSheet;

        List<Word> wordList = new ArrayList<>();
//        wordList.add(new Word("Category Name 1", "Category Name 1", "Category Name 1", "Salom1", "Привет", "Hello"));
//        wordList.add(new Word("Category Name 1", "Category Name 1", "Category Name 1", "Salom2", "Привет", "Hello"));
//        wordList.add(new Word("Category Name 1", "Category Name 1", "Category Name 1", "Salom3", "Привет", "Hello"));
//        wordList.add(new Word("Category Name 1", "Category Name 1", "Category Name 1", "Salom4", "Привет", "Hello"));
//        wordList.add(new Word("Category Name 1", "Category Name 1", "Category Name 1", "Salom5", "Привет", "Hello"));
//        wordList.add(new Word("Category Name 1", "Category Name 1", "Category Name 1", "Salom6", "Привет", "Hello"));

        adapter = new WordListAdapter(new ArrayList<Word>(), this, true, wordNotFoundTextView);
        searchActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchActivityRecyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        viewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                adapter.setWordList(words);
            }
        });

        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private Transition enterTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(200);

        return bounds;
    }

    private Transition returnTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setInterpolator(new DecelerateInterpolator());
        bounds.setDuration(200);

        return bounds;
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

    @Override
    public void onBottomSheetButtonClick(ImageView wordImage, String wordUz, String wordRu, String wordEn, TextView wordRuTextView, TextView wordEnTextView) {
        Intent intent = new Intent(SearchActivity.this, WordDetailsActivity.class);
        intent.putExtra("wordUz", wordUz);
        intent.putExtra("wordRu", wordRu);
        intent.putExtra("wordEn", wordEn);

        if (sdk >= Build.VERSION_CODES.LOLLIPOP) {
            Pair wordImageAnim = Pair.create(wordImage, ViewCompat.getTransitionName(wordImage));
            Pair wordRuAnim = Pair.create(wordRuTextView, ViewCompat.getTransitionName(wordRuTextView));
            Pair wordEnAnim = Pair.create(wordEnTextView, ViewCompat.getTransitionName(wordEnTextView));
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this, wordImageAnim, wordRuAnim, wordEnAnim);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
}