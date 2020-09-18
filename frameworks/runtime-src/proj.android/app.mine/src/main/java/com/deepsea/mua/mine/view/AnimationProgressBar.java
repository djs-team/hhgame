package com.deepsea.mua.mine.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by JUN on 2019/6/27
 */
public class AnimationProgressBar extends ProgressBar {

    public interface OnProgressUpdatedListener {
        void progressUpdate(int progress);
    }

    private OnProgressUpdatedListener onProgressUpdatedListener = null;

    public AnimationProgressBar(Context context) {
        this(context, null);
    }

    public AnimationProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnProgressUpdatedListener(OnProgressUpdatedListener onProgressUpdatedListener) {
        this.onProgressUpdatedListener = onProgressUpdatedListener;
    }

    public synchronized void setAnimationProgress(int progress) {
        ObjectAnimator _secondary = null;
        ObjectAnimator _progress = null;

        if (getProgress() <= progress) {
            _secondary = ObjectAnimator.ofInt(this, "secondaryProgress", getSecondaryProgress(), progress).setDuration(300);
            _progress = ObjectAnimator.ofInt(this, "progress", getProgress(), progress).setDuration(1300);
            appendListener(_secondary);
        } else {
            _secondary = ObjectAnimator.ofInt(this, "secondaryProgress", getSecondaryProgress(), progress).setDuration(1300);
            _progress = ObjectAnimator.ofInt(this, "progress", getProgress(), progress).setDuration(300);
            appendListener(_progress);
        }
        _secondary.start();
        _progress.start();
    }

    private void appendListener(ObjectAnimator progress) {
        if (onProgressUpdatedListener != null) {
            progress.addUpdateListener(animation -> {
                try {
                    onProgressUpdatedListener.progressUpdate((int) animation.getAnimatedValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
