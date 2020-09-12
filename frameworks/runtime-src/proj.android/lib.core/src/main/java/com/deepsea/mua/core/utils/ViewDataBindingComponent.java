package com.deepsea.mua.core.utils;

import android.databinding.DataBindingComponent;

/**
 * Created by JUN on 2019/3/25
 */
public class ViewDataBindingComponent implements DataBindingComponent {

    private final ViewDataBindingAdapter mViewDataBindingAdapter;

    public ViewDataBindingComponent() {
        mViewDataBindingAdapter = new ViewDataBindingAdapter();
    }

    public ViewDataBindingAdapter getViewDataBindingAdapter() {
        return mViewDataBindingAdapter;
    }
}
