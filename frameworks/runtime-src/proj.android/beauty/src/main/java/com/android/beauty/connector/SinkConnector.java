package com.android.beauty.connector;

public interface SinkConnector<T> {
    int onDataAvailable(T data);
}
