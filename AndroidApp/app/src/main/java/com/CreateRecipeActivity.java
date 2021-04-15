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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Switch;
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
import java.util.Arrays;
import java.util.List;

public class CreateRecipeActivity
        extends ActivityWithNavigation
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_READ_STORAGE = 1;
    public static final int REQUEST_SELECT_IMAGE = 2;
    public static final int CAMERA_REQUEST_CODE = 102;

    private int creatorId = 0;
    private Recipe recipe;
    private ServerCallsApi api;
    private Bitmap bitmapImage;
    private ImageView image;
    private String filename = "";
    private List<Spinner> spinners = new ArrayList<>();
    private LinearLayout ingredientsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        listenerTakeImage();

        // store id of the user that is going to create a recipe
        User user = getIntent().getParcelableExtra("user");
        creatorId = user.getUserId();

        image = findViewById(R.id.imageRecipe);

        //Define spinner (dropdown)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateRecipeActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.units));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Define and set up navigation bar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarCreate);
        navigationView.setCheckedItem(R.id.nav_upload_recipe);
        initNavigationBar();

        //Create ServerCallsApi object
        api = createApi();

        //Create ingredient fields
        int nrOfIngredients = 2;
        ingredientsLayout = findViewById(R.id.ingredientsLayout);
        for(int i = 0; i < nrOfIngredients; i++){
            LinearLayout line = new LinearLayout(this);
            line.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            line.setPadding(16, 16, 0, 0);
            line.setOrientation(LinearLayout.HORIZONTAL);

            EditText ingredient = new EditText(this);
            ingredient.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            ingredient.setHint(R.string.edit_ingredient);
            line.addView(ingredient);

            EditText quantity = new EditText(this);
            quantity.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            quantity.setHint("0");
            line.addView(quantity);

            Spinner spinner = new Spinner(this);
            spinner.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            spinner.setAdapter(adapter);
            line.addView(spinner);
            spinners.add(spinner);

            ingredientsLayout.addView(line);
        }
    }

    //Recipes are invalid if there is no title, no ingredient or the description is smaller than 20
    //characters
    public boolean isValidRecipe(Recipe recipe) {
        if(recipe.getName() == null) {
            Toast.makeText(getApplicationContext(),
                    "No title present", Toast.LENGTH_SHORT).show();
            return false;
        }else if(recipe.getName().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "No title present", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(recipe.getDescription().length() < 20) {
            Toast.makeText(getApplicationContext(),
                    "Description too short", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(recipe.getIngredients().size() == 0) {
            Toast.makeText(getApplicationContext(),
                    "No ingredients present", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Methods for taking a picture inside app
    public void listenerTakeImage() {
        Button button;
        button = (Button) findViewById(R.id.take_photo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera,CAMERA_REQUEST_CODE);
            }
        });
    }

    //Asks for permission to open gallery, when already granted calls openGallery()
    public void importImage(View view){
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateRecipeActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
        } else {
            openGallery();
        }
    }

    //Opens gallery
    public void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_SELECT_IMAGE);
    }

    //Result of camera or gallery import, depending on the requestCode
    //Puts imported image in bitmapImage and shows it in the activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If the request is to select image from gallery:
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
        //If the request is to open camera
        if(requestCode == CAMERA_REQUEST_CODE)
        {
            bitmapImage = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmapImage);
        }
    }

    //Result in case permission was asked to open gallery, if granted opens gallery
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_READ_STORAGE && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Retrieves input for new recipe from fields and combines the data into a Recipe object
    //When no image is imported, applies default image and calls postRecipe()
    //When an image is imported, calls uploadImage()
    public void createRecipe(View view){

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

                //Check that something was actually entered in the name / quantity of an
                //ingredient.
                if (!ingredientString.equals("") && !quantityString.equals("")) {
                    ingredients.add(ingredientString);
                    ingredientString = "";
                    quantities.add(quantityString + " " +
                            spinners.get(i).getSelectedItem().toString());
                    quantityString = "";
                }
            }
        }

        //Number of people
        EditText editNumberOfPeople = findViewById(R.id.editTextNrPeople);
        int numberOfPeople = 0;
        if(!editNumberOfPeople.getText().toString().equals("")){
            numberOfPeople = Integer.parseInt(editNumberOfPeople.getText().toString());
        }

        //Tags
        EditText editTags = findViewById(R.id.editTags);
        String tagString = editTags.getText().toString();
        List<String> tags = new ArrayList<>(Arrays.asList(tagString.split(",")));

        // set if recipe is for adults or not
        Switch sw = findViewById(R.id.switch1);
        boolean adult = sw.isChecked();

        this.recipe = new Recipe(creatorId, name, description, ingredients,
                quantities, numberOfPeople, adult, tags);
        if (isValidRecipe(this.recipe)) {
            if(bitmapImage == null){
                this.recipe.setFilename("logo.png");
                postRecipe();
            }else {
                uploadImage();
            }
        }
    }

    //Tries to upload image stored in bitmapImage to server with filename as response
    //If successful, sets recipe filename to response and calls postRecipe()
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
            MultipartBody.Part body = MultipartBody.Part.createFormData(
                    "upload", file.getName(), reqFile);
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
                            Toast.makeText(getApplicationContext(),
                                    "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                            postRecipe();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),
                            "Upload failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Uploads recipe to server database
    public void postRecipe(){
        Call<Recipe> call = api.createRecipe(this.recipe);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if(response.isSuccessful()){
                    Recipe recipe = response.body();
                    Toast.makeText(getApplicationContext(),
                            recipe.getName() + " created!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //Starts activity based on button clicked in navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_upload_recipe: // If you try to enter upload_recipe
                // Just break - same screen
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                super.onNavigationItemSelected(menuItem);
        }
        return true;
    }
}