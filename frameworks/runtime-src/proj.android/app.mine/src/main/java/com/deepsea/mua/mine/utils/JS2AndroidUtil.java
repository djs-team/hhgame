package com.deepsea.mua.mine.utils;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.deepsea.mua.mine.activity.WebActivity;
import com.deepsea.mua.stub.utils.PageJumpUtils;

public class JS2AndroidUtil {
    private Activity mActivity;

    public JS2AndroidUtil(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 进入聊天室
     *
     * @param roomId
     */
    @JavascriptInterface
    public void jumpRoom(String roomId) {
        if (mActivity instanceof WebActivity) {
            ((WebActivity) mActivity).jumpRoom(roomId);
        }
    }

    /**
     * 前往认证
     */
    @JavascriptInterface
    public void gotoAccreditation() {
        if (mActivity instanceof WebActivity) {
            ((WebActivity) mActivity).gotoAccreditation();
        }
    }
}
