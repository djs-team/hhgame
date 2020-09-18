package com.deepsea.mua.stub.utils.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import com.deepsea.mua.core.utils.ResUtils;

/**
 * Created by jun on 19/4/26.
 */

public class IdenImageSpan extends ImageSpan {

    private static final int ALIGN_CENTER = 2;

    private Context mContext;
    private int marginRight;

    public IdenImageSpan(Drawable d, Context context) {
        super(d, ALIGN_CENTER);
        this.mContext = context;
        this.marginRight = ResUtils.dp2px(mContext, 5);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        if (mVerticalAlignment == ALIGN_CENTER) {
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
        return super.getSize(paint, text, start, end, fm);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        if (mVerticalAlignment == ALIGN_CENTER) {
            Drawable b = getDrawable();
            canvas.save();
            int transY = 0;
            transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();

            //drawText
            paint.setColor(0xFFFFFFFF);
            paint.setTextSize(ResUtils.sp2px(mContext, 12));
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            float baseline = (top + bottom - fontMetrics.top - fontMetrics.bottom) / 2;

            float dx = x + (b.getBounds().right - paint.measureText(text, start, end)) / 2;
            canvas.drawText(text, start, end, dx, baseline, paint);
        } else {
            super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        }
    }
}
