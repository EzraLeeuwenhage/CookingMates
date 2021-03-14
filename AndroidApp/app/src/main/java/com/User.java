//package com;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//public class User {
//
//    public String user_name;
//    private String password;
//    private Date b_date;
//    private Image personal_image;
//    private boolean adult;
//    public List<Recipe> recipes;
//    private int PASSWORD_LENGTH =8;
//
//    public User(String name, String password, Date date, Image image) {
//        set_user_name(name);
//        set_password(password);
//        set_personal_image(image);
//        set_b_date(date);
//        recipes = new ArrayList<Recipe>();
//    }//end of User function
//
//    //maybe check the other names to be sure that name is unique
//    private void set_user_name(String name){
//        this.user_name = name;
//    }
//
//    //set the password and check it
//    private void set_password(String password) throws IllegalArgumentException{
//        //not a good password
//        if(!is_Valid_Password(password))
//        {
//            new IllegalArgumentException("This Password is not valid, please try again");
//        }
//        this.password = password;
//    }//end set_password
//
//    private void set_b_date( Date b_date) throws NullPointerException, IllegalArgumentException{
//        if(b_date == null){
//            new NullPointerException("No date added, please enter a date");
//        }// end if
//        if(b_date.getYear() < 1900){
//            new IllegalArgumentException("the inserted date is not accurate");
//        }//end if
//        this.b_date = b_date;
//        //check whether the user is adult or child
//        if ( (Calendar.getInstance().get(Calendar.YEAR) - this.b_date.getYear()) > 17 ) {
//            adult = true;
//        }//end if
//        else{
//            adult = false;
//        }//end else
//    }//end set_b_date
//
//    private void set_personal_image (Image image) throws NullPointerException, IllegalArgumentException {
//        if (image == null) {
//            new NullPointerException("no image was uploaded to choose");
//        }//end if
//        if (!image.sort.equals("jpg")) {
//            new IllegalArgumentException("The uploaded image is not jpg");
//        }//end if
//
//        this.personal_image = image;
//    }//end set_personal_image
//
//    //check whether the password is valid
//    public boolean is_Valid_Password(String password) {
//
//        if (password.length() < PASSWORD_LENGTH)
//            return false;
//
//        int charCount = 0;
//        int numCount = 0;
//        for (int i = 0; i < password.length(); i++) {
//
//            char ch = password.charAt(i);
//
//            if (is_Numeric(ch)) numCount++;
//            else if (is_Letter(ch)) charCount++;
//            else return false;
//        }
//
//
//        return (charCount >= 2 && numCount >= 2);
//    }//end is_Valid_Password
//
//    public static boolean is_Letter(char ch) {
//        ch = Character.toUpperCase(ch);
//        return (ch >= 'A' && ch <= 'Z');
//    }
//
//
//    public static boolean is_Numeric(char ch) {
//
//        return (ch >= '0' && ch <= '9');
//    }
//
//
//}//end of class
