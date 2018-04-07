package com.raywenderlich.alltherages.networks;

import android.util.Log;

import com.raywenderlich.alltherages.utils.SharedPref;
import com.raywenderlich.alltherages.utils.Utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by laptopTCC on 6/4/2017.
 */

public class NetContext {
    private Retrofit retrofit;
    public static final NetContext instance = new NetContext();
    public NetContext(){
        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new LoggerInterceptor())
                .addInterceptor(new HeaderInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://akana.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private class LoggerInterceptor implements Interceptor {
        private static final String TAG = "LoggerInterceptor";
        @Override
        public Response intercept(Chain chain) throws IOException {
            //get request
            Request request = chain.request();
            //process request (print out)
            Log.d(TAG,String.format("url: %s", request.toString()));

            Headers headers = request.headers();
            if (headers != null){
                Log.d(TAG, String.format("headers: %s", headers.toString()));
            }
            //proceed
            Response response = chain.proceed(request);
            //process response
            Log.d(TAG,String.format("response: %s", response.toString()));
            Log.d(TAG,String.format("response body: %s", getResponseString(response)));
            return response;
        }
    }

    private String getResponseString(Response response) {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE);
        } catch (IOException e){
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();
        return buffer.clone().readString(Charset.forName("UTF-8"));
    }

    private class HeaderInterceptor implements Interceptor {
        private final String TAG = HeaderInterceptor.class.getSimpleName();
        @Override
        public Response intercept(Chain chain) throws IOException {
            String token = SharedPref.instance.getToken();
            Log.d(TAG,String.format("Token: %s",token));
            if(token != null){
                token = "";
            }
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization",String.format("JWT %s", Utils.getCodeRegister(token)))// nếu token này có thì e code cho tự gắn vào
                    .build();
            Log.e(TAG,String.format("intercept: %s", request.headers().toString()));
            return chain.proceed(request);
        }
    }

    public <T> T create(Class<T> classz){return retrofit.create(classz);}
}
