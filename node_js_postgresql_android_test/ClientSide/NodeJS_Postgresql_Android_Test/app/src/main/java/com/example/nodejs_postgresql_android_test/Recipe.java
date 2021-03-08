package com.example.nodejs_postgresql_android_test;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private String title;
    private String description;
    private List<String> ingredients = new ArrayList<String>();
    private List<String> steps = new ArrayList<String>();

    public Recipe(String title, String description){
        this.title = title;
        this.description = description;
    }

    protected Recipe(Parcel in) {
        title = in.readString();
        description = in.readString();
        in.readStringList(ingredients);
        in.readStringList(steps);
    }

    public boolean isValid(){
        return true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeStringList(ingredients);
        dest.writeStringList(steps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public void addIngredient(String ingredient){
        ingredients.add(ingredient);
    }

    public void addStep(String step){
        steps.add(step);
    }

    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public String getIngredient(int index) { return ingredients.get(index); }
    public String getStep(int index) { return steps.get(index); }
}
