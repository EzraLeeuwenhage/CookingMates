package com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.cookingmatesapp.R;

import android.content.Intent;
import android.view.View;

import com.example.cookingmatesapp.MainActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button deleteAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // define login button
        deleteAcc = (Button) findViewById(R.id.delete_acc_btn);
        deleteAcc.setOnClickListener(this);

    };

    @Override
    public void onClick (View v){
        Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(myIntent);
        //optional
        finish();
    }

}
