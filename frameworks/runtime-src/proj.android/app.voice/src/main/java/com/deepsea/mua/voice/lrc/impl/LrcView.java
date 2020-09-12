package com.deepsea.mua.voice.lrc.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.deepsea.mua.core.utils.UiUtils;
import com.deepsea.mua.stub.utils.UIUtils;
import com.deepsea.mua.voice.lrc.ILrcView;
import com.deepsea.mua.voice.lrc.ILrcViewListener;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义LrcView,可以同步显示歌词，拖动歌词，缩放歌词
 */
public class LrcView extends View implements ILrcView {
    public final static String TAG = "LrcView";

    /**
     * 正常歌词模式
     */
    public final static int DISPLAY_MODE_NORMAL = 0;
    /**
     * 拖动歌词模式
     */
    public final static int DISPLAY_MODE_SEEK = 1;
    /**
     * 缩放歌词模式
     */
    public final static int DISPLAY_MODE_SCALE = 2;
    /**
     * 歌词的当前展示模式
     */
    private int mDisplayMode = DISPLAY_MODE_NORMAL;

    /**
     * 歌词集合，包含所有行的歌词
     */
    private List<LrcRow> mLrcRows;
    /**
     * 最小移动的距离，当拖动歌词时如果小于该距离不做处理
     */
    private int mMinSeekFiredOffset = 10;

    /**
     * 当前高亮歌词的行数
     */
    private int mHighLightRow = 0;
    /**
     * 当前高亮歌词的字体颜色为黄色
     */
    private int mHighLightRowColor = Color.parseColor("#EF51B2");
    /**
     * 不高亮歌词的字体颜色为白色
     */
    private int mNormalRowColor = Color.parseColor("#EF51B2");

    /**
     * 拖动歌词时，在当前高亮歌词下面的一条直线的字体颜色
     **/
    private int mSeekLineColor = Color.CYAN;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体颜色
     **/
    private int mSeekLineTextColor = Color.CYAN;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体大小默认值
     **/
    private int mSeekLineTextSize = 15;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体大小最小值
     **/
    private int mMinSeekLineTextSize = 13;
    /**
     * 拖动歌词时，展示当前高亮歌词的时间的字体大小最大值
     **/
    private int mMaxSeekLineTextSize = 18;

    /**
     * 歌词字体大小默认值
     **/
    private int mLrcFontSize = 80;    // font size of lrc
    /**
     * 歌词字体大小最小值
     **/
    private int mMinLrcFontSize = 24;
    /**
     * 歌词字体大小最大值
     **/
    private int mMaxLrcFontSize = 24;

    /**
     * 两行歌词之间的间距
     **/
    private int mPaddingY = 10;
    /**
     * 拖动歌词时，在当前高亮歌词下面的一条直线的起始位置
     **/
    private int mSeekLinePaddingX = 0;

    /**
     * 拖动歌词的监听类，回调LrcViewListener类的onLrcSeeked方法
     **/
    private ILrcViewListener mLrcViewListener;

    /**
     * 当没有歌词的时候展示的内容
     **/
    private String mLoadingLrcTip = "";

    private Paint mPaint;

    /**
     * 当前播放的时间
     */
    long currentMillis;

    /**
     * 歌词高亮的模式 正常高亮模式
     */
    private int MODE_HIGH_LIGHT_NORMAL = 0;
    /**
     * 歌词高亮的模式 卡拉OK模式
     */
    private int MODE_HIGH_LIGHT_KARAOKE = 1;

    /**
     * 歌词高亮的模式   卡拉OK模式和正常高亮模式
     */
    private int mode = MODE_HIGH_LIGHT_KARAOKE;
    private Context mContext;

    public LrcView(Context context, AttributeSet attr) {
        super(context, attr);
        this.mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(DisplayUtil.dip2px(mContext, 16));
    }

    public void setListener(ILrcViewListener listener) {
        mLrcViewListener = listener;
    }

    public void setLoadingTipText(String text) {
        mLoadingLrcTip = text;
    }

    @Override
    public void stopShowLrc(boolean isStop) {

    }

    @Override
    public int getHightLightPos() {
        return mHighLightRow;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight(); // height of this view
        final int width = getWidth(); // width of this view
        //当没有歌词的时候
        if (mLrcRows == null || mLrcRows.size() == 0) {
//            if (mLoadingLrcTip != null) {
//                // draw tip when no lrc.
//                mPaint.setColor(mHighLightRowColor);
//                mPaint.setTextSize(mLrcFontSize);
//                mPaint.setTextAlign(Align.CENTER);
//                canvas.drawText(mLoadingLrcTip, 0, height / 2 - mLrcFontSize, mPaint);
//            }
            return;
        }

        int rowY = 0; // vertical point of each row.
        final int rowX = 0;
        int rowNum = 0;
        /**
         * 分以下三步来绘制歌词：
         *
         * 	第1步：高亮地画出正在播放的那句歌词
         *	第2步：画出正在播放的那句歌词的上面可以展示出来的歌词
         *	第3步：画出正在播放的那句歌词的下面的可以展示出来的歌词
         */

        // 1、 高亮地画出正在要高亮的的那句歌词
        int highlightRowY = height / 2 - DisplayUtil.dip2px(mContext, mLrcFontSize);

        if (mode == MODE_HIGH_LIGHT_KARAOKE) {
            // 卡拉OK模式 逐字高亮
            drawKaraokeHighLightLrcRow(canvas, width, rowX, highlightRowY);
        } else {
            // 正常高亮
            drawHighLrcRow(canvas, height, rowX, highlightRowY);
        }

        // 3、画出正在播放的那句歌词的下面的可以展示出来的歌词
        rowNum = mHighLightRow + 1;
        rowY = highlightRowY + mPaddingY + DisplayUtil.dip2px(mContext, mLrcFontSize);

//        只画出正在播放的那句歌词的下一句歌词
        if (rowY < height && rowNum < mLrcRows.size()) {
            String text2 = mLrcRows.get(rowNum).content;
            canvas.drawText(text2, rowX, rowY, mPaint);
        }

//        //画出正在播放的那句歌词的所有下面的可以展示出来的歌词
//        while (rowY < height && rowNum < mLrcRows.size()) {
//            String text = mLrcRows.get(rowNum).content;
//            canvas.drawText(text, rowX, rowY, mPaint);
//            rowY += (mPaddingY + mLrcFontSize);
//            rowNum++;
//        }

    }

