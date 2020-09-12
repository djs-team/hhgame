package com.deepsea.mua.stub.utils;

import android.arch.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

/**
 * Created by JUN on 2019/4/14
 */
public class DisposeUtils {

    public static <T> AutoDisposeConverter<T> autoDisposable(LifecycleOwner lifecycleOwner) {
        return AutoDispose.autoDisposable(
                AndroidLifecycleScopeProvider.from(lifecycleOwner));
    }
}
