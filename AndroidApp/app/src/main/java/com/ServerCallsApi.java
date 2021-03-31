package com;

import java.util.HashMap;
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
import retrofit2.http.PUT;
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

    @POST("login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @GET("users")
    Call<List<User>> getUsers();

    @POST("users")
    Call<User> createUser(@Body User user);

    //Gets user with specified name
    @GET("users/{username}/{password}")
    Call<User> getUserByUsername(@Path("username") String username, @Path("password") String password);

    //Completely replaces object
    @PUT("users")
    Call<User> putUser(@Body User user);

    //Only changes specified fields
    @PATCH("users")
    Call<User> patchUser(@Body User user);

    @DELETE("users")
    Call<Void> deleteUser(@Body User user);

    @POST("recipes")
    Call<Recipe> createRecipe(@Body Recipe recipe);

    @GET("recipes")
    Call<List<Recipe>> getRecipes();

    @GET("recipes/{id}")
    Call<List<Recipe>> getRecipe(@Path("id") int id);

    @GET("recipes/0/{name}")
    Call<List<Recipe>> getRecipeByTitle(@Path("name") String name);

    @GET("recipes/0/0/{creatorid}")
    Call<List<Recipe>> getRecipeByCreator(@Path("creatorid") int creatorid);

    @GET("recipes/0/0/0/{ingredient}")
    Call<List<Recipe>> getRecipeByIngredient(@Path("ingredient") String ingredient);

    //Completely replaces object
    @PUT("recipes/{id}")
    Call<Recipe> putRecipe(@Path("id") int id, @Body Recipe recipe);

    //Only changes specified fields
    @PATCH("recipes/{id}")
    Call<Recipe> patchRecipe(@Path("id") int id, @Body Recipe recipe);

    @PATCH("recipes/rating/{id}")
    Call<Void> addRatingToRecipe(@Path("id") int id, @Body List<Integer> ratings);

    @PATCH("recipes/review/{id}")
    Call<Void> addReviewToRecipe(@Path("id") int id, @Body List<String> reviews);

    @DELETE("recipes/{id}")
    Call<Void> deleteRecipe(@Path("id") int id);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

}
