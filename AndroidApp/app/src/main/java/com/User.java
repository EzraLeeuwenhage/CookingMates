package com;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class User implements Parcelable {
    @SerializedName("userid")
    private int userId;
    public String username;
    private String fullname;
    private String email;
    private String password;
    @SerializedName("dateofbirth")
    private Date dateOfBirth;
    private String personal_image;
    private boolean adult;
    public transient List<Recipe> recipes;
    private transient int PASSWORD_LENGTH =8;

    public User(String name, String fullname, String email, String password, Date date, String image) {
        set_user_name(name);
        this.fullname = fullname;
        this.email = email;
        set_password(password);
        set_personal_image(image);
        set_b_date(date);
        recipes = new ArrayList<Recipe>();
    }//end of User function

    protected User(Parcel in) {
        userId = in.readInt();
        username = in.readString();
        fullname = in.readString();
        email = in.readString();
        password = in.readString();
        dateOfBirth = new Date(in.readLong());
        personal_image = in.readString();
        adult = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //maybe check the other names to be sure that name is unique
    private void set_user_name(String name){
        this.username = name;
    }

    //set the password and check it
    private void set_password(String password) throws IllegalArgumentException{
        //not a good password
        if(!is_Valid_Password(password))
        {
            new IllegalArgumentException("This Password is not valid, please try again");
        }
        this.password = password;
    }//end set_password

    private void set_b_date( Date b_date) throws NullPointerException, IllegalArgumentException{
        if(b_date == null){
            new NullPointerException("No date added, please enter a date");
        }// end if
        if(b_date.getYear() < 1900){
            new IllegalArgumentException("the inserted date is not accurate");
        }//end if
        this.dateOfBirth = b_date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(b_date);
        //check whether the user is adult or child
        if ( (Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR)) > 17 ) {
            adult = true;
        }//end if
        else{
            adult = false;
        }//end else
    }//end set_b_date

    private void set_personal_image (String image) throws NullPointerException, IllegalArgumentException {
        if (image == null) {
            new NullPointerException("no image was uploaded to choose");
        }//end if
        /* Do images need to be jpg?
        if (!image.sort.equals("jpg")) {
            new IllegalArgumentException("The uploaded image is not jpg");
        }//end if

         */

        this.personal_image = image;
    }//end set_personal_image

    //check whether the password is valid
    public boolean is_Valid_Password(String password) {

        if (password.length() < PASSWORD_LENGTH)
            return false;

        int charCount = 0;
        int numCount = 0;
        for (int i = 0; i < password.length(); i++) {

            char ch = password.charAt(i);

            if (is_Numeric(ch)) numCount++;
            else if (is_Letter(ch)) charCount++;
            else return false;
        }


        return (charCount >= 2 && numCount >= 2);
    }//end is_Valid_Password

    public static boolean is_Letter(char ch) {
        ch = Character.toUpperCase(ch);
        return (ch >= 'A' && ch <= 'Z');
    }


    public static boolean is_Numeric(char ch) {

        return (ch >= '0' && ch <= '9');
    }

    public String getName() {
        return username;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public Date getDateOfBirth() { return dateOfBirth; }

    //Returns whether this user is adult or not
    public boolean isAdult(){
        if(dateOfBirth == null){
            new NullPointerException("No date added, please enter a date");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateOfBirth);

        if(cal.get(Calendar.YEAR) < 1900){
            new IllegalArgumentException("the inserted date is not accurate");
        }

        //check whether the user is adult or child
        return Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR) > 17;
    }

    public int getUserId() { return userId; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);;
        dest.writeString(this.username);
        dest.writeString(this.fullname);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeLong(this.dateOfBirth.getTime());
        dest.writeString(this.personal_image);
        dest.writeByte((byte) (this.adult ? 1 : 0));
    }
}//end of class
