package com;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {
    @SerializedName("recipeid")
    private int recipeId;
    @SerializedName("creatorid")
    private int creatorId;
    private String name;
    private String description;
    private List<String> ingredients; //Could change to List<Integer>
    private List<String> quantities;
    private int numberpeople;
    private boolean adult;
    @SerializedName("media")
    private String filename;

    private transient int TITLE_LENGTH = 3;

    public Recipe(int creatorId, String name, String description, List<String> ingredients, List<String> quantities, int numberpeople, boolean adult) {
        this.creatorId = creatorId;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.quantities = quantities;
        this.numberpeople = numberpeople;
        this.adult = adult;
    }

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

    public boolean isAdult() {
        return adult;
    }

    public String getFilename() {
        return filename;
    }
}//end of the class
