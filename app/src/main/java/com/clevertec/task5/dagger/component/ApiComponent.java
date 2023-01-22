package com.clevertec.task5.dagger.component;

import com.clevertec.task5.api.provider.ApiProvider;
import com.clevertec.task5.dagger.module.ApiProviderModule;
import dagger.Component;

@Component(modules = ApiProviderModule.class)
public interface ApiComponent {

    void inject(ApiProvider apiProvider);
}
