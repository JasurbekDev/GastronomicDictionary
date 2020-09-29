package com.example.gastronomicdictionary.data;

import androidx.lifecycle.LiveData;

import com.example.gastronomicdictionary.data.models.Word;

import java.util.List;

public interface WordRepository {
    void insert(Word word);

    LiveData<List<Word>> getAllWords();

    LiveData<Word> getWordById(int id);

    LiveData<List<Word>> getWordsByCategoryName(String categoryName);
}
