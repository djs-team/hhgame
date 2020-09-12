package com.deepsea.mua.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.deepsea.mua.core.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 水波纹特效
 * https://github.com/hackware1993/WaveView
 * Created by fbchen2 on 2016/5/25.
 */
public class WaveView extends View {

    //颜色
    private int mWaveColor;
    //初始半径
    private float mInitialRadius;
    //最大半径
    private float mMaxRadius;
    //一个波纹从创建到消失持续时间
    private float mDuration;

    private boolean mIsRunning;
    private List<Circle> mCircles = new ArrayList<>();

    private Interpolator mInterpolator = new LinearInterpolator();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        mWaveColor = ta.getColor(R.styleable.WaveView_waveColor, 0x66FFFFFF);
        mInitialRadius = ta.getDimensionPixelSize(R.styleable.WaveView_waveInitialRadius, 0);
        mMaxRadius = ta.getDimensionPixelSize(R.styleable.WaveView_waveMaxRadius, 0);
        mDuration = ta.getInt(R.styleable.WaveView_waveDuration, 2000);
        ta.recycle();
        setColor(mWaveColor);
    }

    public void setStyle(Paint.Style style) {
        mPaint.setStyle(style);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * 开始
     */
    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            newCircle();
        }
    }

    /**
     * 缓慢停止
     */
    public void stop() {
        stopImmediately();
    }

    /**
     * 立即停止
     */
    public void stopImmediately() {
        mIsRunning = false;
        mCircles.clear();
        invalidate();
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    protected void onDraw(Canvas canvas) {
        Iterator<Circle> iterator = mCircles.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            float radius = circle.getCurrentRadius();
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.setAlpha(circle.getAlpha());
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
            } else {
                iterator.remove();
            }
        }
        if (mCircles.size() > 0) {
            postInvalidateDelayed(10);
        }
    }

    public void newCircle() {
        Circle circle = new Circle();
        mCircles.add(circle);
        postInvalidateDelayed(10);
    }

    private class Circle {
        private long mCreateTime;

        private Circle() {
            mCreateTime = System.currentTimeMillis();
        }

        int getAlpha() {
            float percent = (getCurrentRadius() - mInitialRadius) / (mMaxRadius - mInitialRadius);
            return (int) (255 - mInterpolator.getInterpolation(percent) * 255);
        }

        float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0F / mDuration;
            return mInitialRadius + mInterpolator.getInterpolation(percent) * (mMaxRadius - mInitialRadius);
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
    }
}

