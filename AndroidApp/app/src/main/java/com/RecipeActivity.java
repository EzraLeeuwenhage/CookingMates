package com;

import androidx.appcompat.app.AppCompatActivity;
import com.example.cookingmatesapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        Recipe recipe = (Recipe)intent.getParcelableExtra("recipe");
        if(recipe != null) {
            TextView text = findViewById(R.id.recipeTitle);
            text.setText(recipe.getName());
            ImageView image = findViewById(R.id.recipeImage);
            getImage(image, recipe);

            if(recipe.getIngredients() != null && recipe.getQuantities() != null && recipe.getIngredients().size() == recipe.getQuantities().size()) {
                LinearLayout ll = findViewById(R.id.recipeLayout);
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    String ingredient = recipe.getIngredients().get(i) + " " + recipe.getQuantities().get(i);
                    LayoutParams lparams = new LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(lparams);
                    tv.setText(ingredient);
                    ll.addView(tv);
                }
            }

            TextView description = findViewById(R.id.recipeDescription);
            description.setText(recipe.getDescription());
        }
    }

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