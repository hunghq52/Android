package com.raywenderlich.alltherages.database;

import android.util.Log;

import com.raywenderlich.alltherages.database.model.OrderRequest;
import com.raywenderlich.alltherages.database.model.RageComic;
import com.raywenderlich.alltherages.networks.NetContext;
import com.raywenderlich.alltherages.networks.jsonmodels.OrderResponseJson;
import com.raywenderlich.alltherages.networks.jsonmodels.RageResponseJson;
import com.raywenderlich.alltherages.networks.services.OrderServices;
import com.raywenderlich.alltherages.networks.services.RageService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by laptopTCC on 6/8/2017.
 */

public class RequestServerManager {
    public static String TAG = RequestServerManager.class.toString();
    public static RequestServerManager instance = new RequestServerManager();
    private RageService rageManager = NetContext.instance.create(RageService.class);
    private OrderServices orderManager = NetContext.instance.create(OrderServices.class);
    public GetRageListener rageListener;

    public interface GetRageListener{
        void onGetAllRage(boolean ok);
    };

    public GetRageListener getRageListener() {
        return rageListener;
    }

    public void setRageListener(GetRageListener rageListener) {
        this.rageListener = rageListener;
    }

    public RequestServerManager(){

    }
//==========================================RageManager===============================================//
    //1.Get all rage
    public boolean getRageFromServer(){
        rageManager.getRages().enqueue(new Callback<List<RageResponseJson>>() {
            @Override
            public void onResponse(Call<List<RageResponseJson>> call, Response<List<RageResponseJson>> response) {
                Log.d(TAG,"onResponse: getAllRage"+response.code());
                if(response.body() != null){
                    for(RageResponseJson r: response.body()){
                        DBContext.instance.addOrUpdate(new RageComic(
                                r.getId().getOid(),
                                r.getName(),
                                r.getUrl(),
                                r.getDescription(),
                                r.getOld_price(),
                                r.getNew_price(),
                                r.getDiscount_rate(),
                                r.getIs_favorite()
                        ));
                    }
                    Log.d(TAG,String.format("getAllRageComicFromServer: "));
                    getRageListener().onGetAllRage(true);
                }
            }

            @Override
            public void onFailure(Call<List<RageResponseJson>> call, Throwable t) {
                Log.d(TAG, String.format("onFailure: get all task %s", t.getCause()));
                getRageListener().onGetAllRage(false);
            }
        });
        return true;
    }

    //2.add newRage
    public void addNewRage(final RageComic r){

        RageResponseJson rageComic;
        rageComic = new RageResponseJson(
                r.getName(),
                r.getUrl(),
                r.getDescription(),
                r.getOld_price(),
                r.getNew_price(),
                r.getDiscount_rate(),
                r.getIs_favorite()
        );

        rageManager.addNewRage(rageComic).enqueue(new Callback<RageResponseJson>() {
            @Override
            public void onResponse(Call<RageResponseJson> call, Response<RageResponseJson> response) {
                if(response.code() == 200){
                    Log.d(TAG,String.format("Add new RageComic Success: ResponseCode: %s, Rage: %s",response.code(),
                            response.body().toString()));
                } else {
                    Log.d(TAG,String.format("Could not post RageComic: ResponseCode: %s", response.code()));
                }
            }

            @Override
            public void onFailure(Call<RageResponseJson> call, Throwable t) {
                Log.d(TAG,"Fail to post new RageComic");
            }
        });
    }

    //==========================================OrderManager===============================================//
    //3.Post new order
    public void postNewOrder(final OrderRequest orderRequest){
        //final OrderResponseJson orderResponseJson;
        orderManager.postNewOrder(orderRequest).enqueue(new Callback<OrderResponseJson>() {
            @Override
            public void onResponse(Call<OrderResponseJson> call, Response<OrderResponseJson> response) {
                Log.d(TAG,String.format("postNewOrder: %s", response.code()));
                if (response.code() == 200){
                    Log.d(TAG,String.format("Post new Order success: ResponseCode: %s, OrderResponseJson: %s",response.code(),
                            response.body().toString()));
                } else {
                    Log.d(TAG,String.format("Could not post OrderResponseJson: ResponseCode: %s", response.code()));
                }
            }

            @Override
            public void onFailure(Call<OrderResponseJson> call, Throwable t) {
                Log.d(TAG,"Fail to post new Order");
            }
        });
    }
}
