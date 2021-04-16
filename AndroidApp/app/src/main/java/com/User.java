package com;

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
    @SerializedName("profilepicture")
    private String profilePicture;
    private boolean adult;
    public transient List<Recipe> recipes;

    public User(String name, String fullname, String email,
                String password, Date date, String image) {
        this.username = name;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        setProfilePicture(image);
        setBirthDate(date);
        recipes = new ArrayList<>();
    }//end of User function

    protected User(Parcel in) {
        userId = in.readInt();
        username = in.readString();
        fullname = in.readString();
        email = in.readString();
        password = in.readString();
        dateOfBirth = new Date(in.readLong());
        profilePicture = in.readString();
        adult = in.readByte() != 0;
    }

    //Creator used to transfer Recipe object to and from Parcel
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

    //Sets dateOfBirth and defines adult
    private void setBirthDate(Date b_date) {
        this.dateOfBirth = b_date;

        Calendar cal = Calendar.getInstance();
        cal.setTime(b_date);
        //check whether the user is 18+
        if ((Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR)) > 17) {
            adult = true;
        }//end if
        else{
            adult = false;
        }//end else
    }//end set_b_date

    //Sets profilePicture
    public void setProfilePicture(String image) {
        this.profilePicture = image;
    }//end setProfilePicture

    //Gets username
    public String getName() {
        return username;
    }

    //Gets email
    public String getEmail() { return email; }

    //Gets password
    public String getPassword() { return password; }

    //Gets dateOfBirth
    public Date getDateOfBirth() { return dateOfBirth; }

    //Returns whether this user is adult or not
    public boolean isAdult(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateOfBirth);

        //check whether the user is adult or child
        return Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR) > 17;
    }

    //Gets userId
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
        dest.writeString(this.profilePicture);
        dest.writeByte((byte) (this.adult ? 1 : 0));
    }
}//end of class
