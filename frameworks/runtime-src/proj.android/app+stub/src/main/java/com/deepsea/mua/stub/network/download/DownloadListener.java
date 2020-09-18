package com.deepsea.mua.stub.network.download;

/**
 * Created by JUN on 2019/7/4
 */
public interface DownloadListener {

    //下载开始
    void onStart();

    //下载进度
    void onProgress(long total, long current);

    //下载完成
    void onFinish(String path);

    //下载失败
    void onFail(String error);
}
