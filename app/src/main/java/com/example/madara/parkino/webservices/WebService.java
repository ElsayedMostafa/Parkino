package com.example.madara.parkino.webservices;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by madara on 2/22/18.
 */

public class WebService {
    private static WebService instance;
    private Api api;
    public WebService(){
        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Urls.MAIN_URL)
                .build();
        api = retrofit.create(Api.class);
    }
    public static WebService getInstance(){
        if(instance == null){
            instance = new WebService();

        }
        return instance;
    }
    public Api getApi(){
        return api;
    }
}
