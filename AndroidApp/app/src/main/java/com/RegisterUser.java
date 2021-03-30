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

public class RegisterUser extends AppCompatActivity {

    // defining data to enter
    private ServerCallsApi api;
    private EditText editTextUsername;
    private EditText editTextFullName;
    private EditText editTextAge;
    private EditText editTextEmail;
    private EditText editTextPassword;

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
        String username = editTextUsername.getText().toString();
        String name = editTextFullName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        // create window to show response
        TextView responseView = findViewById(R.id.textViewResult);

        try {
            String dateAsString = editTextAge.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            format.setLenient(false);
            Date date = format.parse(dateAsString);
            System.out.println("date: " + date.toString());

            // TODO add option for profile picture and adult stuff
            // TODO add email verification?
            User user = new User(username, name, email, password, date, null);
            Call<User> call = api.createUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(!response.isSuccessful()){
                        responseView.setText("Code: " + response.code());
                        return;
                    }
                    User user = response.body();
                    responseView.setText(user.getName() + " created!");
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    responseView.setText(t.getMessage());
                }
            });
        } catch (Exception e) {
            if (e.getClass() == ParseException.class) {
                responseView.setText("The entered date of birth was not valid, please enter the " +
                        "date in the following format: (dd-mm-yyyy)");
            }
        }
    }
}