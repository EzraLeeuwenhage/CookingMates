package com;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookingmatesapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
    
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ServerCallsApi api;
    private Spinner spinner;
    private Button login;
    private TextView register;
    private TextView reset;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.account_types));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

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

                // create window to show response
                TextView responseView = findViewById(R.id.textViewResult);

                if(!isValidPassword(password.getText().toString())){
                    responseView.setText("Invalid password");
                    return;
                }

                Call<User> call = api.getUserByUsername(
                        username.getText().toString(), password.getText().toString());

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            User user = response.body();

                            Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            myIntent.putExtra("user", user);
                            myIntent.putExtra("cook", spinner.getSelectedItem().toString());
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

    // method for validating password
    public static boolean isValidPassword(String password) {
        // stores correct format for password
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{7,1000}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
