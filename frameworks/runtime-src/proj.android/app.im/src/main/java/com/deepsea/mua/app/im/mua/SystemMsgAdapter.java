package com.deepsea.mua.app.im.mua;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ItemSystemMsgBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.stub.entity.DynamicDetailReplylistBean;
import com.deepsea.mua.stub.entity.DynamicMsgExt;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.Locale;

/**
 * Created by JUN on 2019/8/27
 */
public class SystemMsgAdapter extends BaseBindingAdapter<EMMessage, ItemSystemMsgBinding> {

    private OnItemLongClickListener mOnItemLongClickListener;

    public SystemMsgAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_system_msg;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    protected void bind(BindingViewHolder<ItemSystemMsgBinding> holder, EMMessage item) {
        TextView timestampTv = holder.binding.msgTimeTv;

        int position = holder.getAdapterPosition();
        if (position == 0) {
            timestampTv.setText(DateUtils.getTimestampString(new Date(item.getMsgTime())));
            timestampTv.setVisibility(View.VISIBLE);
        } else {
            // show time stamp if interval with last message is > 30 seconds
            EMMessage prevMessage = getItem(position - 1);
            if (prevMessage != null && DateUtils.isCloseEnough(item.getMsgTime(), prevMessage.getMsgTime())) {
                timestampTv.setVisibility(View.GONE);
            } else {
                timestampTv.setText(DateUtils.getTimestampString(new Date(item.getMsgTime())));
                timestampTv.setVisibility(View.VISIBLE);
            }
        }

        final String extKey = "XYServerData";
        if (item.ext() != null && item.ext().containsKey(extKey)) {
            String ext = (String) item.ext().get(extKey);

            Log.d("SystemMsgAdapter", "ext = " + ext);

            DynamicMsgExt msgExt = JsonConverter.fromJson(ext, DynamicMsgExt.class);
            if (msgExt != null) {

                switch (msgExt.getCustomEaseMessageType()) {
                    case CustomMsgType.CustomEaseMessageTypeDynamic:
                        holder.setVisible(holder.binding.dynamicLayout, true);
                        boolean isReplyDy = msgExt.getForum() != null;
                        DynamicDetailReplylistBean.ListEntity addReply = msgExt.getAddReply();

                        GlideUtils.circleImage(holder.binding.avatarIv, addReply.reply_uid_avatar, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
                        holder.binding.titleTv.setText(String.format(Locale.CHINA, "%s 回复了你的%s", addReply.reply_uid_nickname, isReplyDy ? "动态" : "评论"));
                        holder.binding.replyTv.setText(addReply.reply_content);

                        //发帖/评论时间
                        String createTime = isReplyDy ? msgExt.getForum().createtime : msgExt.getReply().createtime;
                        long date = com.deepsea.mua.core.utils.DateUtils.parseDate(createTime, "yyyy-MM-dd HH:mm:ss");
//                        String timeFormatText = com.deepsea.mua.core.utils.DateUtils.getTimeFormatText(date);
                        String timeFormatText = com.deepsea.mua.core.utils.DateUtils.format(date, "yyyy年MM月dd日");
                        holder.binding.createTimeTv.setText(timeFormatText);

                        //评论数
                        int replyNum = isReplyDy ? msgExt.getForum().reply_num : msgExt.getReply().reply_num;
                        holder.binding.replyNumTv.setText(replyNum + "评论");

                        //发帖/评论内容
                        if (isReplyDy) {
                            if (!TextUtils.isEmpty(msgExt.getForum().forum_content)) {
                                holder.binding.contentTv.setTextColor(0xFFBBBBBB);
                                holder.binding.contentTv.setText(msgExt.getForum().forum_content);
                            } else {
                                holder.binding.contentTv.setTextColor(0xFF8B97FF);
                                if (!TextUtils.isEmpty(msgExt.getForum().forum_image)) {
                                    int length = msgExt.getForum().forum_image.split(",").length;
                                    for (int i = 0; i < length; i++) {
                                        holder.binding.contentTv.append("【图片】");
                                    }
                                } else {
                                    holder.binding.contentTv.setText("【语音】");
                                }
                            }
                        } else {
                            holder.binding.contentTv.setTextColor(0xFFBBBBBB);
                            holder.binding.contentTv.setText(msgExt.getReply().reply_content);
                        }

                        String forumId = msgExt.getAddReply().forum_id;

                        holder.binding.dynamicLayout.setOnClickListener(v -> {
                            if (!TextUtils.isEmpty(forumId)) {
                                PageJumpUtils.jumpToDynamicDetail(forumId, false);
                            }
                        });

                        holder.binding.dynamicLayout.setOnLongClickListener(v -> {
                            if (mOnItemLongClickListener != null) {
                                mOnItemLongClickListener.onItemLongClick(v, position);
                            }
                            return false;
                        });

                        break;
                    default:
                        holder.setVisible(holder.binding.dynamicLayout, false);
                        break;
                }


            }
        }
    }

    interface CustomMsgType {
        int CustomEaseMessageTypeNone = 0;
        int CustomEaseMessageTypeOfficial = 1;
        int CustomEaseMessageTypeDynamic = 2;
    }
}
