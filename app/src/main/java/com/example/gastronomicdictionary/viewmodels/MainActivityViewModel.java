package com.example.gastronomicdictionary.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gastronomicdictionary.data.WordRepositoryImpl;
import com.example.gastronomicdictionary.data.models.Category;
import com.example.gastronomicdictionary.data.models.Word;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private WordRepositoryImpl wordRepository;
    private LiveData<List<Category>> categories;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepositoryImpl(application);
        categories = wordRepository.getCategoryNames();
    }

    public LiveData<List<Word>> getAllWords() {
        return wordRepository.getAllWords();
    }
}