    private void drawKaraokeHighLightLrcRow(Canvas canvas, int width, int rowX, int highlightRowY) {
        LrcRow highLrcRow = mLrcRows.get(mHighLightRow);
        String highlightText = highLrcRow.content;

        // 先画一层普通颜色的
        mPaint.setColor(mNormalRowColor);
        mPaint.setTextSize(DisplayUtil.dip2px(mContext, mLrcFontSize));
        mPaint.setTextAlign(Align.LEFT);
        canvas.drawText(highlightText, 0, highlightRowY, mPaint);

        // 再画一层高亮颜色的
        int highLineWidth = (int) mPaint.measureText(highlightText);
        int leftOffset = 0;
        long start = highLrcRow.getStartTime();
        long end = highLrcRow.getEndTime();
        // 高亮的宽度
        int highWidth = (int) ((currentMillis - start) * 1.0f / (end - start) * highLineWidth);
        if (highWidth > 0) {
            //画一个 高亮的bitmap
            try {


                mPaint.setColor(mHighLightRowColor);
                Bitmap textBitmap = Bitmap.createBitmap(highWidth, highlightRowY + mPaddingY, Bitmap.Config.ARGB_8888);
                Canvas textCanvas = new Canvas(textBitmap);
                textCanvas.drawText(highlightText, 0, highlightRowY, mPaint);
                canvas.drawBitmap(textBitmap, leftOffset, 0, mPaint);
                if (textBitmap != null && !textBitmap.isRecycled()) {
                    // 回收并且置为null
                    textBitmap.recycle();
                    textBitmap = null;
                }

                System.gc();
            } catch (Exception e) {

            }
        }
    }

    private void drawHighLrcRow(Canvas canvas, int height, int rowX, int highlightRowY) {
        String highlightText = mLrcRows.get(mHighLightRow).content;
        mPaint.setColor(mHighLightRowColor);
        mPaint.setTextSize(DisplayUtil.dip2px(mContext, mLrcFontSize));
        mPaint.setTextAlign(Align.LEFT);
        canvas.drawText(highlightText, 0, highlightRowY, mPaint);
    }

    /**
     * 设置要高亮的歌词为第几行歌词
     *
     * @param position 要高亮的歌词行数
     * @param cb       是否是手指拖动后要高亮的歌词
     */
    public void seekLrc(int position, boolean cb) {
        if (mLrcRows == null || position < 0 || position > mLrcRows.size()) {
            return;
        }
        LrcRow lrcRow = mLrcRows.get(position);
        mHighLightRow = position;
        invalidate();
        //如果是手指拖动歌词后
        if (mLrcViewListener != null && cb) {
            //回调onLrcSeeked方法，将音乐播放器播放的位置移动到高亮歌词的位置
            mLrcViewListener.onLrcSought(position, lrcRow);
        }
    }


    /**
     * 设置歌词行集合
     *
     * @param lrcRows
     */
    /**
     * 设置歌词行集合
     *
     * @param lrcRows
     */
    @Override
    public void setLrc(List<LrcRow> lrcRows) {
        if (lrcRows == null) {
            mHighLightRow = 0;
        }
        mLrcRows = lrcRows;
        invalidate();
    }

    @Override
    public void setLrcOneLine(LrcRow lrcRow) {
        List<LrcRow> rows = new ArrayList<>();
        rows.add(lrcRow);
        mLrcRows = rows;
        invalidate();
    }

    @Override
    public List<LrcRow> getLrc() {
        return mLrcRows;
    }

    @Override
    public LrcRow getCurrentLrc() {
        if (mLrcRows != null && mLrcRows.size() > mHighLightRow) {
            LrcRow lrcRow = mLrcRows.get(mHighLightRow);

            return lrcRow;
        } else {
            return null;
        }
    }

    @Override
    public LrcRow getNextLrc() {
        if (mLrcRows != null && mLrcRows.size() > mHighLightRow + 1) {
            LrcRow lrcRow = mLrcRows.get(mHighLightRow + 1);
            return lrcRow;
        } else {
            return null;
        }
    }


    /**
     * 播放的时候调用该方法滚动歌词，高亮正在播放的那句歌词
     *
     * @param time
     */
    public void seekLrcToTime(long time) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return;
        }
        if (mDisplayMode != DISPLAY_MODE_NORMAL) {
            return;
        }

        currentMillis = time;
        Log.d(TAG, "seekLrcToTime:" + time);

        for (int i = 0; i < mLrcRows.size(); i++) {
            LrcRow current = mLrcRows.get(i);
            LrcRow next = i + 1 == mLrcRows.size() ? null : mLrcRows.get(i + 1);
            /**
             *  正在播放的时间大于current行的歌词的时间而小于next行歌词的时间， 设置要高亮的行为current行
             *  正在播放的时间大于current行的歌词，而current行为最后一句歌词时，设置要高亮的行为current行
             */
            if ((time >= current.startTime && next != null && time < next.startTime)
                    || (time > current.startTime && next == null)) {
                seekLrc(i, false);
                return;
            }
        }
    }
}