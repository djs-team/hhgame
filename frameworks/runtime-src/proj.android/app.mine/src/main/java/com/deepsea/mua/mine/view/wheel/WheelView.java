package com.deepsea.mua.mine.view.wheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.deepsea.mua.mine.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author cncoderx
 */
public class WheelView extends View {

    private static final int ALIGN_LEFT = -1;
    private static final int ALIGN_CENTER = 0;
    private static final int ALIGN_RIGHT = 1;

    boolean mCyclic;
    int mItemCount;
    int mItemWidth;
    int mItemHeight;
    int mDividerHeight;
    Rect mClipRectTop;
    Rect mClipRectMiddle;
    Rect mClipRectBottom;

    int mTextAlign;

    TextPaint mTextPaint;
    TextPaint mSelectedTextPaint;
    Paint mDividerPaint;
    Paint mHighlightPaint;

    WheelScroller mScroller;

    final List<CharSequence> mEntries = new ArrayList<>();

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
        boolean cyclic = a.getBoolean(R.styleable.WheelView_wheelCyclic, false);
        int itemCount = a.getInt(R.styleable.WheelView_wheelItemCount, 9);
        int itemWidth = a.getDimensionPixelOffset(R.styleable.WheelView_wheelItemWidth, 0);
        int itemHeight = a.getDimensionPixelOffset(R.styleable.WheelView_wheelItemHeight, 0);
        int textSize = a.getDimensionPixelSize(R.styleable.WheelView_wheelTextSize, 0);
        int textColor = a.getColor(R.styleable.WheelView_wheelTextColor, $color(R.color.black));
        int selectedTextColor = a.getColor(R.styleable.WheelView_wheelSelectedTextColor, $color(R.color.black));
        int dividerColor = a.getColor(R.styleable.WheelView_wheelDividerColor, $color(R.color.transparent));
        int highlightColor = a.getColor(R.styleable.WheelView_wheelHighlightColor, $color(R.color.transparent));
        int textAlign = a.getInteger(R.styleable.WheelView_wheelTextAlign, ALIGN_CENTER);
        CharSequence[] entries = a.getTextArray(R.styleable.WheelView_wheelEntries);
        a.recycle();

        mCyclic = cyclic;
        mItemCount = itemCount;
        mItemWidth = itemWidth;
        mItemHeight = itemHeight;
        mTextAlign = textAlign;

