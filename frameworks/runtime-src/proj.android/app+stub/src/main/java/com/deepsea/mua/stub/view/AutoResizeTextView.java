package com.deepsea.mua.stub.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by JUN on 2019/5/5
 */
public class AutoResizeTextView extends TextView {

    public AutoResizeTextView(Context context) {
        super(context);
    }

    public AutoResizeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoResizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!scaleText(getWidth() - getPaddingRight() - getPaddingLeft())) {
            super.onDraw(canvas);
        }
    }

    private boolean scaleText(int width) {
        float needWidth = getPaint().measureText(getText().toString());//需要的宽度
        if (needWidth <= width || needWidth == 0) {
            return false;
        }
        float scaleSize = getTextSize();
        scaleSize = scaleSize * (width / needWidth);
        if (scaleSize != 0) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, scaleSize);
            return true;
        }
        return false;
    }
}
