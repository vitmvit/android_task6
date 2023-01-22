package com.clevertec.task5.api.provider;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;

import static com.clevertec.task5.constants.Constants.BASE_URL;

public class ApiProvider {

    @Inject
    public ApiProvider() {
    }

    @Inject
    public Retrofit getRetrofit() {
        return new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
