package com.example.gastronomicdictionary.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
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

        adapter = new WordListAdapter(this, new ArrayList<Word>(), this, true, wordNotFoundTextView);
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

        String formattedName = getFormattedName(wordEn);
        String wordNameEn = Integer.toString(getResources().getIdentifier(formattedName, "drawable", getPackageName())).toLowerCase();

        bundle.putString("wordImageId", wordNameEn);
        bottomSheet.setArguments(bundle);
        bottomSheet.show(getSupportFragmentManager(), "wordListBottomSheet");
    }

    public static String getFormattedName(String word) {
        String formattedName = word.toLowerCase();
        if (formattedName.contains(" ")) {
            String[] s = formattedName.split(" ");
            formattedName = "";
            for (int i = 0; i < s.length; i++) {
                String suffix = (i < s.length - 1) ? "_" : "";
                formattedName += s[i].toLowerCase() + suffix;
            }
        }
        if (formattedName.contains("-")) {
            String[] s = formattedName.split("-");
            formattedName = "";
            for (int i = 0; i < s.length; i++) {
                String suffix = (i < s.length - 1) ? "_" : "";
                formattedName += s[i].toLowerCase() + suffix;
            }
        }
        if (formattedName.contains("’")) {
            String[] s = formattedName.split("’");
            formattedName = "";
            for (int i = 0; i < s.length; i++) {
//                String suffix = (i < s.length - 1) ? "_" : "";
                formattedName += s[i].toLowerCase();
            }
        }
        if (formattedName.contains("(")) {
            formattedName = formattedName.substring(0, formattedName.indexOf("(")).trim();
            if (formattedName.substring(formattedName.length() - 1).equals("_")) {
                formattedName = formattedName.substring(0, formattedName.length() - 1);
            }
        }
        if (formattedName.contains("–")) {
            formattedName = formattedName.split("–")[0];
            if (formattedName.substring(formattedName.length() - 1).equals("_")) {
                formattedName = formattedName.substring(0, formattedName.length() - 1);
            }
        }
        return formattedName;
    }

    @Override
    public void onBottomSheetButtonClick(ImageView wordImage, int wordImageId, String wordUz, String wordRu, String wordEn, TextView wordRuTextView, TextView wordEnTextView) {
        Intent intent = new Intent(SearchActivity.this, WordDetailsActivity.class);
        intent.putExtra("wordUz", wordUz);
        intent.putExtra("wordRu", wordRu);
        intent.putExtra("wordEn", wordEn);
        intent.putExtra("wordImageId", Integer.toString(wordImageId));
        startActivity(intent);
    }
}