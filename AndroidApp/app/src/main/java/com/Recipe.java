package com;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    @SerializedName("recipeid")
    private int recipeId;
    @SerializedName("creatorid")
    private int creatorId;
    private String name;
    private String description;
    private List<String> ingredients = new ArrayList<>();
    @SerializedName("quantity")
    private List<String> quantities = new ArrayList<>();
    private int numberpeople;
    private boolean adult;
    @SerializedName("media")
    private String filename;
    private List<Integer> ratings = new ArrayList<>();
    private List<String> reviews = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    public Recipe() { }

    public Recipe(int creatorId, String name, String description, List<String> ingredients,
                  List<String> quantities, int numberpeople, boolean adult, List<String> tags) {
        this.creatorId = creatorId;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.quantities = quantities;
        this.numberpeople = numberpeople;
        this.adult = adult;
        this.tags = tags;
    }

    //Creator used to transfer Recipe object to and from Parcel
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

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getQuantities() {
        return quantities;
    }

    public int getNumberpeople() {
        return numberpeople;
    }

    public boolean isForAdult() {
        return adult;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean hasRating() {
        return ratings.size() > 0;
    }

    public void addRating(int rating) {
        this.ratings.add(rating);
    }

    public float getRating() {
        float total = 0;
        for (int i : this.ratings) {
            total += i;
        }

        return total / this.ratings.size();
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void addReview(String review) {
        this.reviews.add(review);
    }

    public List<String> getTags() { return tags; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.recipeId);
        dest.writeInt(this.creatorId);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeStringList(this.ingredients);
        dest.writeStringList(this.quantities);
        dest.writeInt(this.numberpeople);
        dest.writeByte((byte) (this.adult ? 1 : 0));
        dest.writeString(this.filename);
        dest.writeList(this.ratings);
        dest.writeStringList(this.reviews);
        dest.writeStringList(this.tags);
    }

    public Recipe(Parcel pc) {
        this.recipeId = pc.readInt();
        this.creatorId = pc.readInt();
        this.name = pc.readString();
        this.description = pc.readString();
        pc.readStringList(this.ingredients);
        pc.readStringList(this.quantities);
        this.numberpeople = pc.readInt();
        this.adult = pc.readByte() != 0;
        this.filename = pc.readString();
        pc.readList(this.ratings, null);
        pc.readStringList(this.reviews);
        pc.readStringList(this.tags);
    }

}// /end of the class
