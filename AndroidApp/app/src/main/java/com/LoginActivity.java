package com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookingmatesapp.MainActivity;
import com.example.cookingmatesapp.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ServerCallsApi api;
    private Button login;
    private TextView register;
    private TextView reset;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // TODO check database for account upon logging in
            // TODO only log in when entered info is correct
            case R.id.signInBtn:
                Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(myIntent);
                //optional
                finish();
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
