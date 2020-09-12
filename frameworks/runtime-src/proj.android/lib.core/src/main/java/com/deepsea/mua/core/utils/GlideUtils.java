package com.deepsea.mua.core.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by JUN on 2019/3/25
 */
public class GlideUtils {

    /**
     * 加载图片
     *
     * @param view
     * @param url
     * @param placeRes
     * @param errRes
     */
    public static void loadImage(ImageView view, String url, int placeRes, int errRes) {
        Glide.with(view.getContext().getApplicationContext())
                .load(url)
                .apply(options(placeRes, errRes))
                .into(view);
    }

    /**
     * 加载图片
     *
     * @param view
     * @param url
     */
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext().getApplicationContext())
                .load(url)
                .apply(options(0, 0))
                .into(view);
    }

    /**
     * 加载图片
     *
     * @param view
     * @param path
     * @param placeId
     * @param errorId
     */
    public static void loadFromFile(ImageView view, String path, int placeId, int errorId) {
        Glide.with(view.getContext().getApplicationContext())
                .load(Uri.parse("file://" + path))
                .apply(options(placeId, errorId))
                .into(view);
    }

    /**
     * 加载缩略图
     *
     * @param view
     * @param url
     * @param placeId
     * @param errorId
     * @param sizeMultiplier
     */
    public static void loadThumbnail(ImageView view, String url, int placeId, int errorId, float sizeMultiplier) {
        Glide.with(view.getContext().getApplicationContext())
                .load(url)
                .apply(options(placeId, errorId))
                .thumbnail(sizeMultiplier)
                .into(view);
    }

    /**
     * 加载gif
     *
     * @param view
     * @param url
     * @param placeId
     * @param errorId
     */
    public static void loadGif(ImageView view, String url, int placeId, int errorId) {
        Glide.with(view.getContext().getApplicationContext())
                .asGif()
                .load(url)
                .apply(options(placeId, errorId))
                .into(view);
    }

    /**
     * 加载圆图gif
     *
     * @param view
     * @param url
     * @param placeId
     * @param errorId
     */
    public static void loadCircleGif(ImageView view, String url, int placeId, int errorId) {
        Glide.with(view.getContext().getApplicationContext())
                .asGif()
                .load(url)
                .apply(options(placeId, errorId))
                .transform(new CircleCrop())
                .into(view);
    }

    /**
     * 加载本地gif
     *
     * @param view
     * @param resId
     */
    public static void loadGif(ImageView view, int resId) {
        Glide.with(view.getContext().getApplicationContext())
                .asGif()
                .load(resId)
                .into(view);
    }
