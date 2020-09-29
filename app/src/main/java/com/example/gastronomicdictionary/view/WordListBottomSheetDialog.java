package com.example.gastronomicdictionary.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gastronomicdictionary.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class WordListBottomSheetDialog extends BottomSheetDialogFragment {
    private TextView wordUzTextView;
    private TextView wordRuTextView;
    private TextView wordEnTextView;
    private ImageView wordImage;
    private Button bottomSheetButton;
    private BottomSheetListener bottomSheetListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        wordUzTextView = v.findViewById(R.id.wordUzTextView);
        wordRuTextView = v.findViewById(R.id.wordRuTextView);
        wordEnTextView = v.findViewById(R.id.wordEnTextView);
        wordImage = v.findViewById(R.id.wordImage);
        bottomSheetButton = v.findViewById(R.id.bottomSheetButton);

        Bundle bundle = getArguments();
        final String wordUz = bundle.getString("wordUz");
        final String wordRu = bundle.getString("wordRu");
        final String wordEn = bundle.getString("wordEn");
        final int wordImageId = Integer.parseInt(bundle.getString("wordImageId"));

        wordUzTextView.setText(wordUz);
        wordRuTextView.setText(wordRu);
        wordEnTextView.setText(wordEn);

        wordImage.setImageResource(wordImageId);

        bottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetListener.onBottomSheetButtonClick(wordImage, wordImageId, wordUz, wordRu, wordEn, wordRuTextView, wordEnTextView);
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            bottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }

    public interface BottomSheetListener {
        void onBottomSheetButtonClick(ImageView wordImage, int wordImageId, String wordUz, String wordRu, String wordEn, TextView wordRuTextView, TextView wordEnTextView);
    }
}
