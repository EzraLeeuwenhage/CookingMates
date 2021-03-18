package com.example.nodejs_postgresql_android_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nodejs_postgresql_android_test.retrofit.INodeJS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CreateRecipeActivity extends AppCompatActivity {

    private final int REQUEST_READ_STORAGE = 1;
    private final int REQUEST_SELECT_IMAGE = 2;

    private INodeJS api;
    private Bitmap bitmapImage;
    private ImageView image;
    private String filepath = "uploads/";
    private String filename = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        image = findViewById(R.id.imageRecipe);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateRecipeActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.units));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner1 = (Spinner) findViewById(R.id.unit1);
        spinner1.setAdapter(adapter);
        Spinner spinner2 = (Spinner) findViewById(R.id.unit2);
        spinner2.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(INodeJS.class);
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
            Toast.makeText(this, "No image is uploaded", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadImage();
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
        //Title
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        String title = editTitle.getText().toString();
        //Description
        EditText editDescription = (EditText) findViewById(R.id.editDescription);
        String description = editDescription.getText().toString();

        TextView textViewCreated = (TextView) findViewById(R.id.textViewResult);

        RecipePost recipe = new RecipePost(title, description, filename, filepath);
        Call<RecipePost> call = api.createRecipe(recipe);
        call.enqueue(new Callback<RecipePost>() {
            @Override
            public void onResponse(Call<RecipePost> call, Response<RecipePost> response) {
                if(!response.isSuccessful()){
                    textViewCreated.setText("Code: " + response.code());
                    return;
                }
                RecipePost recipe = response.body();
                textViewCreated.setText(recipe.getTitle() + " created!");
            }

            @Override
            public void onFailure(Call<RecipePost> call, Throwable t) {
                textViewCreated.setText(t.getMessage());
            }
        });
    }

}