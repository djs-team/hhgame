package com.deepsea.mua.stub.network.download;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by JUN on 2019/7/4
 */
public interface DownloadService {

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url);
}
