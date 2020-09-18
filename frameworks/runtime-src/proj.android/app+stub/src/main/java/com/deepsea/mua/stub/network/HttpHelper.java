package com.deepsea.mua.stub.network;

import com.deepsea.mua.core.log.Logg;
import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.utils.IntTypeAdapter;
import com.deepsea.mua.stub.utils.SSLUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by JUN on 2018/9/19
 */
public class HttpHelper {

    private static volatile HttpHelper mInstance;
    private Map<String, Object> mServiceMap;

    private HttpHelper() {
        mServiceMap = new ConcurrentHashMap<>();
    }

    public static HttpHelper instance() {
        if (mInstance == null) {
            synchronized (HttpHelper.class) {
                if (mInstance == null) {
                    mInstance = new HttpHelper();
                }
            }
        }
        return mInstance;
    }

    public <T> T getApi(Class<T> clazz) {
        if (mServiceMap.containsKey(clazz.getName())) {
            return (T) mServiceMap.get(clazz.getName());
        } else {
            Object obj = prepare(clazz);
            mServiceMap.put(clazz.getName(), obj);
            return (T) obj;
        }
    }

    private <T> T prepare(Class<T> clazz) {
        //声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> {
            Logg.d("okhttp", message);
        });
        //设定日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //自定义OkHttpClient
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(HttpHeaderInterceptor.headerInterceptor())
                .addInterceptor(HttpInterceptor.httpInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(HttpConst.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(HttpConst.WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(HttpConst.READ_TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        String baseUrl = AddressCenter.getAddress().getHostUrl();
        if (baseUrl.startsWith("https://") && getSSLSocketFactory() != null) {
            final SSLContext sslcontext;
            try {
                sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);
                okHttpClient.sslSocketFactory(sslcontext.getSocketFactory());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(int.class, new IntTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntTypeAdapter()).create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//暂时先这么加 版本冲突
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(okHttpClient.build())
                .build()
                .create(clazz);
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            return SSLUtils.getSSLSocketFactory(AppUtils.getApp().getApplicationContext().getAssets().open("hehe.crt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static TrustManager myX509TrustManager = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType)
                throws CertificateException {
        }

        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType)
                throws CertificateException {
        }
    };



}
