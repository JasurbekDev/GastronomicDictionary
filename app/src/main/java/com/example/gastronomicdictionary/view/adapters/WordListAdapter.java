package com.example.gastronomicdictionary.view.adapters;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gastronomicdictionary.R;
import com.example.gastronomicdictionary.data.models.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> implements Filterable {
    private List<Word> wordList;
    private List<Word> wordListAll = new ArrayList<>();
    private WordListAdapterListener wordListAdapterListener;
    private boolean isInSearchActivity;
    private TextView wordNotFoundTextView;

    public WordListAdapter(List<Word> wordList, WordListAdapterListener wordListAdapterListener, boolean isInSearchActivity) {
        this.wordList = wordList;
        this.wordListAdapterListener = wordListAdapterListener;
        this.isInSearchActivity = isInSearchActivity;
    }

    public WordListAdapter(List<Word> wordList, WordListAdapterListener wordListAdapterListener, boolean isInSearchActivity, TextView wordNotFoundTextView) {
        this.wordList = wordList;
        this.wordListAdapterListener = wordListAdapterListener;
        this.isInSearchActivity = isInSearchActivity;
        this.wordNotFoundTextView = wordNotFoundTextView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false);
        return new ViewHolder(view, wordListAdapterListener, isInSearchActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(wordList.get(position));
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
        this.wordListAll = new ArrayList<>(wordList);
        notifyDataSetChanged();
    }

    public List<Word> getWordList() {
        return wordList;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Word> filterList = new ArrayList<>();
            if(charSequence.toString().isEmpty()) {
                filterList.addAll(wordListAll);
            } else {
                for (Word word : wordListAll) {
                    int queryLength = charSequence.toString().length();
                    if (queryLength <= word.getUz().length()) {
                        String wordSubstring = word.getUz().substring(0, queryLength);
                        if (wordSubstring.toLowerCase().equals(charSequence.toString().toLowerCase())) {
                            filterList.add(word);
                        }
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            wordList.clear();
            wordList.addAll((Collection<? extends Word>) filterResults.values);
            if (wordList.size() == 0) {
                wordNotFoundTextView.setVisibility(View.VISIBLE);
            } else {
                wordNotFoundTextView.setVisibility(View.GONE);
            }
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView wordTextView;
        private WordListAdapterListener wordListAdapterListener;
        private boolean isInSearchActivity;

        public ViewHolder(@NonNull View itemView, WordListAdapterListener wordListAdapterListener, boolean isInSearchActivity) {
            super(itemView);
            this.wordListAdapterListener = wordListAdapterListener;
            this.isInSearchActivity = isInSearchActivity;

            wordTextView = itemView.findViewById(R.id.wordTextView);
        }

        public void bind(final Word word) {
            if (getAdapterPosition() == 0) {
                int firstWordMarginTop;
                if (isInSearchActivity) {
                    firstWordMarginTop = dpToPixel(80);
                } else {
                    firstWordMarginTop = dpToPixel(120);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, firstWordMarginTop, 0, 0);
                itemView.setLayoutParams(params);
            }
            wordTextView.setText(word.getUz());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wordListAdapterListener.onWordSelected(word.getUz(), word.getRu(), word.getEn());
                }
            });
        }

        private int dpToPixel(int dpValue) {
            return Math.round(new TypedValue().applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, itemView.getResources().getDisplayMetrics()));
        }
    }

    public interface WordListAdapterListener {
        void onWordSelected(String wordUz, String wordRu, String wordEn);
    }
}
