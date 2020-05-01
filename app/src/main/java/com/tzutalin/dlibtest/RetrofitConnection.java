package com.tzutalin.dlibtest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnection {

    Retrofit retrofit;
    RetrofitInterface server;

    public void setRetrofit(String url){
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        server = retrofit.create(RetrofitInterface.class);

    }

}