package com;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        //validate password
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,1000}$";
        if (!isValidPassword(password, regex)) {
            responseView.setText("Password is not valid!");
            return;
        }

        try {
            String dateAsString = editTextAge.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            format.setLenient(false);
            Date date = format.parse(dateAsString);

            User user = new User(username, name, email, password, date, null);
            Call<User> call = api.createUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
//                    if(!response.isSuccessful()){
//                        responseView.setText("Code: " + response.code());
//                        return;
//                    }

                    if (response.code() == 200) {
                        responseView.setText(username + " created!");
                    } else if (response.code() == 404) {
                        responseView.setText(username + " already exists as username!");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
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
    public static boolean isValidPassword(String password,String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}