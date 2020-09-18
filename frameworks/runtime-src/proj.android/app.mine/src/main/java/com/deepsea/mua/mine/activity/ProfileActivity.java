package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.im.domain.EaseUser;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.MyTagsAdapter;
import com.deepsea.mua.mine.adapter.PresentWallAdapter;
import com.deepsea.mua.mine.databinding.ActivityProfileBinding;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.client.hyphenate.HyphenateClient;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.dialog.AbsDialog;
import com.deepsea.mua.stub.entity.ProfileBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.deepsea.mua.stub.utils.NumberUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.ViewUtils;
import com.deepsea.mua.stub.utils.eventbus.ClickEvent;
import com.deepsea.mua.stub.utils.eventbus.ClickEventType;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/12
 */
@Route(path = ArouterConst.PAGE_PROFILE)
public class ProfileActivity extends BaseActivity<ActivityProfileBinding> {

    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    RoomJoinController mRoomJumper;
    private ProfileViewModel mViewModel;

    private AbsDialog mAbsDialog;
    private AAlertDialog mAlertDialog;

    @Autowired
    String uid;
    String photo;
    String nickName;
    String onlineState;
    private String roomId;
    private ProfileBean.UserInfoBean mProfile;//用户基本信息
    ProfileBean mProfileUserInfo;//用户所有信息

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        uid = savedInstanceState.getString("uid");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("uid", uid);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra("uid")) {
            uid = intent.getStringExtra("uid");
            toggleViewStatus();
            getProfileInfo();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile;
    }

    PresentWallAdapter mEasyAdapter;
    MyTagsAdapter tagsAdapter;

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel.class);
        registerEventBus(ProfileActivity.this);
        toggleViewStatus();
        //礼物RecycleView
        mEasyAdapter = new PresentWallAdapter(R.layout.present_wall_item);
        mBinding.gRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mBinding.gRecyclerView.setAdapter(mEasyAdapter);
        ViewUtils.setViewSize(mBinding.banner, 360, 300);
        tagsAdapter = new MyTagsAdapter(mContext);
        mBinding.rvTag.setLayoutManager(new GridLayoutManager(mContext, 3));
        mBinding.rvTag.setAdapter(tagsAdapter);

        getProfileInfo();
    }

    private void toggleViewStatus() {
        ViewBindUtils.setVisible(mBinding.addFriendTv, !TextUtils.equals(uid, UserUtils.getUser().getUid()));
        mBinding.operateIv.setBackground(!TextUtils.equals(uid, UserUtils.getUser().getUid()) ? getResources().getDrawable(R.drawable.ic_operate) : getResources().getDrawable(R.drawable.profile_edit));
    }

    private void getProfileInfo() {
        mViewModel.user_info(uid).observe(this, new BaseObserver<ProfileBean>() {
            @Override
            public void onSuccess(ProfileBean result) {
                if (result != null) {
                    //更新本地用户数据
                    EaseUser user = new EaseUser(uid);
                    user.setNickname(result.getUser_info().getNickname());
                    user.setAvatar(result.getUser_info().getAvatar());
                    HyphenateClient.getInstance().saveContact(user);

                    ProfileBean.MemberAvatarBean bean = new ProfileBean.MemberAvatarBean();
                    bean.setPhoto_url(result.getUser_info().getAvatar());
                    initBanner(Collections.singletonList(bean));
                    initProfile(result);
                    mBinding.giftCountTv.setText("(" + NumberUtils.formatNum(result.gift_num + "", false) + ")");
                    if (result.rank_gift != null && result.rank_gift.size() > 0 && result.gift_num > 0) {//有礼物
                        mEasyAdapter.getData().clear();
                        mEasyAdapter.addData(result.rank_gift);
                        mBinding.gRecyclerView.setVisibility(View.VISIBLE);
                        mBinding.giftMore.setVisibility(View.VISIBLE);
                        mBinding.noGift.setVisibility(View.GONE);
                    } else {//没礼物
                        mBinding.gRecyclerView.setVisibility(View.GONE);
                        mBinding.giftMore.setVisibility(View.GONE);
                        mBinding.noGift.setVisibility(View.VISIBLE);
                    }
                    boolean isBlack = TextUtils.equals("2", mProfile.getIs_blocks());
                    upBlockView(isBlack);

                }
            }
        });
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.backIv, o -> finish());
        subscribeClick(mBinding.operateLl, o -> {
            if (mProfile == null)
                return;
            if ((uid.equals(UserUtils.getUser().getUid()))) {
                startActivity(new Intent(mContext, ProfileEditActivity.class));
            } else {
                showOperateDlg();
            }

        });
        subscribeClick(mBinding.voiceRoomCl, o -> {
            mRoomJumper.startJump(roomId, mContext);
        });
        subscribeClick(mBinding.giftMore, o -> {
            startActivity(PresentWallActivity.newIntent(mContext, uid));
        });

        //添加好友/聊天
        subscribeClick(mBinding.addFriendTv, o -> {
            if (isMyFriend) {
                ViewBindUtils.setText(mBinding.addFriendTv, "私聊好友");
                PageJumpUtils.jumpToChat(uid, nickName, onlineState);
            } else {
                PageJumpUtils.jumpToFriendAdd(uid, photo, nickName);
                ViewBindUtils.setText(mBinding.addFriendTv, "送礼物加好友");
            }
        });

        subscribeClick(mBinding.tvGuardGroup, o -> {
            PageJumpUtils.jumpToGuard(mContext, uid);
        });
    }


    private void showOperateDlg() {
        if (mAbsDialog == null) {
            mAbsDialog = new AbsDialog(mContext);
        }
        mAbsDialog.removeAllBody();

        boolean isBlack = TextUtils.equals("2", mProfile.getIs_blocks());

        mAbsDialog.addBody(isBlack ? "取消拉黑" : "拉黑", (view, dialog) -> {
            if (isBlack) {
                showBlackoutDlg();
            } else {
                showBlackDlg();
            }
        }).addBody("举报", 7, (view, dialog) -> {
            ArouterUtils.build(ArouterConst.PAGE_REPORT)
                    .withString("uid", uid)
                    .navigation();
        }).addBody("取消", null);
        mAbsDialog.showAtBottom();
    }

    private void showBlackDlg() {
        if (mAlertDialog == null) {
            mAlertDialog = new AAlertDialog(mContext);
        }
        mAlertDialog.setTitle("拉黑好友");
        mAlertDialog.setMessage("拉黑好友之后将无法再接TA的任何消息。");
        mAlertDialog.getMessageTv().setGravity(Gravity.LEFT);
        mAlertDialog.setLeftButton("确定", (v, dialog1) -> {
            dialog1.dismiss();
            defriend();
        });
        mAlertDialog.setRightButton("取消", null);
        mAlertDialog.show();
    }

    private void showBlackoutDlg() {
        if (mAlertDialog == null) {
            mAlertDialog = new AAlertDialog(mContext);
        }
        mAlertDialog.setTitle("取消拉黑");
        mAlertDialog.setMessage("是否将TA从黑名单中移除？");
        mAlertDialog.setLeftButton("确定", (v, dialog1) -> {
            dialog1.dismiss();
            blackout();
        });
        mAlertDialog.setRightButton("取消", null);
        mAlertDialog.show();
    }

    private void defriend() {
        mViewModel.defriend(uid).observe(this,
                new ProgressObserver<BaseApiResult>(mContext) {
                    @Override
                    public void onSuccess(BaseApiResult result) {
                        EMClient.getInstance().chatManager().deleteConversation(uid, true);
                        mProfile.setIs_blocks("2");
                        toastShort(result);
                    }
                });
    }

    private void blackout() {
        mViewModel.blackout(uid).observe(this,
                new ProgressObserver<BaseApiResult>(mContext) {
                    @Override
                    public void onSuccess(BaseApiResult result) {
                        mProfile.setIs_blocks("1");
                        toastShort(result);
                    }
                });
    }

    private void initBanner(List<ProfileBean.MemberAvatarBean> list) {
        mBinding.banner.setPages(new CBViewHolderCreator() {
            @Override
            public BannerHolder createHolder(View itemView) {
                return new BannerHolder(itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_img_banner;
            }
        }, list);
    }

    private boolean setViewText(TextView view, String prefix, String text) {
        boolean visible = !TextUtils.isEmpty(text);
        ViewBindUtils.setVisible(view, visible);
        ViewBindUtils.setText(view, prefix + text);
        return visible;
    }

    boolean isMyFriend = false;

    private void initProfile(ProfileBean result) {
        ProfileBean.UserInfoBean userInfo = result.getUser_info();
        mProfileUserInfo = result;
        mProfile = userInfo;
        photo = userInfo.getAvatar();
        nickName = userInfo.getNickname();
        mBinding.nickTv.setText(userInfo.getNickname());
        isMyFriend = userInfo.getIs_friend().equals("1");
        ViewBindUtils.setText(mBinding.addFriendTv, isMyFriend ? "私聊好友" : "送礼物加好友");
        userInfo.getOnline();
        onlineState = userInfo.getOnline();
        if (!TextUtils.isEmpty(onlineState)) {
            mBinding.tvOnlineState.setText(onlineState.equals("0") ? "离线" : "在线");
            mBinding.tvOnlineState.setBackgroundResource(onlineState.equals("0") ? R.drawable.bg_grayr_radius_21 : R.drawable.bg_green_radius_21);
        }
//        String state = item.getState();
//        if (!TextUtils.isEmpty(state)) {
//            ViewBindUtils.setVisible(mBinding.tvState, true);
//            if (state.equals("2") || state.equals("3")) {
//                //相亲中
//                ViewBindUtils.setVisible(mBinding.tvState, true);
//                mBinding.tvOnlineState.setBackgroundResource(R.drawable.bg_violet_radius_21);
//                ViewBindUtils.setText(mBinding.tvState, "相亲中");
//            } else if (state.equals("4") || state.equals("5")) {
//                ViewBindUtils.setVisible(mBinding.tvState, true);
//                mBinding.tvOnlineState.setBackgroundResource(R.drawable.bg_pink_radius_21);
//                ViewBindUtils.setText(mBinding.tvState, "热聊中");
//            } else if (state.equals("6")) {
//                ViewBindUtils.setVisible(mBinding.tvState, true);
//                mBinding.tvOnlineState.setBackgroundResource(R.drawable.bg_yellow_radius_21);
//                ViewBindUtils.setText(mBinding.tvState, "开播中");
//            } else {
//                ViewBindUtils.setVisible(mBinding.tvState, false);
//            }
//        } else {
//            ViewBindUtils.setVisible(mBinding.tvState, false);
//        }
        mBinding.uidTv.setText("ID:" + userInfo.getPretty_id());
        setViewText(mBinding.singTv, "", userInfo.getIntro());
        SexResUtils.setSexImgInFindPage(mBinding.rlSex, mBinding.sexIv, userInfo.getSex());

        mBinding.titleNickTv.setText(userInfo.getNickname());
        String hobby = userInfo.getHobby();
        String feature = userInfo.getFeature();
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(hobby)) {
            String[] hobbyStr = hobby.split(",");
            for (String s : hobbyStr) {
                list.add(s);
            }
        }
        if (!TextUtils.isEmpty(feature)) {
            String[] featureStr = feature.split(",");
            for (String s : featureStr) {
                list.add(s);
            }
        }
        if (list.size() > 0)
            tagsAdapter.addData(list);


        //详细资料
        boolean shown = setViewText(mBinding.statureTv, "身高  ", mProfile.getStature() > 0 ? String.valueOf(mProfile.getStature()) : "");
        shown |= setViewText(mBinding.incomeTv, "月收入  ", mProfile.getPay());
        shown |= setViewText(mBinding.ageTv, "年龄  ", String.valueOf(mProfile.getAge()));
        shown |= setViewText(mBinding.professionTv, "职业  ", mProfile.getProfession());
        shown |= setViewText(mBinding.marriageTv, "婚姻状况  ", mProfile.getMarital_status());
        shown |= setViewText(mBinding.educationTv, "学历  ", mProfile.getEducation());
        shown |= setViewText(mBinding.houseTv, "住房情况  ", mProfile.getHousing_status());
        ViewBindUtils.setVisible(mBinding.infoTv, shown);

        //征友条件
        String friendAge = mProfile.getFriend_age();
        if (TextUtils.isEmpty(friendAge)) {
            mBinding.marriageConditionTv.setText("想要开启一段缘分。");
        } else {
            String ageRange = friendAge.replace(",", "-");
            if (friendAge.contains(",") && friendAge.split(",").length == 2) {
                String[] split = friendAge.split(",");
                if (TextUtils.equals(split[0], split[1])) {
                    ageRange = split[0];
                }
            }
            mBinding.marriageConditionTv.setText(String.format(Locale.CHINA, "我想找%s岁的异性。", ageRange));
        }


        //所在房间信息
        ProfileBean.RoomInfoBean roomInfo = result.getRoom_info();
        if (roomInfo != null && !TextUtils.isEmpty(roomInfo.getRoom_id())) {
            roomId = roomInfo.getRoom_id();
            GlideUtils.circleImage(mBinding.profileIv, roomInfo.getAvatar(), R.drawable.ic_place, R.drawable.ic_place);
            mBinding.roomNameTv.setText(roomInfo.getRoom_name());
            ViewBindUtils.setVisible(mBinding.voiceRoomCl, true);
            String inRoomId = SharedPrefrencesUtil.getData(mContext, "mRoomId", "mRoomId", "");
            ViewBindUtils.setEnable(mBinding.voiceRoomCl, !roomId.equals(inRoomId));
        } else {
            ViewBindUtils.setVisible(mBinding.voiceRoomCl, false);
        }
    }


    public class BannerHolder extends Holder<ProfileBean.MemberAvatarBean> {
        private ImageView imageView;

        public BannerHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView(View itemView) {
            imageView = (ImageView) itemView;
        }

        @Override
        public void updateUI(ProfileBean.MemberAvatarBean data) {
            GlideUtils.loadImage(imageView, data.getPhoto_url(), R.drawable.ic_place_default, R.drawable.ic_place_default);
        }
    }

    private void upBlockView(boolean isBlock) {
        ViewBindUtils.setVisible(mBinding.llGiftWall, !isBlock);
        ViewBindUtils.setVisible(mBinding.userInfoLayout, !isBlock);
        ViewBindUtils.setVisible(mBinding.voiceRoomCl, !isBlock);
        ViewBindUtils.setVisible(mBinding.tvIntroTitle, !isBlock);
        ViewBindUtils.setVisible(mBinding.singTv, !isBlock);
        ViewBindUtils.setVisible(mBinding.rvTag, !isBlock);
        ViewBindUtils.setVisible(mBinding.rlPrivacy, isBlock);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ClickEvent event) {
        switch (event.getClick()) {
            case ClickEventType.Click8://更新用户信息了
                getProfileInfo();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mRoomJumper.destroy();
        super.onDestroy();
        unregisterEventBus(ProfileActivity.this);
    }
}
