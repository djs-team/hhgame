package com.deepsea.mua.stub.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.deepsea.mua.stub.R;

/**
 * Created by JUN on 2019/3/28
 */
public class MaxRelativeLayout extends RelativeLayout {

    private int mMaxHeight;

    public MaxRelativeLayout(Context context) {
        this(context, null);
    }

    public MaxRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MaxRelativeLayout);
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxRelativeLayout_maxHeight, mMaxHeight);
        arr.recycle();
    }

    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
