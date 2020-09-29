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
        if (instance == null) {
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

            Type type = new TypeToken<List<Word>>() {
            }.getType();
            List<Word> words = new Gson().fromJson(jsonText, type);

            for (Word word : words) {
                wordDao.insert(word);
            }
            return null;
        }
    }
}
