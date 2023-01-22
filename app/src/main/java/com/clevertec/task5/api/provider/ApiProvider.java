package com.clevertec.task5.api.provider;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvider {
    private final String baseUrl;

    public ApiProvider(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Retrofit getRetrofit() {
        return new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
