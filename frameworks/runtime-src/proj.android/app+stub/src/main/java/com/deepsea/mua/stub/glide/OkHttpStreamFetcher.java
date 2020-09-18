package com.deepsea.mua.stub.glide;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.Synthetic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Fetches an {@link InputStream} using the okhttp library.
 *
 * @deprecated replaced with com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher.
 */
@Deprecated
public class OkHttpStreamFetcher implements DataFetcher<InputStream> {
    private static final String TAG = "OkHttpFetcher";
    private final OkHttpClient client;
    private final GlideUrl url;

    @SuppressWarnings("WeakerAccess")
    @Synthetic
    InputStream stream;

    @SuppressWarnings("WeakerAccess")
    @Synthetic
    ResponseBody responseBody;

    // Public API.
    @SuppressWarnings("WeakerAccess")
    public OkHttpStreamFetcher(OkHttpClient client, GlideUrl url) {
        this.client = client;
        this.url = url;
    }

    @Override
    public void loadData(
            @NonNull Priority priority, @NonNull final DataCallback<? super InputStream> callback) {
        Request.Builder requestBuilder = new Request.Builder().url(url.toStringUrl());
        for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
            String key = headerEntry.getKey();
            requestBuilder.addHeader(key, headerEntry.getValue());
        }
        Request request = requestBuilder.build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
                            Log.d(TAG, "OkHttp failed to obtain result", e);
                        }
                        callback.onLoadFailed(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        responseBody = response.body();
                        if (response.isSuccessful()) {
                            long contentLength = responseBody.contentLength();
                            stream =
                                    ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength);
                            callback.onDataReady(stream);
                        } else {
                            callback.onLoadFailed(new HttpException(response.message(), response.code()));
                        }
                    }
                });
    }

    @Override
    public void cleanup() {
        try {
            if (stream != null) {
                stream.close();
            }
            
            if (responseBody != null) {
                responseBody.close();
            }
        } catch (IOException e) {
            // Ignored
        }
    }

    @Override
    public void cancel() {
        // TODO: call cancel on the client when this method is called on a background thread. See #257
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
