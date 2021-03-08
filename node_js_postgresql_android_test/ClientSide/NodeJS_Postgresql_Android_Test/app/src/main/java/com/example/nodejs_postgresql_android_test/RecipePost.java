package com.example.nodejs_postgresql_android_test;

public class RecipePost {
    private Integer id;
    private String title;
    private String description;

    public RecipePost(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
