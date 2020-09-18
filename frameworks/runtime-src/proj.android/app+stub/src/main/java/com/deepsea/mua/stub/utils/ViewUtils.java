package com.deepsea.mua.stub.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JUN on 2019/5/29
 */
public class ViewUtils {

    /**
     * 设置view大小
     *
     * @param view
     * @param targetW
     * @param targetH
     */
    public static void setViewSize(View view, int targetW, int targetH) {
        view.post(() -> {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            int width = view.getWidth();
            lp.width = width;
            lp.height = width * targetH / targetW;
            view.setLayoutParams(lp);
        });
    }
}
