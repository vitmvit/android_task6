package com.clevertec.task5.component;

import com.clevertec.task5.api.provider.ApiProvider;
import dagger.Component;

@Component
public interface ApiComponent {

    ApiProvider getApiProvider();
}