//    public static void loadGif1(ImageView view, int resId) {
//        Glide.with(view.getContext())
//                .asGif()
//                .load(resId)
////                .override(width,height)
////                .placeholder(placeholderImg)
////                .error(errorImg)
//                .dontAnimate()
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).
////                .into(new RequestListener<>()));
//    }

    /**
     * 加载本地图片
     *
     * @param view
     * @param resId
     */
    public static void loadRes(ImageView view, int resId) {
        Glide.with(view.getContext().getApplicationContext())
                .load(resId)
                .into(view);
    }

    /**
     * 兼容ImageView scaleType 为 CENTER_CROP
     *
     * @param view
     * @param dp
     * @return
     */
    private static BitmapTransformation[] roundTransform(ImageView view, int dp) {
        int radius = ResUtils.dp2px(view.getContext(), dp);
        BitmapTransformation[] transforms = null;
        if (view.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
            transforms = new BitmapTransformation[]{new CenterCrop(), new RoundedCorners(radius)};
            return transforms;
        } else {
            transforms = new BitmapTransformation[]{new RoundedCorners(radius)};
            return transforms;
        }
    }

    /**
     * 加载圆角图片 （圆角默认4dp）
     *
     * @param view
     * @param url
     * @param placeId
     * @param errorId
     */
    public static void roundImage(ImageView view, String url, int placeId, int errorId) {
        Glide.with(view.getContext().getApplicationContext())
                .load(url)
                .apply(options(placeId, errorId))
                .transform(roundTransform(view, 4))
                .into(view);
    }


    /**
     * 加载圆角图片 （圆角默认4dp）
     *
     * @param view
     * @param url
     * @param placeId
     * @param errorId
     */
    public static void roundImageWithBorder(ImageView view, String url, int border, int borderColor, int placeId, int errorId) {
        Glide.with(view.getContext().getApplicationContext())
                .load(url)
                .apply(options(placeId, errorId))
                .transform(new GlideCircleWithBorder(border, borderColor)).into(view);
    }

    /**
     * 加载圆角图片
     *
     * @param view
     * @param url
     * @param placeId
     * @param errorId
     * @param dp      圆角
     */
    public static void roundImage(ImageView view, String url, int placeId, int errorId, int dp) {
        Glide.with(view.getContext().getApplicationContext())
                .load(url)
                .apply(options(placeId, errorId))
                .transform(roundTransform(view, dp))
                .into(view);
    }


    public static void setRoundImage(ImageView image, String url, int placeId, int errorId, int dp) {
        Glide.with(image.getContext().getApplicationContext())
                .load(url)
                .apply(options(placeId, errorId))
                .transform(roundTransform(image, dp))
                .into(image);

    }


    /**
     * 加载圆角图片
     *
     * @param view
     * @param path
     * @param placeId
     * @param errorId
     * @param dp
     */
    public static void roundImageByFile(ImageView view, String path, int placeId, int errorId, int dp) {
        Glide.with(view.getContext().getApplicationContext())
                .load(Uri.parse("file://" + path))
                .apply(options(placeId, errorId))
                .transform(roundTransform(view, dp))
                .into(view);
    }

    /**
     * 加载圆图
     *
     * @param view
     * @param url
     * @param placeId
     * @param errorId
     */
    public static void circleImage(ImageView view, String url, int placeId, int errorId) {
        Glide.with(view.getContext().getApplicationContext())
                .load(url)
                .apply(options(placeId, errorId))
                .transform(new CircleCrop())
                .into(view);
    }

    /**
     * 加载圆图
     *
     * @param view
     * @param path
     * @param placeId
     * @param errorId
     */
    public static void circleImageByFile(ImageView view, String path, int placeId, int errorId) {
        Glide.with(view.getContext().getApplicationContext())
                .load(Uri.parse("file://" + path))
                .apply(options(placeId, errorId))
                .transform(new CircleCrop())
                .into(view);
    }

    public static void loadImageByFile(ImageView view, String path, int placeId, int errorId) {
        Glide.with(view.getContext().getApplicationContext())
                .load(Uri.parse("file://" + path))
                .apply(options(placeId, errorId))
                .into(view);
    }


    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param placeId
     * @param errorId
     * @param callback
     */
    public static void loadImage(Context context, String url, int placeId, int errorId, SimpleTarget<Drawable> callback) {
        Glide.with(context)
                .load(url)
                .apply(options(placeId, errorId))
                .into(callback);
    }

    public static RequestOptions options(int placeId, int errorId) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(placeId)
                .error(errorId);
        return requestOptions;
    }

    /**
     * 清理磁盘缓存，需在子线程中执行
     *
     * @param context
     */
    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    /**
     * 清理内存缓存
     *
     * @param context
     */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 自己写的加载网络图片的方法
     * img_url 图片的网址
     */
    public interface GlideLoadBitmapCallback {
        void getBitmapCallback(Bitmap bitmap);
    }

    public static void getBitmap(final String imgUrl, final Activity context, final GlideLoadBitmapCallback callback) {

        new Thread() {
            public void run() {
                try {
                    Bitmap myBitmap = Glide.with(context)
                            .asBitmap()
                            .load(imgUrl)
                            .submit(600, 480).get();
                    Bitmap bitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
                    callback.getBitmapCallback(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }.start();
    }

}
