package com.raywenderlich.alltherages.networks.services;

import com.raywenderlich.alltherages.database.model.OrderRequest;
import com.raywenderlich.alltherages.networks.jsonmodels.OrderResponseJson;
import com.raywenderlich.alltherages.networks.jsonmodels.DeleteResponseJson;
import com.raywenderlich.alltherages.networks.jsonmodels.UserRegisterResponseJson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by laptopTCC on 6/8/2017.
 */

public interface OrderServices {
    //Post new order
    @POST("order")
    Call<OrderResponseJson> postNewOrder(@Body OrderRequest rage);
    //add total customer spend when order success
    @GET("order")
    Call<List<OrderResponseJson>> getAllOrder();
    //get a order
    @GET("order/{id}")
    Call<OrderResponseJson> getAOrder(@Path("id") String orderId);
    //get all order follow customer
    @GET("api/register/order/id")
    Call<List<OrderResponseJson>> getAllCustomerOrder(@Path("id") String customerId);
    //update a order is success
    @PUT("order/{id}")
    Call<UserRegisterResponseJson> putAOrder(@Path("id") String orderId);
    //delete a order when you don't want to buy
    @DELETE("order/{id}")
    Call<DeleteResponseJson> deleteAOrder(@Path("id") String orderId);
}
