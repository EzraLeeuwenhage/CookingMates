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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private TextView register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // define login button
        login = (Button)findViewById(R.id.signInBtn);
        login.setOnClickListener(this);
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(myIntent);
//                //optional
//                finish();
//            }
//        });

        // define register button
        register = (TextView) findViewById(R.id.textView4);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // TODO fix this stuff
            case R.id.signInBtn:
                Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(myIntent);
                //optional
                finish();
                break;
            case R.id.textView4:
                startActivity(new Intent(this, RegisterUser.class));
                break;
        }
    }
}
