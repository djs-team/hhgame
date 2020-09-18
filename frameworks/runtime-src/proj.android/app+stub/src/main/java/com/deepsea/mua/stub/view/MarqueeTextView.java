package com.deepsea.mua.stub.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;


public class MarqueeTextView extends AppCompatTextView {
    /**
     * 滚动次数
     */
    private int marqueeNum = -1;//-1为永久循环。大于0是循环次数。`

    public void setMarqueeNum(int marqueeNum) {
        this.marqueeNum = marqueeNum;
    }

    public MarqueeTextView(Context context) {
        super(context);
        setAttr();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttr();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttr();
    }

    /**
     * 始终获取焦点
     * <p>
     * 跑马灯在TextView处于焦点状态的时候才会滚动
     *
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }

    /**
     * 设置相关属性
     */
    private void setAttr() {
        this.setEllipsize(TextUtils.TruncateAt.MARQUEE);//设置跑马等效果
        this.setMarqueeRepeatLimit(marqueeNum);//设置跑马灯重复次数
        this.setSingleLine(true);//设置单行
    }
}