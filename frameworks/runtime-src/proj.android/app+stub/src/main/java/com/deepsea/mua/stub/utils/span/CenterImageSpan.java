package com.deepsea.mua.stub.utils.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.core.utils.ResUtils;

/**
 * Created by jun on 19/4/26.
 */

public class CenterImageSpan extends ImageSpan {

    public static final int ALIGN_CENTER = 2;
    private int marginRight;

    public CenterImageSpan(Drawable d) {
        super(d, ALIGN_CENTER);
        this.marginRight = ResUtils.dp2px(AppUtils.getApp(), 5);
    }

    public CenterImageSpan(@NonNull Context context, int resourceId, int marginDp) {
        super(context, resourceId, ALIGN_CENTER);
        this.marginRight = ResUtils.dp2px(context, marginDp);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right + marginRight;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY = 0;
        transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}
