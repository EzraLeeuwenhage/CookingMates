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

    private transient int TITLE_LENGTH = 3;

    public Recipe(){};

    public Recipe(int creatorId, String name, String description, List<String> ingredients, List<String> quantities, int numberpeople, boolean adult) {
        this.creatorId = creatorId;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.quantities = quantities;
        this.numberpeople = numberpeople;
        this.adult = adult;
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

    //set the title of the recipe
    public void set_title (String title) throws NullPointerException, IllegalArgumentException {
        if( title == null){
            NullPointerException exception=new NullPointerException("no title entered");
           throw exception;
        }//end if

        if( !is_word(title)){
            IllegalArgumentException exception = new IllegalArgumentException("the inserted title is not a word");
            throw exception;
        }//end if

        this.name = title;
    }//end set_title

    public void set_ingredients ( List<String> ingredients1)throws NullPointerException, IllegalArgumentException{
        if( ingredients1 == null){
            throw (new NullPointerException("this list is empty"));
        }//end if

        for (int i=0; i< ingredients1.size(); i++){
            if(!( is_word( ingredients1.get(i) ) ) ){
                throw( new IllegalArgumentException("the element "+ i + "is not a word"));
            }//end if
        }//end for

        for( int i=0; i<ingredients1.size(); i++){
            this.ingredients.add( ingredients1.get(i));
        }//end for
    }//end set_ingredients

    private boolean is_word(String title) {
        if( title.length() < TITLE_LENGTH){
            return false;
        }
        char ch;
        for( int i=0; i<title.length(); i++){
            ch = Character.toUpperCase(title.charAt(i));
            if (!( ch> 'A' && ch < 'Z')){
                return false;
            }//end if
        }//end for
        return true;
    }//end is_word

    //check whether the recipe contains alcoholic ingredients or suitable for kids
    private boolean is_for_kids(){
        for(int i=0; i < this.ingredients.size(); i++){
            if (ingredients.get(i).equals("wine") || ingredients.get(i).equals("alcohol")) {
                this.adult = false;
                return this.adult;
            }
        }//end for
        this.adult = true;
        return this.adult;
    }//end is_for_kids

    public int getRecipeId() {
        return recipeId;
    }

    public int getCreatorId() {
        return creatorId;
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

    public void setFilename(String filename) { this.filename = filename; }

    public List<Integer> getRatings() { return ratings; }

    public boolean hasRating(){
        return ratings.size() > 0;
    }

    public void addRating(int rating) {
        this.ratings.add(rating);
    }

    public float getRating(){
        float total = 0;
        for(int i : this.ratings){
            total += i;
        }

        return total / this.ratings.size();
    }

    public List<String> getReviews() { return reviews; }

    public void addReview(String review) {
        this.reviews.add(review);
    }

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
    }

    public Recipe(Parcel pc){
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
    }
}//end of the class
