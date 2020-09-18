package com.deepsea.mua.stub.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;

/**
 * Created by JUN on 2019/8/23
 */
public class IConnectivityMonitorFactory implements ConnectivityMonitorFactory {
    private static final String TAG = "ConnectivityMonitor";
    private static final String NETWORK_PERMISSION = "android.permission.ACCESS_NETWORK_STATE";

    @NonNull
    @Override
    public ConnectivityMonitor build(
            @NonNull Context context,
            @NonNull ConnectivityMonitor.ConnectivityListener listener) {
//        int permissionResult = ContextCompat.checkSelfPermission(context, NETWORK_PERMISSION);
//        boolean hasPermission = permissionResult == PackageManager.PERMISSION_GRANTED;
//        if (Log.isLoggable(TAG, Log.DEBUG)) {
//            Log.d(
//                    TAG,
//                    hasPermission
//                            ? "ACCESS_NETWORK_STATE permission granted, registering connectivity monitor"
//                            : "ACCESS_NETWORK_STATE permission missing, cannot register connectivity monitor");
//        }
        return new NullConnectivityMonitor();
    }
}
