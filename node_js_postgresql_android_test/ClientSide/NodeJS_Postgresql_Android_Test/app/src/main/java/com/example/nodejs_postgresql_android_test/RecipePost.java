package com.example.nodejs_postgresql_android_test;

public class RecipePost {
    private Integer id;
    private String title;
    private String description;
    private String filename;
    private String filepath;

    public RecipePost(String title, String description, String filename, String filepath) {
        this.title = title;
        this.description = description;
        this.filename = filename;
        this.filepath = filepath;
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

    public String getFilename() { return filename; }

    public String getFilepath() { return filepath; }
}
