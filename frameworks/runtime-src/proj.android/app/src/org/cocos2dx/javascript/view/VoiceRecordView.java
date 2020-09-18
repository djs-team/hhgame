package org.cocos2dx.javascript.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.deepsea.mua.core.utils.LogUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.core.utils.VoiceRecorder;
import com.deepsea.mua.stub.utils.CacheUtils;
import com.deepsea.mua.stub.utils.FileUtils;

/**
 * Created by JUN on 2019/5/7
 */
public class VoiceRecordView extends RelativeLayout {

    //最短录制时长
    private static final int MIN_LENGTH = 15;

    protected VoiceRecorder voiceRecorder;

    private OnRecordListener mRecordListener;

    private boolean canRecord;

    public interface OnRecordListener {
        void onStart();

        void onComplete(String filePath);

        void onError();
    }

    public VoiceRecordView(Context context) {
        this(context, null);
    }

    public VoiceRecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        voiceRecorder = new VoiceRecorder();
        voiceRecorder.setVoiceFilePath(CacheUtils.getRecordDir());
    }

    public void setCanRecord(boolean canRecord) {
        this.canRecord = canRecord;
    }

    public void setOnRecordListener(OnRecordListener listener) {
        this.mRecordListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!canRecord)
            return super.dispatchTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    startRecording();
                    if (mRecordListener != null) {
                        mRecordListener.onStart();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopRecoding();
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void startRecording() {
        if (!FileUtils.isSdcardExist()) {
            ToastUtils.showToast("录制语音需要sdcard支持");
            return;
        }
        try {
            voiceRecorder.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
            voiceRecorder.discardRecording();
            ToastUtils.showToast("录音失败，请重试");
        }
    }

    public void stopRecoding() {
        try {
            int length = voiceRecorder.stopRecoding();

            LogUtils.e(length, getVoiceFilePath());

            if (length == 401) {
                ToastUtils.showToast("录制失败");
                if (mRecordListener != null) {
                    mRecordListener.onError();
                }
            } else if (length >= MIN_LENGTH) {
                if (mRecordListener != null) {
                    mRecordListener.onComplete(getVoiceFilePath());
                }
            } else {
                ToastUtils.showToast("录音时间太短");
                voiceRecorder.deleteRecord();
                if (mRecordListener != null) {
                    mRecordListener.onError();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mRecordListener != null) {
                mRecordListener.onError();
            }
        }
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }
}
