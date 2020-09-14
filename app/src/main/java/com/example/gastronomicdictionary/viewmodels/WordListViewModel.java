package com.example.gastronomicdictionary.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gastronomicdictionary.data.WordRepositoryImpl;
import com.example.gastronomicdictionary.data.models.Word;

import java.util.List;

public class WordListViewModel extends AndroidViewModel {
    private WordRepositoryImpl repository;
    private LiveData<List<Word>> wordList = new MutableLiveData<>();

    public WordListViewModel(@NonNull Application application) {
        super(application);
        repository = new WordRepositoryImpl(application);
    }

    public LiveData<List<Word>> getWordsByCategoryName(String categoryName) {
        return repository.getWordsByCategoryName(categoryName);
    }
}
