package com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class RecipeActivity
        extends ActivityWithNavigation
        implements NavigationView.OnNavigationItemSelectedListener {

    private Recipe recipe;
    private ServerCallsApi api;
    private int localRating = 0; // Rating of star that has been pressed before submitting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //Define and set up navigation bar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarRecipe);
        navigationView.setNavigationItemSelectedListener(this);
        initNavigationBar();

        //Create ServerCallsApi object
        api = createApi();

        displayRecipe();
    }

    //Show recipe data in activity
    public void displayRecipe(){
        Intent intent = getIntent();
        this.recipe = (Recipe)intent.getParcelableExtra("recipe");
        if(recipe != null) {
            //Recipe name
            TextView text = findViewById(R.id.recipeTitle);
            text.setText(recipe.getName());

            //Rating
            updateRatingText();

            //Image
            ImageView image = findViewById(R.id.recipeImage);
            getImage(image, recipe);

            //Tags
            TextView tags = findViewById(R.id.recipeTag);
            String tagString = android.text.TextUtils.join(",", recipe.getTags());
            if(tagString.equals("")) {
                tags.setText("-");
            } else {
                tags.setText(tagString);
            }

            //Number of people
            TextView nrPeople = findViewById(R.id.editNrPeople);
            nrPeople.setText(" " + recipe.getNumberpeople());

            //Ingredients with their respective quantities
            if(recipe.getIngredients() != null && recipe.getQuantities() != null) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.ingredientsLayout);

                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    String ingredient = recipe.getQuantities().get(i) + " "
                            + recipe.getIngredients().get(i);
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(ingredient);

                    if(ll != null) {
                        ll.addView(tv);
                    }
                }
            }

            //Description
            TextView description = findViewById(R.id.recipeDescription);
            description.setText(recipe.getDescription());

            //Reviews
            updateReviewLayout();
        }
    }

    //Adds rating to recipe and updates record in database
    //First 5 buttons are 1-5 stars to select a rating
    //Last button adds rating to recipe and makes server call if a rating is selected
    public void sendRating(View view){
        switch (view.getId()){
            case R.id.rating1:
                localRating = 1;
                break;
            case R.id.rating2:
                localRating = 2;
                break;
            case R.id.rating3:
                localRating = 3;
                break;
            case R.id.rating4:
                localRating = 4;
                break;
            case R.id.rating5:
                localRating = 5;
                break;
            case R.id.buttonRating:
                if(localRating != 0){
                    recipe.addRating(localRating);

                    Call<Void> call = api.addRatingToRecipe(recipe.getRecipeId(), recipe);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(getApplicationContext(),
                                        "Rating added!", Toast.LENGTH_SHORT).show();
                                updateRatingText();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
                break;
        }
    }

    //Updates rating textView to show recipe's current rating
    public void updateRatingText(){
        TextView rating = findViewById(R.id.recipeRating);
        if(recipe.hasRating()) {
            rating.setText(String.format(Locale.getDefault(), "%.2f", recipe.getRating()));
        }else{
            rating.setText("-");
        }
    }

    //Adds review to recipe and updates record in database
    public void sendReview(View view){
        EditText editReview = findViewById(R.id.editReview);
        String review = editReview.getText().toString();
        recipe.addReview(review);

        Call<Void> call = api.addReviewToRecipe(recipe.getRecipeId(), recipe);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),
                            "Review added!", Toast.LENGTH_SHORT).show();
                    updateReviewLayout();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //Updates list of reviews to show added review without restarting the activity
    public void updateReviewLayout(){
        if(recipe.getReviews() != null){
            LinearLayout ll = findViewById(R.id.reviewLayout);
            ll.removeAllViews();
            for (int i = 0; i < recipe.getReviews().size(); i++) {
                String review = recipe.getReviews().get(i);
                TextView tv = new TextView(this);
                tv.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                tv.setText(review);
                if(ll != null) {
                    ll.addView(tv);
                }
            }
        }
    }

    //Retrieves image of specified recipe from server and puts it in specified ImageView
    public void getImage(ImageView v, Recipe recipe){
        String path = "http://134.209.92.24:3000/uploads/" + recipe.getFilename();
        Picasso.get().load(path).into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) { }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                Bitmap bitmapImage = bitmap;
                v.setImageBitmap(bitmapImage);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable arg0) { }
        });
    }
}