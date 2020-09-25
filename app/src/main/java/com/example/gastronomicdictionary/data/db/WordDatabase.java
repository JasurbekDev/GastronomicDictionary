package com.example.gastronomicdictionary.data.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gastronomicdictionary.data.models.Word;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

@Database(entities = {Word.class}, version = 1)
public abstract class WordDatabase extends RoomDatabase {
    private static WordDatabase instance;
    public abstract WordDao wordDao();
    static Context mContext;


    public static synchronized WordDatabase getInstance(Context context) {
        WordDatabase.mContext = context;
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
        private Context mContext;

        private PopulateDbAsyncTask(WordDatabase db) {
            wordDao = db.wordDao();
            this.mContext = db.mContext;
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            File file = new File("C:\\Users\\555\\Desktop\\GastronomicDictionary\\app\\src\\main\\res\\assets\\example_file.json");
//            File file = new File("/app/src/main/res/assets/example_file.json");
//            File file = new File("C:\\Users\\555\\Desktop\\GastronomicDictionary\\app\\src\\main\\res\\raw\\example_file.json");
//            File file = new File("file:///android_asset/example_file.json");
//            AssetManager assetManager = getAssets();
            String jsonText = "";
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("example_file.json")));
                String st;
                while ((st = reader.readLine()) != null) {
                    jsonText += st;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Type type = new TypeToken<List<Word>>(){}.getType();
            List<Word> words = new Gson().fromJson(jsonText, type);

            for (Word word : words) {
                wordDao.insert(word);
            }

//            wordDao.insert(new Word("Category Name 1", "Category Name 1", "Category Name 1", "Salom", "Привет", "Hello"));
//            wordDao.insert(new Word("Category Name 1", "Category Name 1", "Category Name 1", "Do'st", "Друг", "Friend"));
//            wordDao.insert(new Word("Category Name 2", "Category Name 2", "Category Name 2", "Taom", "Блюдо", "Food"));
//            wordDao.insert(new Word("Category Name 2", "Category Name 2", "Category Name 2", "Salat", "Салат", "Salad"));
//            wordDao.insert(new Word("Category Name 3", "Category Name 3", "Category Name 3", "Hayrli kun", "Добрый день", "Good afternoon"));
//            wordDao.insert(new Word("Category Name 3", "Category Name 3", "Category Name 3", "Ko'rganimdan xursandman!", "Рад встрече!", "Nice to meet you!"));
            return null;
        }
    }
}
