package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.core.view.chat.BaseChatAdapter;
import com.deepsea.mua.core.view.chat.BaseChatViewHolder;
import com.deepsea.mua.stub.controller.RoomMsgHandler;
import com.deepsea.mua.stub.entity.model.RoomMsgBean;
import com.deepsea.mua.stub.entity.socket.SmashBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.span.LevelResUtils;
import com.deepsea.mua.stub.utils.span.UrlImageSpan;
import com.deepsea.mua.stub.view.WithBackgroundTextView;
import com.deepsea.mua.voice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by JUN on 2019/3/26
 */
public class RoomMsgAdapter extends BaseChatAdapter<RoomMsgBean> {

    private static final int TYPE_HEADER = -100;

    private List<RoomMsgBean> mDatas;

    private List<View> mHeaders;


    private RoomMsgHandler.OnMsgEventListener mListener;

    public void addHeader(View header) {
        if (mHeaders == null) {
            mHeaders = new ArrayList<>();
        }
        if (!mHeaders.contains(header)) {
            mHeaders.add(header);
            notifyItemInserted(mHeaders.size() - 1);
        }
    }

    public void setOnMsgEventListener(RoomMsgHandler.OnMsgEventListener listener) {
        this.mListener = listener;
    }

    private int getHeaderSize() {
        return mHeaders == null ? 0 : mHeaders.size();
    }

    @Override
    public synchronized void addItem(RoomMsgBean chatMsg) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.add(chatMsg);
        notifyItemInserted(getItemCount());
    }

    @Override
    public synchronized void addItemList(List<RoomMsgBean> list) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        int startPos = getItemCount();
        int addSize = list.size();
        mDatas.addAll(list);
        notifyItemRangeInserted(startPos, addSize);
//        notifyDataSetChanged();
    }

    @Override
    public synchronized void removeItems(int startPos, int endPos) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.subList(startPos, endPos).clear();
//        notifyDataSetChanged();
        notifyItemRangeRemoved(startPos, endPos - startPos);
    }

    private boolean isHeader(int position) {
        if (mHeaders == null)
            return false;

        return (position < mHeaders.size());
    }

    @NonNull
    @Override
    public BaseChatViewHolder<RoomMsgBean> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_HEADER) {
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT));
            return new HeaderViewHolder(frameLayout);
        }

        View view = View.inflate(viewGroup.getContext(), R.layout.item_voice_room_msg, null);

        MsgHolder msgHolder = new MsgHolder(view);
        msgHolder.setOnMsgEventListener(mListener);
        return msgHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseChatViewHolder<RoomMsgBean> holder, int position) {

        if (isHeader(position)) {
            if (holder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) holder).bindData(mHeaders.get(position));
            }
        } else {
            holder.bindData(mDatas.get(position - getHeaderSize()), position);
        }
    }

    @Override
    public int getItemCount() {
        int size = mDatas == null ? 0 : mDatas.size();
        return getHeaderSize() + size;
    }

    @Override
    public final int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        }
        return position;
    }

    public static class HeaderViewHolder extends BaseChatViewHolder<RoomMsgBean> {
        private FrameLayout base;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            base = (FrameLayout) itemView;
        }

        @Override
        public void bindData(RoomMsgBean data, int position) {
        }

        public void bindData(View view) {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            base.removeAllViews();
            base.addView(view);

        }
    }

    public class MsgHolder extends BaseChatViewHolder<RoomMsgBean> {

        private Context mContext;

        private RoomMsgHandler.OnMsgEventListener mListener;

        public MsgHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
        }

        public void setOnMsgEventListener(RoomMsgHandler.OnMsgEventListener listener) {
            this.mListener = listener;
        }


        @Override
        public void bindData(RoomMsgBean item, int position) {

            TextView msgTv = (TextView) getView(R.id.room_msg_tv);
            ImageView avatarIv = (ImageView) getView(R.id.avatar_iv);
            ImageView avatarGuardBg = (ImageView) getView(R.id.avatar_guard_bg);
            RelativeLayout avatarRl = (RelativeLayout) getView(R.id.avatar_rl);
//            levelTv.setVisibility(item.getLevel() > 0 ? View.VISIBLE : View.GONE);
//            levelTv.setWithBackgroundColor(LevelResUtils.getSystemMsgColor(item.getLevel()));
//            levelTv.setText("LV" + item.getLevel());
            ConstraintLayout msg_layout = (ConstraintLayout) getView(R.id.cl_group);
            avatarGuardBg.setVisibility(TextUtils.isEmpty(item.getGuardSign())?View.GONE:View.VISIBLE);


            if (item == null)
                return;
//            msgTv.setHighlightColor(Color.TRANSPARENT);
//            msgTv.setMovementMethod(LinkMovementMethod.getInstance());
//            msgTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.isNormal() ? 12 : 14);

            ViewBindUtils.setVisible(avatarRl, !TextUtils.isEmpty(item.getAvatar()));
            if (!TextUtils.isEmpty(item.getAvatar())) {
                GlideUtils.circleImage(avatarIv, item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            }
            avatarRl.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(item.getUid())) {
                    if (mListener != null) {
                        mListener.onMsgClick(item.getUid());
                    }
                }
            });
            msg_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!TextUtils.isEmpty(item.getUid())) {
                        if (mListener != null) {
                            mListener.onMsgLongClick(item.getuName());
                        }
                    }
                    return true;
                }
            });

            SpannableStringBuilder builder = item.getMsg();

            List<SmashBean> list = item.getList();
            if (list != null && builder.length() == item.getStart()) {
                for (SmashBean bean : list) {
                    if (bean == null || bean.getGiftData() == null)
                        continue;

                    int start = builder.length();
                    String temp = "\uFFFC";
                    builder.append(temp);
                    UrlImageSpan span = new UrlImageSpan(mContext, msgTv, bean.getGiftData().getImage());
                    builder.setSpan(span, start, start + temp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                    //count
                    String count = String.format(Locale.CHINA, "X%d ", bean.getCount());
                    builder.append(count);
                    ForegroundColorSpan msgSpan = new ForegroundColorSpan(0xFFFF4D4D);
                    builder.setSpan(msgSpan, builder.length() - count.length(), builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }

            if (!TextUtils.isEmpty(item.getUrl()) && builder.length() == item.getStart()) {
                String append = "\uFFFC";
                UrlImageSpan span = new UrlImageSpan(mContext, msgTv, item.getUrl());
                builder.append(append);
                builder.setSpan(span, builder.length() - append.length(), builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                if (item.getCount() > 1) {
                    String count = String.format(Locale.CHINA, "X%d ", item.getCount());
                    builder.append(count);
                    ForegroundColorSpan msgSpan = new ForegroundColorSpan(0xFFFFEB93);
                    builder.setSpan(msgSpan, builder.length() - count.length(), builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            msgTv.setText(item.getMsg());
            msgTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(item.getUid())) {
                        if (mListener != null) {
                            mListener.onMsgClick(item.getUid());
                        }
                    }
                }
            });
            msgTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!TextUtils.isEmpty(item.getUid())) {
                        if (mListener != null) {
                            mListener.onMsgLongClick(item.getuName());
                        }
                    }
                    return true;
                }
            });
        }
    }
}
