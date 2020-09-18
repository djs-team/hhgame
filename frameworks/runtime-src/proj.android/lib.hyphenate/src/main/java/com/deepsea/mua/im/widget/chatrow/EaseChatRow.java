package com.deepsea.mua.im.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.im.EaseUI;
import com.deepsea.mua.im.R;
import com.deepsea.mua.im.adapter.EaseMessageAdapter;
import com.deepsea.mua.im.domain.EaseAvatarOptions;
import com.deepsea.mua.im.model.styles.EaseMessageListItemStyle;
import com.deepsea.mua.im.utils.ChatUserInfoController;
import com.deepsea.mua.im.utils.EaseUserUtils;
import com.deepsea.mua.im.widget.EaseChatMessageList;
import com.deepsea.mua.im.widget.EaseImageView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;

import java.util.Date;

public abstract class EaseChatRow extends LinearLayout {
    public interface EaseChatRowActionCallback {
        void onResendClick(EMMessage message);

        void onBubbleClick(EMMessage message);

        void onDetachedFromWindow();

        void onChangeWxClick(EMMessage message, TextView btn_changewx_receive);

        void onGiftClick(EMMessage message, ImageView img_gift);
    }

    protected static final String TAG = EaseChatRow.class.getSimpleName();

    protected LayoutInflater inflater;
    protected Context context;
    protected BaseAdapter adapter;
    protected EMMessage message;
    protected int position;

    protected TextView timeStampView;
    protected ImageView userAvatarView;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected TextView percentageView;
    protected ProgressBar progressBar;
    protected ImageView statusView;
    protected Activity activity;

    protected TextView ackedView;
    protected TextView deliveredView;

    protected EaseChatMessageList.MessageListItemClickListener itemClickListener;
    protected EaseMessageListItemStyle itemStyle;

    private EaseChatRowActionCallback itemActionCallback;
    protected TextView btn_changewx_receive;
    protected ImageView img_gift;

