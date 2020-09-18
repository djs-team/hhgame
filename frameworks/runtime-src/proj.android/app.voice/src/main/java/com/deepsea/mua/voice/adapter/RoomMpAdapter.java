package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.view.SkipTextView;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRoomMicroBinding;
import com.deepsea.mua.voice.view.MicroFaceView;
import com.deepsea.mua.voice.view.TextureVideoViewOutlineProvider;
import com.uuzuche.lib_zxing.DisplayUtil;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by JUN on 2019/3/26
 */
public class RoomMpAdapter extends BaseBindingAdapter<RoomData.MicroInfosBean, ItemRoomMicroBinding> {

    private boolean isOpenHeart = false;
    private OnMicUserListener onMicUserListener;

    public interface OnMPClickLister {
        void onMpClick(int pos);
    }

    private OnMPClickLister onMPClickLister;

    public void setOnMPClickLister(OnMPClickLister onMPClickLister) {
        this.onMPClickLister = onMPClickLister;
    }

    public interface OnMicUserListener {
        void sendSingleGift(WsUser user);

        void addFriend(String uid, String imgUrl, String nickName);

        void fansList(WsUser user);

        void sendOneRose(String userId);

        void downMicro(String userId);//下麦

        void microOperate(int pos, String userId, boolean isOpen);//闭麦/禁言
    }

    public void setOnMicUserListener(OnMicUserListener onMicUserListener) {
        this.onMicUserListener = onMicUserListener;
    }

    public RoomMpAdapter(Context context) {
        super(context);
    }

    public void setOpenHeart(boolean openHeart) {
        isOpenHeart = openHeart;
        if (getItemCount() > 0) {
            notifyDataSetChanged();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_room_micro;
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<ItemRoomMicroBinding> holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        RoomData.MicroInfosBean item = getItem(position);

        int eventCode = (int) payloads.get(0);
        switch (eventCode) {
            case IMicroEvent.VoiceWave:
//                if (holder.binding.waveView.getVisibility() != View.VISIBLE) {
//                    holder.binding.waveView.setVisibility(View.VISIBLE);
//                }
//                holder.binding.waveView.newCircle();
                break;

            case IMicroEvent.GameResult:
                item.setActionUrl(null);
                holder.binding.animIv.loadGameResult(item.getResultUrl());
                break;

            case IMicroEvent.NormalEmojiAnima:
            case IMicroEvent.GameEmojiAnima:
            case IMicroEvent.GiftAnima:
                bindAnimation(holder, item);
                break;

            case IMicroEvent.MicroUserChanged:
                bindUser(holder, item);
                break;

            case IMicroEvent.CountDown:
                bindCountDown(holder, item);
                break;

            case IMicroEvent.LockMicro:
                holder.setVisible(holder.binding.lockIv, item.isIsLocked());
//                holder.binding.avatarIv.setVisibility(item.isIsLocked() ? View.INVISIBLE : View.VISIBLE);
                break;

            case IMicroEvent.ForbiddenMicro:
                holder.setVisible(holder.binding.forbidMicro, item.isIsDisabled());
                break;

            case IMicroEvent.HeartValue:
                bindHeartValue(holder, item);
                break;
            case IMicroEvent.RoseValue:
                holder.binding.faceView.setRoleNum(item.getRolse());
                break;
            case IMicroEvent.AddFriendState:
                holder.binding.faceView.updateFriendState(item);
                break;
            case IMicroEvent.UpdateRanks:
                holder.binding.faceView.updateRankHeads(item.getRoseRanks());
                break;
            case IMicroEvent.ReleaseLayout:
                holder.binding.faceView.releaseLayout(item.getType());
                break;
            case IMicroEvent.UpMpMicro:
                holder.binding.faceView.upDataMicroState(item.isOpenMp());

                break;
        }
    }


    private int roomMode = 1;

    public void setRoomMode(int roomMode) {
        this.roomMode = roomMode;
    }

    @Override
    public int getItemCount() {

        int size = 0;
        if (mData == null) {
            return 0;
        } else if (mData.size() > 2) {
            if (roomMode == 9) {
                return 2;
            } else {
                return mData.size();
            }
        } else {
            return mData.size();
        }
//        if (mData == null) {
//            return 0;
//        } else {
//            return 1;
//        }

    }

    private boolean isMusicType = false;

    public void setMusicType(boolean musicType) {
        isMusicType = musicType;
    }


    @Override
    protected void bind(BindingViewHolder<ItemRoomMicroBinding> holder, RoomData.MicroInfosBean item) {
//        if (item.isUpRose()) {
//            holder.binding.faceView.setRoleNum(item.getRolse());
//            item.setUpRose(false);
//            return;
//        }
        holder.binding.faceView.setOnMpClickListener(new MicroFaceView.OnMpClickListener() {
            @Override
            public void onClick(WsUser user) {
                if (onMPClickLister != null) {
                    onMPClickLister.onMpClick(holder.getLayoutPosition());
                }
            }
        });
        int roomMode = AppConstant.getInstance().getJoinRoom().getRoomMode();

        int pos = holder.getLayoutPosition();
        if (isMusicType) {
            if (roomMode == 5 || roomMode == 6 || (roomMode == 8 && getData() != null && getData().size() == 2)) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 167));
                holder.binding.llCheckMain.setLayoutParams(params);
            }
        } else {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (pos == 0) {
                params.rightMargin = DisplayUtil.dip2px(mContext, 3);
            } else {
                params.leftMargin = DisplayUtil.dip2px(mContext, 3);
            }
            holder.binding.llCheckMain.setLayoutParams(params);

        }
        //用户信息
        bindUser(holder, item);
        //心动值
