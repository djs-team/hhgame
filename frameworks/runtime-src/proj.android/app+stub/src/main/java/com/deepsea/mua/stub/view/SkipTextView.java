package com.deepsea.mua.stub.view;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.deepsea.mua.stub.utils.WeakHandler;

/**
 * Created by JUN on 2019/7/20
 */
public class SkipTextView extends TextView {

    private long mSkipDuration = 0;
    private SkipListener mSkipListener = null;
    private SkipHandler mHandler = new SkipHandler(this);

    public SkipTextView(Context context) {
        this(context, null);
    }

    public SkipTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkipTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSkipListener(SkipListener listener) {
        this.mSkipListener = listener;
    }

    public void setSkipDuration(long seconds) {
        this.mSkipDuration = seconds;
    }

    public void startCountDown() {
        mHandler.sendEmptyMessage(0);
    }

    public void stopCountDown() {
        mHandler.removeMessages(0);
    }

    public interface SkipListener {
        void onSkip(SkipTextView view, long duration);

        void onFinished();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopCountDown();
        super.onDetachedFromWindow();
    }

    private static class SkipHandler extends WeakHandler<SkipTextView> {

        public SkipHandler(SkipTextView view) {
            super(view);
        }

        @Override
        public void handleMessage(SkipTextView object, Message msg) {
            if (object.mSkipDuration >= 0) {
                if (object.mSkipListener != null) {
                    object.mSkipListener.onSkip(object, object.mSkipDuration);
                }
                object.mSkipDuration--;
                sendEmptyMessageDelayed(0, 1000);
            } else {
                if (object.mSkipListener != null) {
                    object.mSkipListener.onFinished();
                }
            }
        }
    }
}
