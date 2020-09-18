package com.deepsea.mua.core.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.deepsea.mua.core.R;
import com.deepsea.mua.core.utils.ResUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by chaohx on 2017/9/19.
 */

public class VoicePlaying extends View {

    //画笔
    private Paint paint;

    //跳动指针的集合
    private List<Pointer> pointers;

    //跳动指针的数量
    private int pointerNum;

    //逻辑坐标 原点
    private float basePointX;
    private float basePointY;

    //指针间的间隙  默认5dp
    private float pointerPadding;

    //每个指针的宽度 默认5dp
    private float pointerWidth;

    //指针的颜色
    private int pointerColor;

    //控制开始/停止
    private boolean isPlaying = false;

    //指针波动速率
    private int pointerSpeed;

    private float mIndex;
    private RectF mPointerRect;

    public VoicePlaying(Context context) {
        this(context, null);
    }

    public VoicePlaying(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoicePlaying(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VoicePlaying);
        pointerColor = ta.getColor(R.styleable.VoicePlaying_pointer_color, Color.RED);
        //指针的数量，默认为4
        pointerNum = ta.getInt(R.styleable.VoicePlaying_pointer_num, 4);
        //指针的宽度，默认5dp
        pointerWidth = ta.getDimensionPixelSize(R.styleable.VoicePlaying_pointer_width, ResUtils.dp2px(getContext(), 5));
        //指针间隔
        pointerPadding = ta.getDimensionPixelSize(R.styleable.VoicePlaying_pointer_padding, ResUtils.dp2px(getContext(), 5));
        pointerSpeed = ta.getInt(R.styleable.VoicePlaying_pointer_speed, 50);
        ta.recycle();
        init();
    }

    /**
     * 初始化画笔与指针的集合
     */
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(pointerColor);
        pointers = new ArrayList<>();
        mPointerRect = new RectF();
    }


    /**
     * 在onLayout中做一些，宽高方面的初始化
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //获取逻辑原点的，也就是画布左下角的坐标。这里减去了paddingBottom的距离
        basePointY = getHeight() - getPaddingBottom();
        Random random = new Random();
        if (pointers != null)
            pointers.clear();
        for (int i = 0; i < pointerNum; i++) {
            //创建指针对象，利用0~1的随机数 乘以 可绘制区域的高度。作为每个指针的初始高度。
            pointers.add(new Pointer((float) (0.1 * (random.nextInt(10) + 1) * (getHeight() - getPaddingBottom() - getPaddingTop()))));
        }
        //计算每个指针之间的间隔  总宽度 - 左右两边的padding - 所有指针占去的宽度  然后再除以间隔的数量
//        pointerPadding = (getWidth() - getPaddingLeft() - getPaddingRight() - pointerWidth * pointerNum) / (pointerNum - 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                size = (int) (pointerNum * pointerWidth + (pointerNum - 1) * pointerPadding + 0.5F);
                break;
            case MeasureSpec.EXACTLY:
                break;
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    /**
     * 开始绘画
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //将x坐标移动到逻辑原点，也就是左下角
        basePointX = 0f + getPaddingLeft();
        //循环绘制每一个指针。
        for (int i = 0; i < pointers.size(); i++) {
            //绘制指针，也就是绘制矩形
            mPointerRect.set(basePointX,
                    basePointY - pointers.get(i).getHeight(),
                    basePointX + pointerWidth,
                    basePointY);
            canvas.drawRoundRect(mPointerRect, pointerWidth / 2, pointerWidth / 2, paint);
            basePointX += (pointerPadding + pointerWidth);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * 开始播放
     */
    public void start() {
        if (!isPlaying) {
            mIndex = 0;
            mHandler.sendEmptyMessageDelayed(0, pointerSpeed);
            isPlaying = true;
        }
    }

    public void stop() {
        isPlaying = false;
        mHandler.removeCallbacksAndMessages(null);
        invalidate();
    }

    @SuppressLint("HandlerLeak")
    private WeakHandler mHandler = new WeakHandler(this) {

        @Override
        public void handleMessage(VoicePlaying object, Message msg) {
            for (int i = 0; i < pointers.size(); i++) {
                //利用正弦有规律的获取0~1的数。
                float rate = (float) Math.abs(Math.sin(mIndex + i));
                //rate 乘以 可绘制高度，来改变每个指针的高度
                pointers.get(i).setHeight((basePointY - getPaddingTop()) * rate);
            }
            invalidate();
            mIndex += 0.1;
            if (isPlaying) {
                sendEmptyMessageDelayed(0, pointerSpeed);
            }
        }
    };

    /**
     * 指针对象
     */
    public class Pointer {
        private float height;

        public Pointer(float height) {
            this.height = height;
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            this.height = height;
        }
    }

    abstract static class WeakHandler extends Handler {

        private WeakReference<VoicePlaying> mWeakReference;

        public WeakHandler(VoicePlaying voicePlaying) {
            mWeakReference = new WeakReference<>(voicePlaying);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakReference.get() != null) {
                handleMessage(mWeakReference.get(), msg);
            }
        }

        public abstract void handleMessage(VoicePlaying object, Message msg);
    }
}