        Paint.Align align = Paint.Align.CENTER;
        if (mTextAlign == ALIGN_LEFT) {
            align = Paint.Align.LEFT;
        }
        if (mTextAlign == ALIGN_RIGHT) {
            align = Paint.Align.RIGHT;
        }

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(align);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);

        mSelectedTextPaint = new TextPaint();
        mSelectedTextPaint.setAntiAlias(true);
        mSelectedTextPaint.setTextAlign(align);
        mSelectedTextPaint.setTextSize(textSize);
        mSelectedTextPaint.setColor(selectedTextColor);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth(mDividerHeight);
        mDividerPaint.setColor(dividerColor);

        mHighlightPaint = new Paint();
        mHighlightPaint.setAntiAlias(true);
        mHighlightPaint.setStyle(Paint.Style.FILL);
        mHighlightPaint.setColor(highlightColor);

        if (entries != null && entries.length > 0) {
            mEntries.addAll(Arrays.asList(entries));
        }

        mScroller = new WheelScroller(context, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mItemWidth = getMeasuredWidth();
        if (mItemWidth != 0) {
            setMeasuredDimension(getMeasuredWidth(), getPrefHeight());
        }

        updateClipRect();
    }

    private void updateClipRect() {
        int clipLeft = getPaddingLeft();
        int clipRight = getMeasuredWidth() - getPaddingRight();
        int clipTop = getPaddingTop();
        int clipBottom = getMeasuredHeight() - getPaddingBottom();
        int clipVMiddle = (clipTop + clipBottom) / 2;

        mClipRectMiddle = new Rect();
        mClipRectMiddle.left = clipLeft;
        mClipRectMiddle.right = clipRight;
        mClipRectMiddle.top = clipVMiddle - mItemHeight / 2;
        mClipRectMiddle.bottom = clipVMiddle + mItemHeight / 2;

        mClipRectTop = new Rect();
        mClipRectTop.left = clipLeft;
        mClipRectTop.right = clipRight;
        mClipRectTop.top = clipTop;
        mClipRectTop.bottom = clipVMiddle - mItemHeight / 2;

        mClipRectBottom = new Rect();
        mClipRectBottom.left = clipLeft;
        mClipRectBottom.right = clipRight;
        mClipRectBottom.top = clipVMiddle + mItemHeight / 2;
        mClipRectBottom.bottom = clipBottom;
    }

    int $dp(int resId) {
        return getResources().getDimensionPixelOffset(resId);
    }

    int $sp(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    int $color(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * @return 控件的预算宽度
     */
    public int getPrefWidth() {
        int paddingHorizontal = getPaddingLeft() + getPaddingRight();
        return paddingHorizontal + mItemWidth;
    }

    /**
     * @return 控件的预算高度
     */
    public int getPrefHeight() {
        int paddingVertical = getPaddingTop() + getPaddingBottom();
        return paddingVertical + mItemHeight * mItemCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawHighlight(canvas);
        drawItems(canvas);
        drawDivider(canvas);
    }

    private void drawItems(Canvas canvas) {
        final int index = mScroller.getItemIndex();
        final int offset = mScroller.getItemOffset();
        final int hf = (mItemCount + 1) / 2;
        final int minIdx, maxIdx;
        if (offset < 0) {
            minIdx = index - hf - 1;
            maxIdx = index + hf;
        } else if (offset > 0) {
            minIdx = index - hf;
            maxIdx = index + hf + 1;
        } else {
            minIdx = index - hf;
            maxIdx = index + hf;
        }
        for (int i = minIdx; i < maxIdx; i++) {
            drawItem(canvas, i, offset);
        }
    }

    protected void drawItem(Canvas canvas, int index, int offset) {
        CharSequence text = getCharSequence(index);
        if (text == null) return;

        int startX = mClipRectMiddle.centerX();
        final int centerY = mClipRectMiddle.centerY();

        if (mTextAlign == ALIGN_LEFT) {
            startX = 0;
        } else if (mTextAlign == ALIGN_RIGHT) {
            startX = mClipRectMiddle.width();
        }

        // 和中间选项的距离
        final int range = (index - mScroller.getItemIndex()) * mItemHeight - offset;

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        int baseline = (int) ((fontMetrics.top + fontMetrics.bottom) / 2);

        // 绘制与下分界线相交的文字
        if (range > 0 && range < mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectMiddle);
            canvas.drawText(text, 0, text.length(), startX, centerY + range - baseline, mSelectedTextPaint);
            canvas.restore();

            canvas.save();
            canvas.clipRect(mClipRectBottom);
            canvas.drawText(text, 0, text.length(), startX, centerY + range - baseline, mTextPaint);
            canvas.restore();
        }
        // 绘制下分界线下方的文字
        else if (range >= mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectBottom);
            canvas.drawText(text, 0, text.length(), startX, centerY + range - baseline, mTextPaint);
            canvas.restore();
        }
        // 绘制与上分界线相交的文字
        else if (range < 0 && range > -mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectMiddle);
            canvas.drawText(text, 0, text.length(), startX, centerY + range - baseline, mSelectedTextPaint);
            canvas.restore();

            canvas.save();
            canvas.clipRect(mClipRectTop);
            canvas.drawText(text, 0, text.length(), startX, centerY + range - baseline, mTextPaint);
            canvas.restore();
        }
        // 绘制上分界线上方的文字
        else if (range <= -mItemHeight) {
            canvas.save();
            canvas.clipRect(mClipRectTop);
            canvas.drawText(text, 0, text.length(), startX, centerY + range - baseline, mTextPaint);
            canvas.restore();
        }
        // 绘制两条分界线之间的文字
        else {
            canvas.save();
            canvas.clipRect(mClipRectMiddle);
            canvas.drawText(text, 0, text.length(), startX, centerY + range - baseline, mSelectedTextPaint);
            canvas.restore();
        }
    }

    CharSequence getCharSequence(int index) {
        int size = mEntries.size();
        if (size == 0) return null;
        CharSequence text = null;
        if (isCyclic()) {
            int i = index % size;
            if (i < 0) {
                i += size;
            }
            text = mEntries.get(i);
        } else {
            if (index >= 0 && index < size) {
                text = mEntries.get(index);
            }
        }
        return text;
    }

    private void drawHighlight(Canvas canvas) {
        canvas.drawRect(mClipRectMiddle, mHighlightPaint);
    }

    private void drawDivider(Canvas canvas) {
        // 绘制上层分割线
        canvas.drawLine(mClipRectMiddle.left, mClipRectMiddle.top, mClipRectMiddle.right, mClipRectMiddle.top, mDividerPaint);

        // 绘制下层分割线
        canvas.drawLine(mClipRectMiddle.left, mClipRectMiddle.bottom, mClipRectMiddle.right, mClipRectMiddle.bottom, mDividerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScroller.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        mScroller.computeScroll();
    }

    public boolean isCyclic() {
        return mCyclic;
    }

    public void setCyclic(boolean cyclic) {
        mCyclic = cyclic;
        mScroller.reset();
        invalidate();
    }

    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    public void setTextSize(int textSize) {
        mTextPaint.setTextSize(textSize);
        mSelectedTextPaint.setTextSize(textSize);
        invalidate();
    }

    public int getTextColor() {
        return mTextPaint.getColor();
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    public int getSelectedTextColor() {
        return mSelectedTextPaint.getColor();
    }

    public void setSelectedTextColor(int color) {
        mSelectedTextPaint.setColor(color);
        invalidate();
    }

    public int getItemSize() {
        return mEntries.size();
    }

    public CharSequence getItem(int index) {
        if (index < 0 || index >= mEntries.size())
            return "";

        return mEntries.get(index);
    }

    public CharSequence getCurrentItem() {
        return getItem(getCurrentIndex());
    }

    public int getCurrentIndex() {
        return mScroller.getCurrentIndex();
    }

    public void setCurrentIndex(int index) {
        setCurrentIndex(index, false);
    }

    public void setCurrentIndex(int index, boolean animated) {
        if (index < 0 || index > mEntries.size())
            return;
        mScroller.setCurrentIndex(index, animated);
    }

    public void setEntries(CharSequence... entries) {
        mEntries.clear();
        if (entries != null && entries.length > 0) {
            Collections.addAll(mEntries, entries);
        }
        mScroller.reset();
        invalidate();
    }

    public void setEntries(Collection<? extends CharSequence> entries) {
        mEntries.clear();
        if (entries != null && entries.size() > 0) {
            mEntries.addAll(entries);
        }
        mScroller.reset();
        invalidate();
    }

    public OnWheelChangedListener getOnWheelChangedListener() {
        return mScroller.onWheelChangedListener;
    }

    public void setOnWheelChangedListener(OnWheelChangedListener onWheelChangedListener) {
        mScroller.onWheelChangedListener = onWheelChangedListener;
    }
}
