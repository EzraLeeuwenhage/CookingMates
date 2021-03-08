package com.example.nodejs_postgresql_android_test.retrofit;

import com.example.nodejs_postgresql_android_test.RecipePost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface INodeJS {
    @POST("recipes")
    Call<RecipePost> createRecipe(@Body RecipePost recipe);


    //Gets all recipes
    @GET("recipes")
    Call<List<RecipePost>> getPosts();

    //Gets recipe with specified id
    @GET("recipes/{id}")
    Call<List<RecipePost>> getRecipe(@Path("id") int id);

    //Gets recipe with specified name
    @GET("recipes/0/{title}")
    Call<List<RecipePost>> getRecipeByTitle(@Path("title") String title);

    //Completely replaces object
    @PUT("recipes/{id}")
    Call<RecipePost> putRecipe(@Path("id") int id, @Body RecipePost recipe);

    //Only changes specified fields
    @PATCH("recipes/{id}")
    Call<RecipePost> patchRecipe(@Path("id") int id, @Body RecipePost recipe);

    @DELETE("recipes/{id}")
    Call<Void> deleteRecipe(@Path("id") int id);
}
