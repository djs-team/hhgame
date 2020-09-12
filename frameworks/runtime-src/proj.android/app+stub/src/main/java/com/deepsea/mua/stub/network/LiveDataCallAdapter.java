/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.deepsea.mua.stub.network;


import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.response.ApiResponse;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 *
 * @param <R>
 */
public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ApiResponse<R>>> {

    private final Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<ApiResponse<R>> adapt(Call<R> call) {
        return new LiveData<ApiResponse<R>>() {
            AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            if (response.isSuccessful()) {
                                if (ResultValid.codeValid(response)) {
                                    postValue(new ApiResponse<>(response, ResultValid.code(response)));
                                } else {
                                    postValue(new ApiResponse<>(ResultValid.message(response), ResultValid.code(response)));
                                }
                            } else {
                                onFailure(null, null);
                            }
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable throwable) {
                            if (throwable instanceof JsonParseException || throwable instanceof JSONException || throwable instanceof ParseException) {
                                onError("数据解析错误");
                            } else if (throwable instanceof SocketTimeoutException) {
                                onError("网络连接超时");
                            } else {
                                onError(HttpConst.SERVER_ERROR);
                            }
                        }

                        private void onError(String msg) {
                            postValue(new ApiResponse<>(msg, HttpConst.HTTP_ERROR_CODE));
                        }
                    });
                }
            }
        };
    }
}
