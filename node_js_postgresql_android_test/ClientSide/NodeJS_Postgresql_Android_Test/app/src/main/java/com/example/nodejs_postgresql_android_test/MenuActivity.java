package com.example.nodejs_postgresql_android_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.nodejs_postgresql_android_test.retrofit.INodeJS;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
