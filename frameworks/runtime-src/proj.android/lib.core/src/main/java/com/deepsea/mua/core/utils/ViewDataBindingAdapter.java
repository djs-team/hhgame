package com.deepsea.mua.core.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

/**
 * Created by JUN on 2019/3/25
 */
public class ViewDataBindingAdapter {

    /**
     * 加载图片
     *
     * @param view
     * @param imgUrl
     * @param placeRes
     * @param errRes
     * @param sizeMultiplier
     * @param round
     */
    @BindingAdapter(value = {"imgUrl", "placeRes", "errRes", "sizeMultiplier", "round"}, requireAll = false)
    public void loadImage(ImageView view, String imgUrl, Drawable placeRes, Drawable errRes, float sizeMultiplier, int round) {
        Glide.with(view.getContext())
                .load(imgUrl)
                .placeholder(placeRes)
                .error(errRes)
                .thumbnail(sizeMultiplier)
                .transform(new RoundedCorners(round))
                .into(view);

    }
}
