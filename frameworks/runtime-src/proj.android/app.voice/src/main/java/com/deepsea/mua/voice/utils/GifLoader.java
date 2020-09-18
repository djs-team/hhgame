package com.deepsea.mua.voice.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by JUN on 2019/4/27
 */
public class GifLoader {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private OnLoadListener mListener;

    public void loadImage(String url, ImageView target) {
        RequestOptions options = GlideUtils.options(0, 0);

        Glide.with(target.getContext())
                .asGif()
                .load(url)
                .apply(options)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                LogUtils.e(e);
                if (mListener != null) {
                    mListener.onError(e == null ? "" : e.getMessage());
                }
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                try {
                    Field gifStateField = GifDrawable.class.getDeclaredField("state");
                    gifStateField.setAccessible(true);
                    Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
                    Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                    gifFrameLoaderField.setAccessible(true);
                    Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
                    Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
                    gifDecoderField.setAccessible(true);
                    Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
                    Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                    Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
                    getDelayMethod.setAccessible(true);
                    //设置只播放一次
                    resource.setLoopCount(1);
                    //获得总帧数
                    int count = resource.getFrameCount();
                    int duration = 0;
                    for (int i = 0; i < count; i++) {
                        //计算每一帧所需要的时间进行累加
                        duration += (int) getDelayMethod.invoke(gifDecoder, i);
                    }
                    //发送延时消息，通知动画结束
                    LogUtils.i("onFinished", duration + "");

                    int finalDuration = duration;
                    mHandler.postDelayed(() -> {
                        if (mListener != null) {
                            mListener.onFinished(finalDuration);
                        }
                    }, duration);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return false;
            }
        })
                .transform(new CircleCrop())
                .into(target);
    }

    public void loadImage(int id, ImageView target) {
        RequestOptions options = GlideUtils.options(0, 0);

        Glide.with(target.getContext())
                .asGif()
                .load(id)
                .apply(options)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        LogUtils.e(e);
                        if (mListener != null) {
                            mListener.onError(e == null ? "" : e.getMessage());
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        try {
                            Field gifStateField = GifDrawable.class.getDeclaredField("state");
                            gifStateField.setAccessible(true);
                            Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
                            Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                            gifFrameLoaderField.setAccessible(true);
                            Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
                            Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
                            gifDecoderField.setAccessible(true);
                            Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
                            Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                            Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
                            getDelayMethod.setAccessible(true);
                            //设置只播放一次
                            resource.setLoopCount(1);
                            //获得总帧数
                            int count = resource.getFrameCount();
                            int duration = 0;
                            for (int i = 0; i < count; i++) {
                                //计算每一帧所需要的时间进行累加
                                duration += (int) getDelayMethod.invoke(gifDecoder, i);
                            }
                            //发送延时消息，通知动画结束
                            LogUtils.i("onFinished", duration + "");

                            int finalDuration = duration;
                            mHandler.postDelayed(() -> {
                                if (mListener != null) {
                                    mListener.onFinished(finalDuration);
                                }
                            }, duration);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                })
                .transform(new CircleCrop())
                .into(target);
    }

    public void removeCallbacksAndMessages() {
        mHandler.removeCallbacksAndMessages(null);
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.mListener = listener;
    }

    public interface OnLoadListener {
        /**
         * gif动画结束
         *
         * @param duration gif动画时长
         */
        void onFinished(int duration);

        void onError(String msg);
    }
}
