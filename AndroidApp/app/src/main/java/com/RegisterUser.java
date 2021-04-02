package com;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cookingmatesapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity {

    // defining data to enter
    private ServerCallsApi api;
    private EditText editTextUsername;
    private EditText editTextFullName;
    private EditText editTextAge;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void createAccount(View view) {
        // retrieving entered data
        username = editTextUsername.getText().toString();
        String name = editTextFullName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        // create window to show response
        TextView responseView = findViewById(R.id.textViewResult);

        // validate username
        if (username == null || username == "") {
            responseView.setText("Please enter a username!");
            return;
        }

        // validate full name
        if (name == null || name == "") {
            responseView.setText("Please enter a full name!");
            return;
        }

        //validate email
        if (!isValidEmail(email)) {
            responseView.setText("Email is not valid!");
            return;
        }

        //validate password
        if (!isValidPassword(password)) {
            responseView.setText("Password is not valid!");
            return;
        }

        try {
            String dateAsString = editTextAge.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            format.setLenient(false);
            Date date = format.parse(dateAsString);

            String oldDate = "01-01-1900";
            Date old = format.parse(oldDate);

            // check if date is also reasonable (at least year 1900 and at most current date)
            if (date.before(old) || date.after(new Date())) {
                responseView.setText("Enter a date after the 19th century and " +
                        "before the current date!");
                return;
            }

            User user = new User(username, name, email, password, date, "new String()");
            Call<Void> call = api.createUser(user);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        responseView.setText(username + " created!");

                        Intent myIntent = new Intent(RegisterUser.this, HomeActivity.class);
                        myIntent.putExtra("user", user);
                        startActivity(myIntent);
                    } else if (response.code() == 404) {
                        responseView.setText(username + " already exists as username!");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            if (e.getClass() == ParseException.class) {
                responseView.setText("The entered date of birth was not valid, please enter the " +
                        "date in the following format: (dd-mm-yyyy)");
            }
        }
    }

    // method for validating password
    public static boolean isValidPassword(String password) {
        // stores correct format for password
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{7,1000}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //method for validating email
    public static boolean isValidEmail(String email)
    {
        // stores correct format for email
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +  //part before @
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}