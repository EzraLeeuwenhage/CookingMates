package com.util;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ActivityWithNavigation;
import com.Recipe;
import com.RecipeActivity;
import com.User;
import com.example.cookingmatesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchHelper {

    private Intent intent;
    private ActivityWithNavigation activity;
    private List<Recipe> recipes = new ArrayList<>();

    public SearchHelper(Intent intent, ActivityWithNavigation activity){
        this.intent = intent;
        this.activity = activity;
    }

    //Retrieves image of specified recipe from server and puts it in specified ImageButton
    public void getImageInButton(ImageButton btn, Recipe recipe){
        String path = "http://134.209.92.24:3000/uploads/" + recipe.getFilename();
        Picasso.get().load(path).into(btn);
        btn.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    //Filters out adult recipes if intent's user is child
    public List<Recipe> filterRecipes(List<Recipe> recipeList){
        List<Recipe> recipes = new ArrayList<>();

        User user = intent.getParcelableExtra("user");
        if (!user.isAdult()) {
            for (Recipe recipe: recipeList) {
                if (!recipe.isForAdult()) {
                    recipes.add(recipe);
                }
            }
            recipeList = recipes;
        }

        return recipeList;
    }

    //Creates an ImageButton for every recipe in the specified list, sets the image of the button
    // to the recipe's image and links the button to RecipeActivity
    public void createButtons(List<Recipe> list, LinearLayout layout) {
        layout.removeAllViews();

        if(list != null) {
            for (int i = 0; i < list.size() && i < 10; i++) {
                ImageButton btn = new ImageButton(activity);
                btn.setId(i);
                if (list.get(i).getFilename() == null) {
                    btn.setImageResource(R.drawable.logo);
                } else {
                    getImageInButton(btn, list.get(i));
                }
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400));
                btn.setPadding(0, 32, 0,0);
                layout.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity.getApplicationContext(), RecipeActivity.class);
                        intent.putExtra("recipe", list.get(v.getId()));
                        activity.passUserObject(intent);
                        activity.startActivity(intent);
                    }
                });
            }
        }
    }

    //Actually makes the call for the get methods
    public void makeCall(Call<List<Recipe>> call, LinearLayout layout, List<Recipe> recipeList){
        this.recipes = recipeList;

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    //Filter recipe result based on age, then create buttons for recipes
                    recipes = response.body();
                    recipes = filterRecipes(recipes);
                    createButtons(recipes, layout);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
