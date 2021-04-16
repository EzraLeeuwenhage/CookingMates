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

    // constructor to create a Recipe object
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

    // Retrieves the recipe's id
    public int getRecipeId() {
        return recipeId;
    }

    // Retrieves the recipe's name
    public String getName() {
        return name;
    }

    // Retrieves the recipe's description
    public String getDescription() {
        return description;
    }

    // Retrieves the recipe's list of ingredients
    public List<String> getIngredients() {
        return ingredients;
    }

    // Retrieves the related quantities of the recipe's ingredients
    public List<String> getQuantities() {
        return quantities;
    }

    // Retrieves the number of people for which the recipe is meant
    public int getNumberpeople() {
        return numberpeople;
    }

    // Retrieves whether the recipe is meant for adults only
    public boolean isForAdult() {
        return adult;
    }

    // Retrieves the filename of the image related to the recipe
    public String getFilename() {
        return filename;
    }

    // Enables the changing of the image's filename related to the recipe
    public void setFilename(String filename) {
        this.filename = filename;
    }

    // Retrieves whether the recipe has one or more ratings
    public boolean hasRating() {
        return ratings.size() > 0;
    }

    // Enables adding ratings to the recipe
    public void addRating(int rating) {
        this.ratings.add(rating);
    }

    // Retrieves the ratings related to the recipe
    public float getRating() {
        float total = 0;
        for (int i : this.ratings) {
            total += i;
        }

        return total / this.ratings.size();
    }

    // Retrieves the reviews related to the recipe
    public List<String> getReviews() {
        return reviews;
    }

    // Enables adding reviews to the recipe
    public void addReview(String review) {
        this.reviews.add(review);
    }

    // Retrieves the tags related to the recipe
    public List<String> getTags() { return tags; }

    // standard function that needs to be overwritten when implementing Parcelable class
    // (see implemented method Parcelable.java)
    @Override
    public int describeContents() {
        return 0;
    }

    // Flattens the object into a Parcel format (see implemented method Parcelable.java)
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

    // constructs Recipe object from Parcel object
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
