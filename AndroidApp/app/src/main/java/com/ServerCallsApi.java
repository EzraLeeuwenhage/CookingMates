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

/*
    @GET -> query data
    @POST -> create new record
    @PUT -> update record
    @DELETE -> delete record

    @Body sends data with the request
    @Path("var") + {var} allow for variable query
 */

    @POST("users")
    Call<Void> createUser(@Body User user);

    //Gets user with specified name
    @GET("users/{username}/{password}")
    Call<User> getUserByUsername(@Path("username") String username, @Path("password") String password);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    @POST("recipes")
    Call<Recipe> createRecipe(@Body Recipe recipe);

    @GET("recipes")
    Call<List<Recipe>> getRecipes();

    @GET("recipes/0/{name}")
    Call<List<Recipe>> getRecipeByTitle(@Path("name") String name);

    @GET("recipes/0/0/0/{ingredient}")
    Call<List<Recipe>> getRecipeByIngredient(@Path("ingredient") String ingredient);

    @GET("recipes/0/0/0/0/{tag}")
    Call<List<Recipe>> getRecipeByTag(@Path("tag") String tag);

    @PATCH("recipes/rating/{id}")
    Call<Void> addRatingToRecipe(@Path("id") int id, @Body Recipe recipe);

    @PATCH("recipes/review/{id}")
    Call<Void> addReviewToRecipe(@Path("id") int id, @Body Recipe recipe);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

}
