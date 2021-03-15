package com;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

}
