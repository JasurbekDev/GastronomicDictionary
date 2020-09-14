package com.example.gastronomicdictionary.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "word_table")
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("category_uz")
    private String categoryUz;
    @SerializedName("category_ru")
    private String categoryRu;
    @SerializedName("category_en")
    private String categoryEn;
    @SerializedName("uz")
    private String uz;
    @SerializedName("ru")
    private String ru;
    @SerializedName("en")
    private String en;

    public Word(String categoryUz, String categoryRu, String categoryEn, String uz, String ru, String en) {
        this.categoryUz = categoryUz;
        this.categoryRu = categoryRu;
        this.categoryEn = categoryEn;
        this.uz = uz;
        this.ru = ru;
        this.en = en;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryUz() {
        return categoryUz;
    }

    public void setCategoryUz(String categoryUz) {
        this.categoryUz = categoryUz;
    }

    public String getCategoryRu() {
        return categoryRu;
    }

    public void setCategoryRu(String categoryRu) {
        this.categoryRu = categoryRu;
    }

    public String getCategoryEn() {
        return categoryEn;
    }

    public void setCategoryEn(String categoryEn) {
        this.categoryEn = categoryEn;
    }

    public String getUz() {
        return uz;
    }

    public void setUz(String uz) {
        this.uz = uz;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
