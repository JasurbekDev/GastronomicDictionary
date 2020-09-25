package com.example.gastronomicdictionary.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gastronomicdictionary.R;
import com.example.gastronomicdictionary.data.db.WordDao;
import com.example.gastronomicdictionary.data.db.WordDatabase;
import com.example.gastronomicdictionary.data.models.Category;
import com.example.gastronomicdictionary.data.models.Word;

import java.util.ArrayList;
import java.util.List;

public class WordRepositoryImpl implements WordRepository {
    private WordDao wordDao;
    private LiveData<List<Word>> allWords;
    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();

    public WordRepositoryImpl(Application application) {
        wordDao = WordDatabase.getInstance(application).wordDao();
        List<Category> names = new ArrayList<>();
        names.add(new Category("Category Name 1", R.drawable.ic_launcher_background));
        names.add(new Category("Category Name 2", R.drawable.ic_launcher_background));
        names.add(new Category("Category Name 3", R.drawable.ic_launcher_background));
        names.add(new Category("Category Name 4", R.drawable.ic_launcher_background));
        names.add(new Category("Category Name 5", R.drawable.ic_launcher_background));
        names.add(new Category("Category Name 6", R.drawable.ic_launcher_background));
        names.add(new Category("Category Name 7", R.drawable.ic_launcher_background));
        names.add(new Category("Category Name 8", R.drawable.ic_launcher_background));
        names.add(new Category("Category Name 9", R.drawable.ic_launcher_background));
        names.add(new Category("Category Name 10", R.drawable.ic_launcher_background));

        categories.postValue(names);
    }

    @Override
    public void insert(Word word) {
        new InsertAsyncTask(wordDao).execute(word);
    }

    @Override
    public LiveData<List<Word>> getAllWords() {
        return wordDao.getAllWords();
    }

    @Override
    public LiveData<Word> getWordById(int id) {
        return wordDao.getWordById(id);
    }

    @Override
    public LiveData<List<Word>> getWordsByCategoryName(String categoryName) {
        return wordDao.getWordsByCategoryName(categoryName);
    }

    public LiveData<List<Category>> getCategoryNames() {
        return categories;
    }

    private static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insert(words[0]);
            return null;
        }
    }
}
