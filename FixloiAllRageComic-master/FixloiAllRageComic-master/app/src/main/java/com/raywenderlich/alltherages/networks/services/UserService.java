package com.raywenderlich.alltherages.networks.services;

import com.raywenderlich.alltherages.networks.jsonmodels.Token;
import com.raywenderlich.alltherages.networks.jsonmodels.UserUpdate;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by laptopTCC on 6/2/2017.
 */

public interface UserService {
    //Post a new account
    @POST("api/register")
    Call<Token> postNewAccount(@Body RequestBody body);
    //Login an existing account
    @POST("api/login")
    Call<Token> login(@Body RequestBody body);
    //get an account
    @GET("api/register/{id}")
    Call<UserUpdate> getUserInfo(@Path("id") String UserId);
    //update an account
    @PUT("api/register/{id}")
    Call<UserUpdate> putUserUpdate(@Path("id") String id, @Body UserUpdate body);
}
