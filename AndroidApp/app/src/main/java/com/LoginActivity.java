package com;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookingmatesapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ServerCallsApi api;
    private Button login;
    private TextView register;
    private TextView reset;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);

        // define login button
        login = (Button) findViewById(R.id.signInBtn);
        login.setOnClickListener(this);

        // define register button
        register = (TextView) findViewById(R.id.textView4);
        register.setOnClickListener(this);

        reset = (TextView) findViewById(R.id.textView5);
        reset.setOnClickListener(this);

        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInBtn:

                Call<User> call = api.getUserByUsername(
                        username.getText().toString(), password.getText().toString());

                // create window to show response
                TextView responseView = findViewById(R.id.textViewResult);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            User user = response.body();
                            Log.i("userdate", user.getDateOfBirth().toString());

                            Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            myIntent.putExtra("user", user);
                            startActivity(myIntent);
                            //optional
                            finish();
                        } else if (response.code() == 404) {
                            responseView.setText("Wrong username or password entered!");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t)  {
                        responseView.setText(t.getMessage());
                    }
                });
                break;
            case R.id.textView4:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.recipeIngredientsHeader:
                startActivity(new Intent(this, ResetPassword.class));
                break;
        }
    }
}
