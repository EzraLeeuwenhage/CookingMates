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


/*  This is commented out because the user class is commented out
    @GET("users")
    Call<List<User>> getUsers();

    @POST("users")
    Call<User> createUser(@Body User user);

    //Gets recipe with specified id
    @GET("users/{id}")
    Call<List<User>> getUser(@Path("id") int id);

    //Gets recipe with specified name
    @GET("users/0/{name}")
    Call<List<User>> getUserByName(@Path("name") String name);

    //Completely replaces object
    @PUT("users/{id}")
    Call<User> putUser(@Path("id") int id, @Body User user);

    //Only changes specified fields
    @PATCH("users/{id}")
    Call<User> patchUser(@Path("id") int id, @Body User user);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") int id);
*/
    @POST("recipes")
    Call<RecipePost> createRecipe(@Body RecipePost recipe);

    @GET("recipes")
    Call<List<RecipePost>> getPosts();

    @GET("recipes/{id}")
    Call<List<RecipePost>> getRecipe(@Path("id") int id);

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

    @Multipart
    @POST("/upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

}
