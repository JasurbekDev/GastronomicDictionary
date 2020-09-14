package com.example.gastronomicdictionary.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.gastronomicdictionary.R;
import com.example.gastronomicdictionary.view.WordListBottomSheetDialog;

public class WordDetailsActivity extends AppCompatActivity {
    private TextView wordRuTextView;
    private TextView wordEnTextView;
    public static WordListBottomSheetDialog bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_details);

        wordRuTextView = findViewById(R.id.detailsWordRu);
        wordEnTextView = findViewById(R.id.detailsWordEn);

        Toolbar toolbar = findViewById(R.id.detailsToolBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String wordUz = intent.getStringExtra("wordUz");
        String wordRu = intent.getStringExtra("wordRu");
        String wordEn = intent.getStringExtra("wordEn");

        wordRuTextView.setText(wordRu);
        wordEnTextView.setText(wordEn);

        toolbar.setTitle(wordUz);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        bottomSheet.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
//                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}