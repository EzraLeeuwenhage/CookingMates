package com;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cookingmatesapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class SearchRecipe extends AppCompatActivity {

    private ServerCallsApi api;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private EditText editTextId;
    private EditText editTextTitle;
    private ImageView imageView;

    private Bitmap bitmapImage;
    private Recipe recipe;

    private List<Recipe> foundRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);

        textViewTitle = findViewById(R.id.textViewResult);
        textViewDescription = findViewById(R.id.textViewDescription);
        editTextId = findViewById(R.id.editTextNumber);
        editTextTitle = findViewById(R.id.editTitle);
        imageView = findViewById(R.id.imageView2);
    }

    public void getRecipes(View view){
        Call<List<Recipe>> call = api.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()){
                    textViewTitle.setText("Code: " + response.code());
                    return;
                }
                foundRecipes = response.body();
                createButtons(foundRecipes, findViewById(0));
//                List<Recipe> posts = response.body();
//                recipe = posts.get(0);
//                getImage(imageView);
//
//                textViewTitle.setText(recipe.getName());
//                textViewDescription.setText(recipe.getDescription());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                textViewTitle.setText(t.getMessage());
            }
        });
    }

    public void getRecipeById(View view){
        int id = Integer.parseInt(editTextId.getText().toString());
        Call<List<Recipe>> call = api.getRecipe(id);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()){
                    textViewTitle.setText("Code: " + response.code());
                    return;
                }
                foundRecipes = response.body();
                createButtons(foundRecipes, findViewById(0));
//                List<Recipe> posts = response.body();
//                Recipe first = posts.get(0);
//                textViewTitle.setText(first.getName());
//                textViewDescription.setText(first.getDescription());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                textViewTitle.setText(t.getMessage());
            }
        });
    }

    public void getRecipeByTitle(View view){
        String title = editTextTitle.getText().toString();
        Call<List<Recipe>> call = api.getRecipeByTitle(title);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()){
                    textViewTitle.setText("Code: " + response.code());
                    return;
                }
                foundRecipes = response.body();
                createButtons(foundRecipes, findViewById(0));
//                    Recipe first = posts.get(0);
//                    textViewTitle.setText(first.getName());
//                    textViewDescription.setText(first.getDescription());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                textViewTitle.setText(t.getMessage());
            }
        });
    }

    public void getImage(ImageView view){
        String path = "http://134.209.92.24:3000/uploads" + recipe.getFilename();
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

    public void getImageInButton(ImageButton btn, Recipe recipe){
        String path = "http://134.209.92.24:3000/uploads/" + recipe.getFilename();
        Picasso.get().load(path).into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) { }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                Bitmap bitmapImage = bitmap;
                btn.setImageBitmap(bitmapImage);
                btn.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable arg0) { }
        });
    }

    public void createButtons(List<Recipe> list, LinearLayout layout){
        if(list != null) {
            LinearLayout ll = layout;

            for (int i = 0; i < list.size() && i < 10; i++) {
                ImageButton btn = new ImageButton(this);
                btn.setId(i);
                if (list.get(i).getFilename() == null) {
                    btn.setImageResource(R.drawable.logo);
                } else {
                    getImageInButton(btn, list.get(i));
                }
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400));
                btn.setPadding(0, 32, 0,0);
                ll.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchRecipe.this, RecipeActivity.class);
                        intent.putExtra("recipe", foundRecipes.get(v.getId()));
                        startActivity(intent);
                    }
                });
            }
        }
    }
}