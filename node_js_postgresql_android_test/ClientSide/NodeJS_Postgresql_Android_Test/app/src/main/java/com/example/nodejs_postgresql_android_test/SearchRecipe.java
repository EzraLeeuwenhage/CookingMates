package com.example.nodejs_postgresql_android_test;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nodejs_postgresql_android_test.retrofit.INodeJS;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class SearchRecipe extends AppCompatActivity {

    private INodeJS api;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private EditText editTextId;
    private EditText editTextTitle;
    private ImageView imageView;

    private Bitmap bitmapImage;
    private RecipePost recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(INodeJS.class);

        textViewTitle = (TextView) findViewById(R.id.textViewResult);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        editTextId = (EditText) findViewById(R.id.editTextNumber);
        editTextTitle = (EditText) findViewById(R.id.editTitle);
        imageView = findViewById(R.id.imageView2);
    }

    public void getRecipes(View view){
        Call<List<RecipePost>> call = api.getPosts();
        call.enqueue(new Callback<List<RecipePost>>() {
            @Override
            public void onResponse(Call<List<RecipePost>> call, Response<List<RecipePost>> response) {
                if(!response.isSuccessful()){
                    textViewTitle.setText("Code: " + response.code());
                    return;
                }
                List<RecipePost> posts = response.body();
                recipe = posts.get(0);
                getImage(imageView);

                textViewTitle.setText(recipe.getTitle());
                textViewDescription.setText(recipe.getDescription());
            }

            @Override
            public void onFailure(Call<List<RecipePost>> call, Throwable t) {
                textViewTitle.setText(t.getMessage());
            }
        });
    }

    public void getRecipeById(View view){
        int id = Integer.parseInt(editTextId.getText().toString());
        Call<List<RecipePost>> call = api.getRecipe(id);
        call.enqueue(new Callback<List<RecipePost>>() {
            @Override
            public void onResponse(Call<List<RecipePost>> call, Response<List<RecipePost>> response) {
                if(!response.isSuccessful()){
                    textViewTitle.setText("Code: " + response.code());
                    return;
                }
                List<RecipePost> posts = response.body();
                RecipePost first = posts.get(0);
                textViewTitle.setText(first.getTitle());
                textViewDescription.setText(first.getDescription());
            }

            @Override
            public void onFailure(Call<List<RecipePost>> call, Throwable t) {
                textViewTitle.setText(t.getMessage());
            }
        });
    }

    public void getRecipeByTitle(View view){
        String title = editTextTitle.getText().toString();
        Call<List<RecipePost>> call = api.getRecipeByTitle(title);
        call.enqueue(new Callback<List<RecipePost>>() {
            @Override
            public void onResponse(Call<List<RecipePost>> call, Response<List<RecipePost>> response) {
                if(!response.isSuccessful()){
                    textViewTitle.setText("Code: " + response.code());
                    return;
                }
                List<RecipePost> posts = response.body();
                if(posts.size() > 0) {
                    RecipePost first = posts.get(0);
                    textViewTitle.setText(first.getTitle());
                    textViewDescription.setText(first.getDescription());
                }
            }

            @Override
            public void onFailure(Call<List<RecipePost>> call, Throwable t) {
                textViewTitle.setText(t.getMessage());
            }
        });
    }

    public void getImage(ImageView view){
        String path = "http://localhost:3000/"+ recipe.getFilepath() + recipe.getFilename();
        Picasso.get().load(path).into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) { }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                if(bitmap != null) {
                    Log.i("bitmapTag", "Bitmap is being retrieved");
                }
                bitmapImage = bitmap;
                if(bitmapImage != null){
                    Log.i("drawableNull", "Drawable is not null");
                }else{
                    Log.i("drawableNotNull", "Drawable is null");
                }
                view.setImageBitmap(bitmapImage);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable arg0) { }
        });
    }
}