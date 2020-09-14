package com.example.gastronomicdictionary.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gastronomicdictionary.data.models.Word;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    void insert(Word word);

    @Query("SELECT * FROM word_table")
    LiveData<List<Word>> getAllWords();

    @Query("SELECT * FROM word_table WHERE id = :id")
    LiveData<Word> getWordById(int id);

    @Query("SELECT * FROM word_table WHERE categoryUz = :categoryName")
    LiveData<List<Word>> getWordsByCategoryName(String categoryName);

}
