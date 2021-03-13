package com;

import java.util.List;

public class Recipe {
    public String title;
    public List<String> ingredients;
    private boolean for_kids;
    public Image image;
    int TITLE_LENGTH = 3;

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

        this.title = title;
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
                this.for_kids = false;
                return this.for_kids;
            }
        }//end for
        this.for_kids = true;
        return this.for_kids;
    }//end is_for_kids

}//end of the class
