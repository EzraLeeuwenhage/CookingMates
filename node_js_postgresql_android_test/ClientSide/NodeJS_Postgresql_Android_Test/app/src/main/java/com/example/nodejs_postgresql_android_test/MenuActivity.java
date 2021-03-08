package com.example.nodejs_postgresql_android_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nodejs_postgresql_android_test.retrofit.INodeJS;

public class MenuActivity extends AppCompatActivity {

    public static final String RECIPE = "com.example.myfirstapp.RECIPE";
    private INodeJS api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view){
        Intent intent = new Intent(this, SearchRecipe.class);
        startActivity(intent);
    }

    public void updateOrDelete(View view){
        Intent intent = new Intent(this, UpdateDeleteRecipeActivity.class);
        startActivity(intent);
    }

    public void createRecipe(View view){
        Intent intent = new Intent(this, CreateRecipeActivity.class);
        startActivity(intent);
    }
}