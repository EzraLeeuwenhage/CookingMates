package com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookingmatesapp.R;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateRecipeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_READ_STORAGE = 1;
    private final int REQUEST_SELECT_IMAGE = 2;

    private int creatorId = 0;
    private Recipe recipe;
    private ServerCallsApi api;
    private Bitmap bitmapImage;
    private ImageView image;
    private String filename = "";
    private List<Spinner> spinners = new ArrayList<>();

    LinearLayout ingredientsLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        // store id of the user that is going to create a recipe
        User user = getIntent().getParcelableExtra("user");
        creatorId = user.getUserId();

        image = findViewById(R.id.imageRecipe);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateRecipeActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.units));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://134.209.92.24:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerCallsApi.class);

        // Navigation
        // Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarCreate);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_upload_recipe);

        ingredientsLayout = findViewById(R.id.ingredientsLayout);
        for(int i = 0; i < 2; i++){
            LinearLayout line = new LinearLayout(this);
            line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            line.setPadding(16, 16, 0, 0);
            line.setOrientation(LinearLayout.HORIZONTAL);

            EditText ingredient = new EditText(this);
            ingredient.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            ingredient.setHint(R.string.edit_ingredient);
            line.addView(ingredient);

            EditText quantity = new EditText(this);
            quantity.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            quantity.setHint("0");
            line.addView(quantity);

            Spinner spinner = new Spinner(this);
            spinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            spinner.setAdapter(adapter);
            line.addView(spinner);
            spinners.add(spinner);

            ingredientsLayout.addView(line);
        }
    }

    //Recipes are invalid if there is no title, no ingredient or the description is smaller than 20
    //characters
    public boolean isValidRecipe(Recipe recipe){
        if(recipe.getName() == null){
            Toast.makeText(getApplicationContext(), "No title present", Toast.LENGTH_SHORT).show();
            return false;
        }else if(recipe.getName().equals("")){
            Toast.makeText(getApplicationContext(), "No title present", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(recipe.getDescription().length() < 20) {
            Toast.makeText(getApplicationContext(), "Description too short", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(recipe.getIngredients().size() == 0){
            Toast.makeText(getApplicationContext(), "No ingredients present", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

//Start methods for importing images
    public void importImage(View view){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateRecipeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
        }else{
            selectImage();
        }
    }

    public void selectImage(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                if(imageUri != null) {
                    try {
                        InputStream stream = getContentResolver().openInputStream(imageUri);
                        bitmapImage = BitmapFactory.decodeStream(stream);
                        image.setImageBitmap(bitmapImage);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_READ_STORAGE && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
//End methods for importing images

    public void createRecipe(View view){
        if(bitmapImage == null){
            //TODO upload default image
            Toast.makeText(this, "No image is uploaded", Toast.LENGTH_SHORT).show();
            return;
        }

        //Title
        EditText editTitle = findViewById(R.id.editTitle);
        String name = editTitle.getText().toString();

        //Description
        EditText editDescription = findViewById(R.id.editDescription);
        String description = editDescription.getText().toString();

        //Ingredients + Quantities
        List<String> ingredients = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            LinearLayout line = (LinearLayout) ingredientsLayout.getChildAt(i);
            String ingredientString = "";
            String quantityString = "";
            for (int j = 0; j < line.getChildCount(); j++) {
                switch (j) {
                    case 0:
                        EditText ingredient = (EditText) line.getChildAt(j);
                        ingredientString = ingredient.getText().toString();
                        break;
                    case 1:
                        EditText quantity = (EditText) line.getChildAt(j);
                        quantityString = quantity.getText().toString();
                        break;
                }

                if (!ingredientString.equals("") && !quantityString.equals("")) {
                    Log.i("ingredient", ingredientString + " - " + quantityString + spinners.get(i).getSelectedItem().toString());
                    ingredients.add(ingredientString);
                    ingredientString = "";
                    quantities.add(quantityString + " " + spinners.get(i).getSelectedItem().toString());
                    quantityString = "";
                    Log.i("ingredient", ingredients.size() + " - " + quantities.size());
                }
            }
        }

        //Number of people, adult
        EditText editNumberOfPeople = findViewById(R.id.editTextNrPeople);
        int numberOfPeople = 0;
        if(!editNumberOfPeople.getText().toString().equals("")){
            numberOfPeople = Integer.parseInt(editNumberOfPeople.getText().toString());
        }

        // set if recipe is for adults or not
        Switch sw = findViewById(R.id.switch1);
        boolean adult = sw.isChecked();

        this.recipe = new Recipe(creatorId, name, description, ingredients, quantities, numberOfPeople, adult);
        if (isValidRecipe(this.recipe)) {
            Log.i("created", "Recipe valid");
            uploadImage();
        }
    }

    public void uploadImage(){
        try {
            //Create new file
            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            //Write data to file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            //Create retrofit objects to send the file
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

            //Send call to post image
            Call<ResponseBody> req = api.postImage(body, name);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        try {
                            filename = response.body().string();
                            recipe.setFilename(filename);
                            Toast.makeText(getApplicationContext(), "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                            postRecipe();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postRecipe(){
        Call<Recipe> call = api.createRecipe(this.recipe);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Recipe recipe = response.body();
                Toast.makeText(getApplicationContext(), recipe.getName() + " created!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // TODO add "Are you sure?" message on quit
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                Intent home_intent = new Intent(CreateRecipeActivity.this, HomeActivity.class);
                home_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(home_intent);
                startActivity(home_intent);
                break;
            case R.id.nav_profile:
                Intent profile_intent = new Intent(CreateRecipeActivity.this, ProfileActivity.class);
                profile_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(profile_intent);
                startActivity(profile_intent);
                break;
            case R.id.nav_settings:
                Intent settings_intent = new Intent(CreateRecipeActivity.this, SettingsActivity.class);
                settings_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(settings_intent);
                startActivity(settings_intent);
                break;
            case R.id.nav_about:
                Intent about_intent = new Intent(CreateRecipeActivity.this, AboutActivity.class);
                about_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(about_intent);
                startActivity(about_intent);
                break;
            case R.id.nav_help:
                Intent help_intent = new Intent(CreateRecipeActivity.this, HelpActivity.class);
                help_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(help_intent);
                startActivity(help_intent);
                break;
            case R.id.nav_logout:
                Intent logout_intent = new Intent(CreateRecipeActivity.this, LoginActivity.class);
                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout_intent);
                break;
            case R.id.nav_upload_recipe:
                // Just break - same screen
                break;
            case R.id.nav_search_recipe:
                Intent search_recipe_intent = new Intent(CreateRecipeActivity.this, SearchRecipe.class);
                search_recipe_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                passUserObject(search_recipe_intent);
                startActivity(search_recipe_intent);
                break;
            case R.id.nav_findcookingmates:
                break;
            case R.id.nav_contact:
                break;
            case R.id.nav_instagram:
                break;
            case R.id.nav_facebook:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void passUserObject(Intent myIntent) {
        Intent currentIntent = getIntent();
        User user = (User) currentIntent.getParcelableExtra("user");
        myIntent.putExtra("user", user);
    }
}