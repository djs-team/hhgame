package com.deepsea.mua.stub.controller;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.view.floatwindow.FloatWindow;
import com.deepsea.mua.core.view.floatwindow.enums.MoveType;
import com.deepsea.mua.core.view.floatwindow.enums.Screen;
import com.deepsea.mua.core.view.floatwindow.interfaces.BaseFloatWindow;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.app.IActivityLifecycle;
import com.deepsea.mua.stub.databinding.LayoutRoomMiniBinding;
import com.deepsea.mua.stub.model.RoomModel;

/**
 * Created by JUN on 2019/4/29
 */
public class RoomMiniUtils extends IActivityLifecycle {

    private static final String TAG = "RoomMiniUtils";

    private int mOffsetY;

    /**
     * 创建悬浮窗
     */
    private void create(Context context) {
        if (FloatWindow.get(TAG) != null || RoomController.getInstance().getRoomModel() == null)
            return;

        RoomModel model = RoomController.getInstance().getRoomModel();
        LayoutRoomMiniBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_room_mini, null, false);
        GlideUtils.circleImage(binding.roomIv, model.getRoomData().getRoomOwnerHeadUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
//        loadImageAndAnim(iv, model.getRoomData().getRoomData().getRoomImage());
        binding.roomNameTv.setText(model.getRoomData().getRoomData().getRoomName());
        binding.roomIdTv.setText("ID: " + model.getRoomId());
        binding.getRoot().setOnClickListener(v -> {
            RoomJoinController controller = new RoomJoinController();
            controller.startJump(model.getRoomId(), ActivityCache.getInstance().getTopActivity());
        });
        binding.closeIv.setOnClickListener(v -> {
            RoomController.getInstance().release();
            destroy();
        });

        FloatWindow.Builder builder = FloatWindow.with(context.getApplicationContext())
                .setView(binding.getRoot())
                .setX(Screen.WIDTH, 0F)
                .setMoveType(MoveType.ACTIVE)
                .setDesktopShow(true)
                .setTag(TAG);

        if (mOffsetY > 0) {
            binding.getRoot().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int height = binding.getRoot().getMeasuredHeight();
            builder.setY(mOffsetY - height);
        } else {
            builder.setY(Screen.HEIGHT, 0.8F);
        }
        builder.build();
        addActivityLifecycle();
    }

    public void setOffsetY(int offsetY) {
        this.mOffsetY = offsetY;
    }

    private void loadImageAndAnim(ImageView roomIv, String url) {
        Glide.with(roomIv.getContext())
                .load(url)
                .apply(GlideUtils.options(0, R.drawable.ic_place))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        roomIv.startAnimation(getAnimation());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        roomIv.setImageDrawable(resource);
                        roomIv.startAnimation(getAnimation());
                        return false;
                    }
                })
                .transform(new CircleCrop())
                .into(roomIv);
    }

    private Animation getAnimation() {
        Animation anim = new RotateAnimation(0F, 360F, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        anim.setFillAfter(true);
        anim.setDuration(5000);
        anim.setRepeatCount(-1);
        anim.setInterpolator(new LinearInterpolator());

        return anim;
    }

    private void addActivityLifecycle() {
        ActivityCache.getInstance().addActivityLifecycle(this);
    }

    private void removeActivityLifecycle() {
        ActivityCache.getInstance().removeActivityLifecycle(this);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        show(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (!ActivityCache.getInstance().isAppOnForeground()) {
            hide();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (!ActivityCache.getInstance().isAppOnForeground()) {
            hide();
        }
    }

    /**
     * 显示悬浮窗
     */
    public void show(Context context) {
        if (FloatWindow.get(TAG) == null) {
            create(context);
        }
        if (FloatWindow.get(TAG) != null) {
            FloatWindow.get(TAG).show();
        }
    }

    /**
     * 隐藏悬浮窗
     */
    public void hide() {
        BaseFloatWindow window = FloatWindow.get(TAG);
        if (window != null) {
            window.hide();
        }
    }

    /**
     * 移除悬浮窗
     */
    public void destroy() {
        try {
            FloatWindow.destroy(TAG);
            removeActivityLifecycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        BaseFloatWindow window = FloatWindow.get(TAG);
        if (window != null) {
            return window.isShowing();
        }
        return false;
    }
}
