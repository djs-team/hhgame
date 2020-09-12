package com.deepsea.mua.im.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.im.R;
import com.deepsea.mua.im.adapter.EaseMessageAdapter;
import com.deepsea.mua.im.model.EaseDingMessageHelper;
import com.deepsea.mua.im.utils.EaseSmileUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

public class EaseChatGiveGift extends EaseChatRow {

    private TextView contentView;
    private EaseMessageAdapter adapter;
    ImageView imageView;


    public EaseChatGiveGift(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        this.adapter= (EaseMessageAdapter) adapter;

    }
    @Override
    protected void onInflateView() {
        View view=inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_gift_received_message : R.layout.ease_gift_sent_message, this);
         imageView=view.findViewById(R.id.img_gift);

    }


    @Override
    protected void onFindViewById() {
        contentView = findViewById(R.id.tv_chatcontent);
    }
    @Override
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        Spannable span = null;
        span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);

        String gif_url=message.getStringAttribute("gift_url",null);
        if (gif_url!=null) {

            GlideUtils.loadImage(imageView, gif_url, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);

        }

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
