package com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookingmatesapp.MainActivity;
import com.example.cookingmatesapp.R;

import java.util.HashMap;

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
                // storage for login info
                HashMap<String, String> map = new HashMap<>();

                map.put("username", username.getText().toString());
                map.put("password", password.getText().toString());

                Call<LoginResult> call = api.executeLogin(map);

                // create window to show response
                TextView responseView = findViewById(R.id.textViewResult);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        //TODO use javascript code to handle responses
                        //TODO check whether case/ break works as intended
                        int x = 0;

                        if (response.code() == x) {
                            LoginResult result = response.body();

                            Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(myIntent);
                            //optional
                            finish();
                        } else if (response.code() == x) {
                            responseView.setText("Wrong username or password entered!");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t)  {
                        responseView.setText(t.getMessage());
                    }
                });
                break;
            case R.id.textView4:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.textView5:
                startActivity(new Intent(this, ResetPassword.class));
                break;
        }
    }
}
