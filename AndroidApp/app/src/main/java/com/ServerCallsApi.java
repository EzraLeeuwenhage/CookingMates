package com;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServerCallsApi {

    //Posts specified user to server to put in database
    @POST("users")
    Call<Void> createUser(@Body User user);

    //Gets user with specified name and password from database
    @GET("users/{username}/{password}")
    Call<User> getUserByUsername(@Path("username") String username,
                                 @Path("password") String password);

    //Deletes user with specified id from database
    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    //Posts specified user to server to put in database
    @POST("recipes")
    Call<Recipe> createRecipe(@Body Recipe recipe);

    //Gets all recipes from database
    @GET("recipes")
    Call<List<Recipe>> getRecipes();

    //Gets all recipes from database that have specified string as a substring of their name
    @GET("recipes/0/{name}")
    Call<List<Recipe>> getRecipeByTitle(@Path("name") String name);

    //Gets all recipes from database that have specified string as a substring of at least
    // one of their ingredients
    @GET("recipes/0/0/0/{ingredient}")
    Call<List<Recipe>> getRecipeByIngredient(@Path("ingredient") String ingredient);

    //Gets all recipes from database that have specified string as a substring of at least
    // one of their tags
    @GET("recipes/0/0/0/0/{tag}")
    Call<List<Recipe>> getRecipeByTag(@Path("tag") String tag);

    //Patches recipe with specified id in database using specified recipe with new rating
    @PATCH("recipes/rating/{id}")
    Call<Void> addRatingToRecipe(@Path("id") int id, @Body Recipe recipe);

    //Patches recipe with specified id in database using specified recipe with new review
    @PATCH("recipes/review/{id}")
    Call<Void> addReviewToRecipe(@Path("id") int id, @Body Recipe recipe);

    //Posts specified image to server in /uploads
    @Multipart
    @POST("/upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

}
