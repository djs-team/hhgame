package com.deepsea.mua.im.widget.presenter;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.im.R;
import com.deepsea.mua.im.adapter.EaseMessageAdapter;
import com.deepsea.mua.im.model.EaseDingMessageHelper;
import com.deepsea.mua.im.widget.chatrow.EaseChatExchangeEx;
import com.deepsea.mua.im.widget.chatrow.EaseChatRow;
import com.deepsea.mua.im.widget.dialog.ChangeWxDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by zhangsong on 17-10-12.
 */

public class EaseChangewxPresenter extends EaseChatRowPresenter {
    private static final String TAG = "EaseCHANGEWXPresenter";

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {

        return new EaseChatExchangeEx(cxt, message, position, adapter);
    }

    @Override
    public void onChangeWxClick(EMMessage message, TextView btn_changewx_receive) {
        String wxNum = null;
        String action = null;
        String fromWxNum=null;
        try {
            action = message.getStringAttribute("action", null);
            wxNum = message.getStringAttribute("wxNum");
             fromWxNum = message.getStringAttribute("fromWxNum", null);
            if (action != null && action.equals("cwx_request")) {
                boolean isAccept = message.getBooleanAttribute("isAccept", false);
                if (!isAccept) {
                    showChangeWxDialog(message, wxNum, btn_changewx_receive);
                } else {
                    ToastUtils.showToast("已经接受了交换微信的请求");
                }
            } else {
                String copyString="";
                if (action.equals("copyMsgTwoOver")){
                    copyString=fromWxNum;
                }else {
                    copyString=wxNum;
                }
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText(null, copyString));
                ToastUtils.showToast("微信号已经复制");
            }

        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }


    ChangeWxDialog changeWxDialog = null;

    private void showChangeWxDialog(EMMessage message, String fromWxNo, TextView btn_changewx_receive) {
        if (changeWxDialog != null) {
            changeWxDialog.dismiss();
            changeWxDialog = null;
        }
        changeWxDialog = new ChangeWxDialog(getContext());
        changeWxDialog.setRightButton("提交", new ChangeWxDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                btn_changewx_receive.setEnabled(false);
                btn_changewx_receive.setBackgroundResource(R.drawable.tv_chat_btn_received_bg);
                EaseMessageAdapter adapter = (EaseMessageAdapter) getAdapter();
                EMMessage msgFrom = EMMessage.createTxtSendMessage("接受了交换微信的请求", message.getFrom());
                String wxNo = changeWxDialog.getWxNo();
                msgFrom.setAttribute("type", "wx");
                msgFrom.setAttribute("wxNum", wxNo);
                msgFrom.setAttribute("fromWxNum", fromWxNo);
                msgFrom.setAttribute("action", "acceptOver");
                message.setAttribute("isAccept", true);

                EMClient.getInstance().chatManager().sendMessage(msgFrom);
                adapter.refresh();
                changeWxDialog.dismiss();
            }
        });
        changeWxDialog.show();

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