    public EaseChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        this.activity = (Activity) context;
        inflater = LayoutInflater.from(context);

        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        itemActionCallback.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    public void updateView(final EMMessage msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onViewUpdate(msg);
            }
        });
    }

    private void initView() {
        onInflateView();
        timeStampView = findViewById(R.id.timestamp);
        userAvatarView = findViewById(R.id.iv_userhead);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = findViewById(R.id.tv_userid);

        progressBar = findViewById(R.id.progress_bar);
        statusView = findViewById(R.id.msg_status);
        ackedView = findViewById(R.id.tv_ack);
        deliveredView = findViewById(R.id.tv_delivered);
        btn_changewx_receive = findViewById(R.id.btn_changewx_receive);
        img_gift = findViewById(R.id.img_gift);

        onFindViewById();
    }

    /**
     * set property according message and postion
     *
     * @param message
     * @param position
     */
    public void setUpView(EMMessage message, int position,
                          EaseChatMessageList.MessageListItemClickListener itemClickListener,
                          EaseChatRowActionCallback itemActionCallback,
                          EaseMessageListItemStyle itemStyle) {
        this.message = message;
        this.position = position;
        this.itemClickListener = itemClickListener;
        this.itemActionCallback = itemActionCallback;
        this.itemStyle = itemStyle;
        try {
            String action = message.getStringAttribute("action");

            if (action.equals("cwx_request")) {
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    btn_changewx_receive.setVisibility(VISIBLE);
                }
                btn_changewx_receive.setText("接受");
                if (action != null && action.equals("cwx_request")) {
                    boolean isAccept = message.getBooleanAttribute("isAccept", false);
                    if (isAccept) {
                        btn_changewx_receive.setEnabled(false);
                        btn_changewx_receive.setBackgroundResource(R.drawable.tv_chat_btn_received_bg);
                    } else {
                        btn_changewx_receive.setEnabled(true);
                        btn_changewx_receive.setBackgroundResource(R.drawable.tv_chat_btn_unreceive_bg);

                    }
                }
            } else if (action.equals("starWxNumSend")) {
                btn_changewx_receive.setVisibility(GONE);
            } else if (action.equals("copyMsgOne") || action.equals("copyMsgTwo") || action.equals("copyMsgTwoOver")) {
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    btn_changewx_receive.setVisibility(VISIBLE);
                }
                btn_changewx_receive.setText("复制");
                btn_changewx_receive.setBackgroundResource(R.drawable.tv_chat_btn_copy_bg);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        setUpBaseView();
        onSetUpView();
        setClickListener();
    }

    private void setUpBaseView() {
        // set nickname, avatar and background of bubble
        TextView timestamp = findViewById(R.id.timestamp);
        if (timestamp != null) {
            if (position == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
                // show time stamp if interval with last message is > 30 seconds
                EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }
        if (userAvatarView != null) {
            //set nickname and avatar
            String url;

            if (message.direct() == Direct.SEND) {
                url = "";
                GlideUtils.circleImage(userAvatarView, url, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
                EaseUserUtils.setUserAvatar(context, EMClient.getInstance().getCurrentUser(), userAvatarView);
            } else {
                GlideUtils.circleImage(userAvatarView, ChatUserInfoController.getInstance().getTochatUserAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);

//                EaseUserUtils.setUserAvatar(context, message.getFrom(), userAvatarView);
                EaseUserUtils.setUserNick(message.getFrom(), usernickView);
            }
        }
        if (btn_changewx_receive != null) {
            btn_changewx_receive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemActionCallback != null) {
                        itemActionCallback.onChangeWxClick(message, btn_changewx_receive);
                    }
                }
            });
        }
        if (img_gift != null) {
            img_gift.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemActionCallback != null) {
                        itemActionCallback.onGiftClick(message, img_gift);
                    }
                }
            });
        }
        if (EMClient.getInstance().getOptions().getRequireDeliveryAck()) {
            if (deliveredView != null) {
                if (message.isDelivered()) {
                    deliveredView.setVisibility(View.VISIBLE);
                } else {
                    deliveredView.setVisibility(View.INVISIBLE);
                }
            }
        }
        if (EMClient.getInstance().getOptions().getRequireAck()) {
            if (ackedView != null) {
                if (message.isAcked()) {
                    if (deliveredView != null) {
                        deliveredView.setVisibility(View.INVISIBLE);
                    }
                    ackedView.setVisibility(View.VISIBLE);
                } else {
                    ackedView.setVisibility(View.INVISIBLE);
                }
            }
        }

        if (itemStyle != null) {
            if (userAvatarView != null) {
                if (itemStyle.isShowAvatar()) {
                    userAvatarView.setVisibility(View.VISIBLE);
                    EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
                    if (avatarOptions != null && userAvatarView instanceof EaseImageView) {
                        EaseImageView avatarView = ((EaseImageView) userAvatarView);
                        if (avatarOptions.getAvatarShape() != 0)
                            avatarView.setShapeType(avatarOptions.getAvatarShape());
                        if (avatarOptions.getAvatarBorderWidth() != 0)
                            avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
                        if (avatarOptions.getAvatarBorderColor() != 0)
                            avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
                        if (avatarOptions.getAvatarRadius() != 0)
                            avatarView.setRadius(avatarOptions.getAvatarRadius());
                    }
                } else {
                    userAvatarView.setVisibility(View.GONE);
                }
            }
            if (usernickView != null) {
                if (itemStyle.isShowUserNick())
                    usernickView.setVisibility(View.VISIBLE);
                else
                    usernickView.setVisibility(View.GONE);
            }
            if (bubbleLayout != null) {
                if (message.direct() == Direct.SEND) {
                    if (itemStyle.getMyBubbleBg() != null) {
                        bubbleLayout.setBackground(((EaseMessageAdapter) adapter).getMyBubbleBg());
                    }
                } else if (message.direct() == Direct.RECEIVE) {
                    if (itemStyle.getOtherBubbleBg() != null) {
                        bubbleLayout.setBackground(((EaseMessageAdapter) adapter).getOtherBubbleBg());
                    }
                }
            }
        }

    }

    private void setClickListener() {
        if (bubbleLayout != null) {
            bubbleLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null && itemClickListener.onBubbleClick(message)) {
                        return;
                    }
                    if (itemActionCallback != null) {
                        itemActionCallback.onBubbleClick(message);
                    }
                }
            });

            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        if (statusView != null) {
            statusView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null && itemClickListener.onResendClick(message)) {
                        return;
                    }
                    if (itemActionCallback != null) {
                        itemActionCallback.onResendClick(message);
                    }
                }
            });
        }

        if (userAvatarView != null) {
            userAvatarView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (message.direct() == Direct.SEND) {
                            itemClickListener.onUserAvatarClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarClick(message.getFrom());
                        }
                    }
                }
            });
            userAvatarView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        if (message.direct() == Direct.SEND) {
                            itemClickListener.onUserAvatarLongClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarLongClick(message.getFrom());
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    protected abstract void onInflateView();

    /**
     * find view by id
     */
    protected abstract void onFindViewById();

    /**
     * refresh view when message status change
     */
    protected abstract void onViewUpdate(EMMessage msg);

    /**
     * setup view
     */
    protected abstract void onSetUpView();
}
