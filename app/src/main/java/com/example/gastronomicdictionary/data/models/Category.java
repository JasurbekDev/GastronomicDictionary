package com.example.gastronomicdictionary.data.models;

public class Category {
    private String categoryName;
    private int categoryImageResourceId;

    public Category(String categoryName, int categoryImageResourceId) {
        this.categoryName = categoryName;
        this.categoryImageResourceId = categoryImageResourceId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryImageResourceId() {
        return categoryImageResourceId;
    }

    public void setCategoryImageResourceId(int categoryImageResourceId) {
        this.categoryImageResourceId = categoryImageResourceId;
    }
}
