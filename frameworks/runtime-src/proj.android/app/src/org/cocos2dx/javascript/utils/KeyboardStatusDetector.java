package org.cocos2dx.javascript.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class KeyboardStatusDetector {

    private static final int SOFT_KEY_BOARD_MIN_HEIGHT = 100;

    private KeyboardListener mKBListener;
    private boolean isVisible = false;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalListener;

    public void setKeyboardListener(KeyboardListener mGlobalListener) {
        this.mKBListener = mGlobalListener;
    }

    public void registerActivity(Activity activity) {
        View globalView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        mGlobalListener = () -> {
            Rect r = new Rect();
            globalView.getWindowVisibleDisplayFrame(r);
            int height = globalView.getRootView().getHeight() - (r.bottom - r.top);

            if (height > SOFT_KEY_BOARD_MIN_HEIGHT) {
                if (!isVisible) {
                    isVisible = true;
                    if (mKBListener != null)
                        mKBListener.onKeyBoardStatusChanged(true, height);
                } else {
                    isVisible = false;
                    if (mKBListener != null)
                        mKBListener.onKeyBoardStatusChanged(true, 0);
                }
            }
        };
        globalView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalListener);
    }

    public void unRegisterActivity(Activity activity) {
        View globalView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        globalView.getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalListener);
    }

    public interface KeyboardListener {
        void onKeyBoardStatusChanged(boolean visible, int height);
    }
}
