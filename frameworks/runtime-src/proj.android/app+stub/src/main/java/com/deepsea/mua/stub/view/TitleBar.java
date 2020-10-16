package com.deepsea.mua.stub.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.utils.StateUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/7/13
 */
public class TitleBar extends ConstraintLayout {

    private TextView mTitleTv;
    private ImageView mLeftIv, mLeftVolleyIv;
    private ImageView mRightIv;
    private TextView mRightTv;
    private TextView mLeftTv;
    private WithBackgroundTextView tvSate;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.layout_title_bar, this);
        mTitleTv = findViewById(R.id.title_tv);
        mLeftIv = findViewById(R.id.back_iv);
        mLeftVolleyIv = findViewById(R.id.back_volley_iv);
        mRightIv = findViewById(R.id.right_iv);
        mRightTv = findViewById(R.id.right_tv);
        mLeftTv = findViewById(R.id.left_title_tv);
        tvSate = findViewById(R.id.tv_state);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        String title = typedArray.getString(R.styleable.TitleBar_title);
        boolean isLeftImgVisibility = typedArray.getBoolean(R.styleable.TitleBar_leftImgVisibility, true);
        int leftImgRes = typedArray.getResourceId(R.styleable.TitleBar_leftImgRes, 0);
        int rightImgRes = typedArray.getResourceId(R.styleable.TitleBar_rightImgRes, 0);
        int backgroundRes = typedArray.getResourceId(R.styleable.TitleBar_backgroundRes, 0);
        int titleColor = typedArray.getColor(R.styleable.TitleBar_titleColor, 0xFF313131);
        int rightTitleColor = typedArray.getColor(R.styleable.TitleBar_rightTitleColor, 0xFF313131);
        int titleSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_titleSize, 0);
        int rightTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBar_rightTextSize, 0);
        String rightText = typedArray.getString(R.styleable.TitleBar_rightText);
        String leftText = typedArray.getString(R.styleable.TitleBar_leftText);
        boolean titleBold = typedArray.getBoolean(R.styleable.TitleBar_titleBold, false);
        int rightTitleDrawableLeft = typedArray.getResourceId(R.styleable.TitleBar_rightTitleDrawableLeft, 0);

        typedArray.recycle();

        setTitle(title);
        setRightText(rightText);
        setLeftText(leftText);
        ViewBindUtils.setVisible(mLeftIv, isLeftImgVisibility);
        if (leftImgRes != 0) {
            mLeftIv.setImageResource(leftImgRes);
        }
        if (rightImgRes != 0) {
            mRightIv.setImageResource(rightImgRes);
        }
        if (rightTitleDrawableLeft != 0) {
            setRightTextDrawableLeft(rightTitleDrawableLeft);
        }
        if (backgroundRes == 0) {
            backgroundRes = R.color.white;
        }
        if (titleSize != 0) {
            mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        }
        if (rightTextSize != 0) {
            mRightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
        }
        if (titleBold) {
            mTitleTv.getPaint().setFakeBoldText(true);
        }
        setBackgroundResource(backgroundRes);
        mTitleTv.setTextColor(titleColor);
        mRightTv.setTextColor(rightTitleColor);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setOnlineState(String str) {
        Log.d("onlineState", str);
        boolean hasStateOnline = !TextUtils.isEmpty(str);
        ViewBindUtils.setVisible(tvSate, hasStateOnline);
        if (hasStateOnline) {
            StateUtils.setState(tvSate, str);
        }
    }

    public void setRightText(String text) {
        mRightTv.setText(text);
    }

    public void setRightTextDrawableLeft(int imgId) {
        Drawable drawable = getResources().getDrawable(imgId);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mRightTv.setCompoundDrawables(drawable, null, null, null);
        mRightTv.setCompoundDrawablePadding(4);
    }

    public void setLeftText(String text) {
        mLeftTv.setText(text);
    }

    public void setLeftTextColor(String color) {
        mLeftTv.setTextColor(Color.parseColor(color));
    }

    public ImageView getLeftImg() {
        return mLeftIv;
    }

    public ImageView getLeftVolleyImg() {
        return mLeftVolleyIv;
    }

    public ImageView getRightImg() {
        return mRightIv;
    }

    public TextView getRightTv() {
        return mRightTv;
    }

    public TextView getLeftTv() {
        return mLeftTv;
    }

    public TextView getTitleTv() {
        return mTitleTv;
    }

    public void setRightImgRes(int imgRes) {
        mRightIv.setImageResource(imgRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                size = getContext().getResources().getDimensionPixelSize(R.dimen.title_height);
                break;
            case MeasureSpec.EXACTLY:
                break;
        }

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
    }
}
