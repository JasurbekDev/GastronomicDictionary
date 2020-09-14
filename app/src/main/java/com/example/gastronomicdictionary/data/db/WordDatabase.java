package com.example.gastronomicdictionary.data.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gastronomicdictionary.data.models.Word;

@Database(entities = {Word.class}, version = 1)
public abstract class WordDatabase extends RoomDatabase {
    private static WordDatabase instance;
    public abstract WordDao wordDao();


    public static synchronized WordDatabase getInstance(Context context) {
        if(instance == null) {
           instance = Room.databaseBuilder(context.getApplicationContext(), WordDatabase.class, "word_database")
                   .fallbackToDestructiveMigration()
                   .addCallback(roomCallback)
                   .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao wordDao;

        private PopulateDbAsyncTask(WordDatabase db) {
            wordDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.insert(new Word("Category Name 1", "Category Name 1", "Category Name 1", "Salom", "Привет", "Hello"));
            wordDao.insert(new Word("Category Name 1", "Category Name 1", "Category Name 1", "Do'st", "Друг", "Friend"));
            wordDao.insert(new Word("Category Name 2", "Category Name 2", "Category Name 2", "Taom", "Блюдо", "Food"));
            wordDao.insert(new Word("Category Name 2", "Category Name 2", "Category Name 2", "Salat", "Салат", "Salad"));
            wordDao.insert(new Word("Category Name 3", "Category Name 3", "Category Name 3", "Hayrli kun", "Добрый день", "Good afternoon"));
            wordDao.insert(new Word("Category Name 3", "Category Name 3", "Category Name 3", "Ko'rganimdan xursandman!", "Рад встрече!", "Nice to meet you!"));
            return null;
        }
    }
}
