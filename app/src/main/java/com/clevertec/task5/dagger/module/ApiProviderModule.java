package com.clevertec.task5.dagger.module;

import com.clevertec.task5.api.provider.ApiProvider;
import dagger.Module;
import dagger.Provides;

@Module
public class ApiProviderModule {

    @Provides
    ApiProvider providesApiProvider(String baseUrl) {
        return new ApiProvider(baseUrl);
    }
}
