package com.example.gastronomicdictionary.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gastronomicdictionary.data.WordRepositoryImpl;
import com.example.gastronomicdictionary.data.models.Word;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private WordRepositoryImpl repository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new WordRepositoryImpl(application);
    }

    public LiveData<List<Word>> getAllWords() {
        return repository.getAllWords();
    }
}
