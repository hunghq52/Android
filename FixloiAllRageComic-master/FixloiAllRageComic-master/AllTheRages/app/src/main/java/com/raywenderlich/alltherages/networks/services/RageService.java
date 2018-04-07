package com.raywenderlich.alltherages.networks.services;

import com.raywenderlich.alltherages.networks.jsonmodels.RageResponseJson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by laptopTCC on 6/2/2017.
 */

public interface RageService {
    @POST("api/rage")
    Call<RageResponseJson> addNewRage(@Body RageResponseJson rage);
    @POST("api/rage/comment/{id}")
    Call<RageResponseJson> rageComment(@Path("id") String id);
    @GET("api/rage")
    Call<List<RageResponseJson>> getRages();

}
