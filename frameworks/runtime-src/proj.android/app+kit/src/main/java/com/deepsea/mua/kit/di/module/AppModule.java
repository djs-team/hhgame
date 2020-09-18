package com.deepsea.mua.kit.di.module;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.network.HttpHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by JUN on 2019/3/22
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    RetrofitApi provideRetrofitApi() {
        return HttpHelper.instance().getApi(RetrofitApi.class);
    }
}