//        bindHeartValue(holder, item);
        //锁麦、禁言
//        holder.setVisible(holder.binding.forbidMicro, item.isIsDisabled());
//        holder.setVisible(holder.binding.lockIv, item.isIsLocked());
        //动画
        bindAnimation(holder, item);
        //倒计时
//        bindCountDown(holder, item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.binding.llCheckMain.setOutlineProvider(new TextureVideoViewOutlineProvider(DisplayUtil.dip2px(mContext, 8)));
            holder.binding.llCheckMain.setClipToOutline(true);
        }
    }

    private void bindUser(BindingViewHolder<ItemRoomMicroBinding> holder, RoomData.MicroInfosBean item) {

        holder.binding.faceView.setMicroData(item);
        holder.binding.faceView.setOnMicUserListener(new MicroFaceView.OnMicUserListener() {
            @Override
            public void sendSingleGift(WsUser user) {
                if (onMicUserListener != null) {
                    onMicUserListener.sendSingleGift(user);
                }
            }

            @Override
            public void addFriend(String uid, String imgUrl, String nickName) {
                if (onMicUserListener != null) {
                    onMicUserListener.addFriend(uid, imgUrl, nickName);
                }
            }

            @Override
            public void fansList(WsUser user) {
                if (onMicUserListener != null) {
                    onMicUserListener.fansList(user);
                }
            }

            @Override
            public void sendOneRose(String userId) {
                if (onMicUserListener != null) {
                    onMicUserListener.sendOneRose(userId);
                    ;
                }
            }

            @Override
            public void downMicro(String userId) {
                if (onMicUserListener != null) {
                    onMicUserListener.downMicro(userId);
                }
            }

            @Override
            public void microOperate(String userId, boolean isOpen) {
                if (onMicUserListener != null) {
                    onMicUserListener.microOperate(holder.getLayoutPosition(), userId, isOpen);
                }
            }
        });


    }

    private void bindHeartValue(BindingViewHolder<ItemRoomMicroBinding> holder, RoomData.MicroInfosBean item) {
        holder.setVisible(holder.binding.microXdTv, isOpenHeart);
        holder.binding.microXdTv.setText(FormatUtils.formatTenThousand(item.getXinDongZhi()));
        if (item.getXinDongZhi() >= 0) {
            holder.binding.microXdTv.setBackgroundResource(R.drawable.micro_heart_positive);
        } else {
            holder.binding.microXdTv.setBackgroundResource(R.drawable.micro_heart_negative);
        }
    }

    private void bindAnimation(BindingViewHolder<ItemRoomMicroBinding> holder, RoomData.MicroInfosBean item) {
        if (!TextUtils.isEmpty(item.getResultUrl())) {
            holder.binding.animIv.loadGameResult(item.getResultUrl());
            item.setActionUrl(null);
            item.setResultUrl(null);
        } else if (!TextUtils.isEmpty(item.getActionUrl())) {
            holder.binding.animIv.loadGameAnim(item.getActionUrl());
            item.setActionUrl(null);
        } else if (!TextUtils.isEmpty(item.getAnimUrl())) {
            holder.binding.animIv.loadNormalAnim(item.getAnimUrl());
            item.setAnimUrl(null);
        } else {
            holder.binding.animIv.setVisibility(View.GONE);
        }
    }

    private void bindCountDown(BindingViewHolder<ItemRoomMicroBinding> holder, RoomData.MicroInfosBean item) {
        if (item.getDaojishiShichang() > 0) {
            Date date = new DateTime(item.getDaojishiShijiandian()).toDate();
            long timeMillis = System.currentTimeMillis();
            long interval = (timeMillis - date.getTime()) / 1000;
            if (interval >= item.getDaojishiShichang()) {
                holder.setVisible(holder.binding.skipTv, false);
            } else {
                holder.setVisible(holder.binding.skipTv, true);
                long seconds = item.getDaojishiShichang() - interval;
                holder.binding.skipTv.setSkipListener(new SkipTextView.SkipListener() {
                    @Override
                    public void onSkip(SkipTextView view, long duration) {
                        if (duration == 0) {
                            holder.setVisible(holder.binding.skipTv, false);
                        }
                        holder.binding.skipTv.setText(duration + "s");
                    }

                    @Override
                    public void onFinished() {
                        holder.setVisible(holder.binding.skipTv, false);
                    }
                });
                holder.binding.skipTv.setSkipDuration(seconds);
                holder.binding.skipTv.startCountDown();
            }
        } else {
            holder.setVisible(holder.binding.skipTv, false);
        }
    }
}
