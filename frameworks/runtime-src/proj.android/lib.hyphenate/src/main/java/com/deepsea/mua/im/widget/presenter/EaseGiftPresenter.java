package com.deepsea.mua.im.widget.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.deepsea.mua.im.model.EaseDingMessageHelper;
import com.deepsea.mua.im.widget.chatrow.EaseChatGiveGift;
import com.deepsea.mua.im.widget.chatrow.EaseChatRow;
import com.deepsea.mua.lib.svga.dialog.ShowGiftAnimationDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.lang.ref.WeakReference;

/**
 * Created by zhangsong on 17-10-12.
 */

public class EaseGiftPresenter extends EaseChatRowPresenter {
    private static final String TAG = "EaseGiftPresenter";

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {

        return new EaseChatGiveGift(cxt, message, position, adapter);
    }

    /**
     * 动画
     */
    ShowGiftAnimationDialog dialog;

    private void showGiftAnimation(String animation) {

        dialog = new ShowGiftAnimationDialog(getContext(), new MyHandler(this));
        dialog.setGiftAnimation(animation);
        dialog.show();

    }

    private static final int msg_close_dialog = 1001;

    private static class MyHandler extends Handler {
        private final WeakReference<EaseGiftPresenter> mActivity;

        public MyHandler(EaseGiftPresenter activity) {
            mActivity = new WeakReference<EaseGiftPresenter>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            if (msg.what == msg_close_dialog) {
                ShowGiftAnimationDialog dialog = mActivity.get().dialog;
                dialog.dismiss();
                dialog.cancel();
                dialog = null;
            }

        }
    }


    @Override
    public void onGiftClick(EMMessage message, ImageView img_gift) {
        super.onGiftClick(message, img_gift);
        String animation = message.getStringAttribute("animation", null);
        if (!TextUtils.isEmpty(animation)) {
            showGiftAnimation(animation);
        }
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
        if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            return;
        }

        // Send the group-ack cmd type msg if this msg is a ding-type msg.
        EaseDingMessageHelper.get().sendAckMessage(message);
    }
}
