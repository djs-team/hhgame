package com.deepsea.mua.stub.network.download;

import android.support.annotation.NonNull;

import com.deepsea.mua.stub.apiaddress.AddressCenter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by JUN on 2019/7/4
 */
public class DownloadUtils {

    public static void download(String url, final String path, final DownloadListener downloadListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AddressCenter.getAddress().getHostUrl())
                //通过线程池获取一个线程，指定callback在子线程中运行。
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build();

        DownloadService service = retrofit.create(DownloadService.class);

        Call<ResponseBody> call = service.download(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                //将Response写入到从磁盘中
                //注意，这个方法是运行在子线程中的
                if (response.isSuccessful()) {
                    writeResponseToDisk(path, response, downloadListener);
                } else {
                    String error = "下载失败";
                    if (response.errorBody() != null) {
                        try {
                            error = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (downloadListener != null) {
                        downloadListener.onFail(error);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                if (downloadListener != null) {
                    downloadListener.onFail(throwable.getMessage());
                }
            }
        });
    }

    private static void writeResponseToDisk(String path, Response<ResponseBody> response, DownloadListener downloadListener) {
        //从response获取输入流以及总大小
        writeFileFromIS(new File(path), response.body().byteStream(), response.body().contentLength(), downloadListener);
    }

    private static int sBufferSize = 8192;

    //将输入流写入文件
    private static void writeFileFromIS(File file, InputStream is, long totalLength, DownloadListener downloadListener) {
        //开始下载
        if (downloadListener != null) {
            downloadListener.onStart();
        }

        //创建文件
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                if (downloadListener != null) {
                    downloadListener.onFail("createNewFile IOException");
                }
            }
        }

        OutputStream os = null;
        long currentLength = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[sBufferSize];
            int len;
            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                os.write(data, 0, len);
                currentLength += len;
                //计算当前下载进度
                if (downloadListener != null) {
                    downloadListener.onProgress(totalLength, currentLength);
                }
            }
            //下载完成，并返回保存的文件路径
            if (downloadListener != null) {
                downloadListener.onFinish(file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (downloadListener != null) {
                downloadListener.onFail("IOException");
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
