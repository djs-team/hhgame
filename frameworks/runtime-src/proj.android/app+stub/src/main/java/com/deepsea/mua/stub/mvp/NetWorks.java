//package com.deepsea.mua.mvp;
//
//
//import com.deepsea.mua.api.DynamicApi;
//import com.deepsea.mua.core.utils.LogUtils;
//
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
///**
// * author : liyaxing
// * date   : 2019/3/23 10:39
// * desc   : Retrofit
// */
//public class NetWorks {
//
//    private Retrofit retrofit;
//    private DynamicApi mDynamicApi;
//    private static NetWorks netWorks;
//    private static final int TIMEOUT = 20;// 超时时间-s
//
//
//
//    public static NetWorks getInstance() {
//        if (netWorks == null) {
//            netWorks = new NetWorks();
//        }
//        return netWorks;
//    }
//
//    private NetWorks() {
//        this.retrofit = new Retrofit.Builder()
//                .baseUrl("https://mtestapi.57xun.com/")
//                .client(configClient())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//
//        this.mDynamicApi = retrofit.create(DynamicApi.class);
//
//
//    }
//
//    // 简单单例 根据功能设置不同api
////    public DynamicApi getBaseApi() {
////        return baseApi == null ? configRetrofit(DynamicApi.class,"dasdasddsa") : baseApi;
////    }
//
//
//    private <T> T configRetrofit(Class<T> api, String baseUrl) {
//        retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .client(configClient())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//        return retrofit.create(api);
//    }
//
//
//    private OkHttpClient configClient() {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
////        builder.addInterceptor(new AddQueryParameterInterceptor());
////        if (BuildConfig.BASE_URL.contains("dev")||BuildConfig.BASE_URL.contains("test")) {
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
//            LogUtils.i("JSon ==>", message);
//        });
//        //打印retrofit日志
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(loggingInterceptor);
////        }
//        builder.retryOnConnectionFailure(true);
//        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
//                .writeTimeout(TIMEOUT, TimeUnit.SECONDS);
//        return builder.build();
//    }
//    public String s;
//
//    public DynamicApi getDynamicApi() {
//        return mDynamicApi;
//    }
//
//}
