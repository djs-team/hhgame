package com.deepsea.mua.app.im.mua;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ItemConversationBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.im.model.EaseAtMessageHelper;
import com.deepsea.mua.im.utils.EaseCommonUtils;
import com.deepsea.mua.im.utils.EaseSmileUtils;
import com.deepsea.mua.im.utils.EaseUserUtils;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;

import java.util.Date;

/**
 * Created by JUN on 2019/5/28
 */
public class ConversationAdapter extends BaseBindingAdapter<EMConversation, ItemConversationBinding> {

    public ConversationAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_conversation;
    }

    @Override
    protected void bind(BindingViewHolder<ItemConversationBinding> holder, EMConversation conversation) {

        boolean systemConversation = isSystemConversation(conversation);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        lp.height = ResUtils.dp2px(mContext, systemConversation ? 87 : 77);
        holder.itemView.setLayoutParams(lp);
        holder.itemView.setPadding(0, 0, 0, systemConversation ? ResUtils.dp2px(mContext, 10) : 0);

        int position = holder.getAdapterPosition();
        if (systemConversation) {
            holder.itemView.setBackgroundResource(R.drawable.conversation_gray_bg);
        } else {
            EMConversation preEMConversation = getItem(position - 1);
            boolean isFirstNormal = preEMConversation == null || isSystemConversation(preEMConversation);
            holder.itemView.setBackgroundResource(isFirstNormal ? R.drawable.conversation_first_bg : R.drawable.conversation_item_bg);
        }


        // get username or group id
        String username = conversation.conversationId();

        if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
            String groupId = conversation.conversationId();
            if (EaseAtMessageHelper.get().hasAtMeMsg(groupId)) {
//                holder.motioned.setVisibility(View.VISIBLE);
            } else {
//                holder.motioned.setVisibility(View.GONE);
            }
            // group message, show group avatar
            holder.binding.avatarIv.setImageResource(R.drawable.ic_place_avatar);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
            holder.binding.nickTv.setText(group != null ? group.getGroupName() : username);
        } else if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
            holder.binding.avatarIv.setImageResource(R.drawable.ic_place_avatar);
            EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
            holder.binding.nickTv.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
//            holder.motioned.setVisibility(View.GONE);
        } else {
            EaseUserUtils.setUserAvatar(mContext, username, holder.binding.avatarIv);
            EaseUserUtils.setUserNick(username, holder.binding.nickTv);
//            holder.motioned.setVisibility(View.GONE);
        }

        if (conversation.getUnreadMsgCount() > 0) {
            // show unread message count
            String count = conversation.getUnreadMsgCount() > 99 ? "..." : conversation.getUnreadMsgCount() + "";
            holder.binding.unreadMsgNumber.setText(count);
            holder.binding.unreadMsgNumber.setVisibility(View.VISIBLE);
        } else {
            holder.binding.unreadMsgNumber.setVisibility(View.INVISIBLE);
        }

        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            EMMessage lastMessage = conversation.getLastMessage();
            holder.binding.messageTv.setText(EaseSmileUtils.getSmiledText(mContext, EaseCommonUtils.getMessageDigest(lastMessage, mContext)),
                    TextView.BufferType.SPANNABLE);
            holder.binding.timeTv.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
        }
    }

    public boolean isSystemConversation(EMConversation conversation) {
        try {
            String conversationId = conversation.conversationId();
            return FormatUtils.isNumber(conversationId) && Long.parseLong(conversationId) > 100 && Long.parseLong(conversationId) <= 150;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void remove(int position) {
        if (mData != null && position < mData.size()) {
            mData.remove(position);
//            notifyItemRemoved(position);
            if (position != mData.size() - 1) {
//                notifyItemRangeChanged(position, mData.size() - position);
            }
            notifyDataSetChanged();
        }
    }
}
