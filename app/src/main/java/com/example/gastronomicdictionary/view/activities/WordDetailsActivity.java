package com.example.gastronomicdictionary.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gastronomicdictionary.R;
import com.example.gastronomicdictionary.view.WordListBottomSheetDialog;

public class WordDetailsActivity extends AppCompatActivity {
    private TextView wordUzTextView;
    private TextView wordRuTextView;
    private TextView wordEnTextView;
    private ImageView wordDetailsImage;
    public static WordListBottomSheetDialog bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_details);

        wordUzTextView = findViewById(R.id.detailsWordUz);
        wordRuTextView = findViewById(R.id.detailsWordRu);
        wordEnTextView = findViewById(R.id.detailsWordEn);
        wordDetailsImage = findViewById(R.id.wordDetailsImage);

        Toolbar toolbar = findViewById(R.id.detailsToolBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String wordUz = intent.getStringExtra("wordUz");
        String wordRu = intent.getStringExtra("wordRu");
        String wordEn = intent.getStringExtra("wordEn");
        String wordImageId = intent.getStringExtra("wordImageId");

        wordUzTextView.setText(wordUz);
        wordRuTextView.setText(wordRu);
        wordEnTextView.setText(wordEn);
        wordDetailsImage.setImageResource(Integer.parseInt(wordImageId));

        toolbar.setTitle(wordUz);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}