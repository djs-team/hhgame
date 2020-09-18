package com.deepsea.mua.stub.utils.span;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.stub.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by JUN on 2019/4/29
 */
public class UrlImageSpan extends CenterImageSpan {

    private static final String TAG = "UrlImageSpan";

    private String mUrl;
    private WeakReference<TextView> mWeakReference;

    private boolean isLoading;
    private boolean isLoaded;

    public UrlImageSpan(Context context, @NonNull TextView target, String url) {
        super(context, R.drawable.url_span_place, 3);
        this.mUrl = url;
        this.mWeakReference = new WeakReference<>(target);
    }

    @Override
    public Drawable getDrawable() {
        if (!isLoaded && !isLoading) {
            loadImage();
        }
        return super.getDrawable();
    }

    private void loadImage() {
        isLoading = true;

        if (mWeakReference.get() == null)
            return;

        RequestOptions options = GlideUtils.options(0, R.drawable.url_span_place);

        Glide.with(mWeakReference.get().getContext().getApplicationContext())
                .asBitmap()
                .load(mUrl)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Log.d(TAG, "onResourceReady: " + mUrl);

                        TextView targetView = mWeakReference.get();
                        if (targetView != null) {

                            Resources resources = targetView.getContext().getResources();
//                        Bitmap zoom = zoom(resource, targetWidth);
                            BitmapDrawable b = new BitmapDrawable(resources, resource);
                            int i = ResUtils.dp2px(targetView.getContext(), 25);
                            b.setBounds(0, 0, i, i);

                            try {
                                Field mDrawable = ImageSpan.class.getDeclaredField("mDrawable");
                                mDrawable.setAccessible(true);
                                mDrawable.set(UrlImageSpan.this, b);

                                Field mDrawableRef = DynamicDrawableSpan.class.getDeclaredField("mDrawableRef");
                                mDrawableRef.setAccessible(true);
                                mDrawableRef.set(UrlImageSpan.this, null);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            }

                            isLoaded = true;
                            isLoading = false;
                            targetView.setText(targetView.getText());

                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Log.d(TAG, "onLoadFailed: " + errorDrawable + " " + mUrl);
                        isLoaded = true;
                        isLoading = false;
                    }
                });
    }

    /**
     * 按宽度缩放图片
     *
     * @param bmp  需要缩放的图片源
     * @param newW 需要缩放成的图片宽度
     * @return 缩放后的图片
     */
    public static Bitmap zoom(@NonNull Bitmap bmp, int newW) {

        // 获得图片的宽高
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        // 计算缩放比例
        float scale = ((float) newW) / width;

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

        return newbm;
    }
}
