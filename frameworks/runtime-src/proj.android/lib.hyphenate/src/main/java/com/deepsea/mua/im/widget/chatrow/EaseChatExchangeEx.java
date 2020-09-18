package com.deepsea.mua.im.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.deepsea.mua.im.R;
import com.deepsea.mua.im.adapter.EaseMessageAdapter;
import com.deepsea.mua.im.model.EaseDingMessageHelper;
import com.deepsea.mua.im.utils.EaseSmileUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class EaseChatExchangeEx extends EaseChatRow {

    private TextView contentView;
    private EaseMessageAdapter adapter;


    public EaseChatExchangeEx(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        this.adapter = (EaseMessageAdapter) adapter;

    }

    @Override
    protected void onInflateView() {
        EMMessage.Direct direct = message.direct();
        View view = inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_changewx_received_message : R.layout.ease_changewx_sent_message, this);
        TextView btn_changewx_receive = view.findViewById(R.id.btn_changewx_receive);
      String  action = message.getStringAttribute("action", null);


    }

    @Override
    protected void onFindViewById() {
        contentView = findViewById(R.id.tv_chatcontent);
    }

    @Override
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();

        Spannable span = null;
        try {
            String action = message.getStringAttribute("action");
            EMMessage.Direct direct = message.direct();
            if (action != null && action.equals("cwx_request")) {
                if (direct == EMMessage.Direct.RECEIVE) {
                    txtBody = new EMTextMessageBody("收到了交换微信的请求");
                } else {
                    txtBody = new EMTextMessageBody("发起了交换微信的请求");
                }
            } else if (action != null && action.equals("acceptOver")) {
                String fromWxNum = message.getStringAttribute("fromWxNum", null);
                String wxNum = message.getStringAttribute("wxNum", null);
                message.setAttribute("isAccept", true);

                if (direct == EMMessage.Direct.RECEIVE) {
                    txtBody = new EMTextMessageBody(fromWxNum);
                    EMMessage msgFrom = EMMessage.createTxtSendMessage("微信ID:" + fromWxNum, message.getFrom());
                    msgFrom.setAttribute("type", "wx");
                    msgFrom.setAttribute("action", "copyMsgOne");
                    msgFrom.setAttribute("wxNum", wxNum);
                    msgFrom.setAttribute("fromWxNum", fromWxNum);
                    message.setAttribute("action", "starWxNumSend");
                    EMClient.getInstance().chatManager().sendMessage(msgFrom);
                    adapter.refresh();
                }
            } else if (action != null && action.equals("copyMsgOne")) {
                String fromWxNum = message.getStringAttribute("fromWxNum", null);
                String wxNum = message.getStringAttribute("wxNum", null);
                if (direct == EMMessage.Direct.RECEIVE) {
                    txtBody = new EMTextMessageBody(wxNum);
                    EMMessage msgFrom = EMMessage.createTxtSendMessage("微信ID:" + wxNum, message.getFrom());
                    msgFrom.setAttribute("type", "wx");
                    msgFrom.setAttribute("action", "copyMsgTwo");
                    msgFrom.setAttribute("wxNum", wxNum);
                    msgFrom.setAttribute("fromWxNum", fromWxNum);
                    msgFrom.setAttribute("isAccept", true);
                    message.setAttribute("isAccept", true);
                    message.setAttribute("action", "copyMsgTwoOver");
                    EMClient.getInstance().chatManager().sendMessage(msgFrom);
                    adapter.refresh();
                }
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);

    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        switch (msg.status()) {
            case CREATE:
                onMessageCreate();
                break;
            case SUCCESS:
                onMessageSuccess();
                break;
            case FAIL:
                onMessageError();
                break;
            case INPROGRESS:
                onMessageInProgress();
                break;
        }
    }

    public void onAckUserUpdate(final int count) {
        if (ackedView != null) {
            ackedView.post(new Runnable() {
                @Override
                public void run() {
                    ackedView.setVisibility(VISIBLE);
                    ackedView.setText(String.format(getContext().getString(R.string.group_ack_read_count), count));
                }
            });
        }
    }

    private void onMessageCreate() {
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }

    private void onMessageSuccess() {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.GONE);

        // Show "1 Read" if this msg is a ding-type msg.
        if (EaseDingMessageHelper.get().isDingMessage(message) && ackedView != null) {
            ackedView.setVisibility(VISIBLE);
            List<String> userList = EaseDingMessageHelper.get().getAckUsers(message);
            int count = userList == null ? 0 : userList.size();
            ackedView.setText(String.format(getContext().getString(R.string.group_ack_read_count), count));
        }

        // Set ack-user list change listener.
        EaseDingMessageHelper.get().setUserUpdateListener(message, userUpdateListener);
    }

    private void onMessageError() {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.VISIBLE);
    }

    private void onMessageInProgress() {
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }

    private EaseDingMessageHelper.IAckUserUpdateListener userUpdateListener =
            new EaseDingMessageHelper.IAckUserUpdateListener() {
                @Override
                public void onUpdate(List<String> list) {
                    onAckUserUpdate(list.size());
                }
            };
}
