package com.deepsea.mua.lib.svga.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.lib.svga.R;
import com.deepsea.mua.lib.svga.databinding.DialogShowgiftAnimationBinding;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by JUN on 2018/9/27
 */
public class ShowGiftAnimationDialog extends BaseDialog<DialogShowgiftAnimationBinding> {
    private Handler mHandler;

    public ShowGiftAnimationDialog(@NonNull Context context, Handler mHandler) {
        super(context);
        this.mHandler = mHandler;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_showgift_animation;
    }

    @Override
    protected float getWidthPercent() {
        return 1F;
    }

    @Override
    protected float getDimAmount() {
        return 0.5F;
    }

    private RequestOptions options(int placeId, int errorId) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(placeId)
                .error(errorId);
        return requestOptions;
    }

    public void setGiftAnimation(String animation) {
        Glide.with(getContext()).load(animation).apply(options(R.drawable.ic_indicator, R.drawable.ic_indicator))
                .listener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
//                        // 计算动画时长
//                        if (resource instanceof GifDrawable) {
//
//                            GifDrawable gifDrawable=(GifDrawable) resource;
//                            GifDecoder gifDecoder=gifDrawable.getG
//
//                                    //加载一次
//                            ((GifDrawable) resource).setLoopCount(1);
//                        }
//                        return false;
                        try {
                            GifDrawable gifDrawable = (GifDrawable) resource;
                            long duration = 0;
                            // 计算动画时长
                            //GifDecoder decoder = gifDrawable.getDecoder();//4.0开始没有这个方法了
                            Drawable.ConstantState state = gifDrawable.getConstantState();
                            if (state != null) {
                                //不能混淆GifFrameLoader和GifState类
                                Object gifFrameLoader = getValue(state, "frameLoader");
                                if (gifFrameLoader != null) {
                                    Object decoder = getValue(gifFrameLoader, "gifDecoder");
                                    if (decoder != null && decoder instanceof GifDecoder) {
                                        for (int i = 0; i < gifDrawable.getFrameCount(); i++) {
                                            duration += ((GifDecoder) decoder).getDelay(i);
                                        }
                                        mHandler.sendEmptyMessageDelayed(1001,duration);
                                    }
                                }

                            }
                        } catch (Throwable e) {
                        }
                        return false;
                    }

                }).into(mBinding.ivGiftAnimation);

    }


    /*---------------------------------------------------------------------------------------------*/

    /**
     * 通过字段名从对象或对象的父类中得到字段的值
     *
     * @param object    对象实例
     * @param fieldName 字段名
     * @return 字段对应的值
     */
    public static Object getValue(Object object, String fieldName) throws Exception {
        if (object == null) {
            return null;
        }
        if (TextUtils.isEmpty(fieldName)) {
            return null;
        }
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }

        return null;
    }

}
