package org.cocos2dx.javascript.ui.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.beauty.VideoManager;
import com.android.beauty.faceunity.entity.FaceBean;
import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.app.im.ui.MessageMainFragment;
import com.deepsea.mua.core.log.Logg;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.mine.activity.InviteDialogActivity;
import com.deepsea.mua.mine.fragment.MineFragment;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.client.hyphenate.IEMMessageListener;
import com.deepsea.mua.stub.controller.OnlineController;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.controller.RoomMiniController;
import com.deepsea.mua.stub.controller.active.AppActiveController;
import com.deepsea.mua.stub.controller.active.OnAppActiveListener;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.dialog.UpMicroCardReceiveDialog;
import com.deepsea.mua.stub.entity.FaceRequestBean;
import com.deepsea.mua.stub.entity.FriendInfoListBean;
import com.deepsea.mua.stub.entity.HeartBeatBean;
import com.deepsea.mua.stub.entity.JumpRoomVo;
import com.deepsea.mua.stub.entity.MessageNumVo;
import com.deepsea.mua.stub.entity.PushEvent;
import com.deepsea.mua.stub.network.ResultValid;
import com.deepsea.mua.stub.utils.AppManager;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.FragmentTabEvent;
import com.deepsea.mua.stub.utils.FriendsUtils;
import com.deepsea.mua.stub.utils.LogoutUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.ClickEvent;
import com.deepsea.mua.stub.utils.eventbus.ClickEventType;
import com.deepsea.mua.stub.utils.eventbus.GotoShowEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteDialogCloseEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteDialogEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteOtherEvent;
import com.deepsea.mua.stub.utils.eventbus.MessageTipsEvent;
import com.deepsea.mua.stub.utils.eventbus.OpenRoom;
import com.deepsea.mua.stub.utils.eventbus.ShowMineDialog;
import com.deepsea.mua.stub.utils.eventbus.ShowRankStepOne;
import com.deepsea.mua.stub.utils.eventbus.ShowRankStepTwo;
import com.deepsea.mua.voice.fragment.VoiceFragment;
import com.deepsea.mua.voice.utils.AppConstant;
import com.hh.game.R;
import com.hh.game.databinding.ActivityMainBinding;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.cocos2dx.javascript.ui.main.viewmodel.MainViewModel;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import io.agora.rtc.RtcEngine;

@Route(path = ArouterConst.PAGE_MAIN)
public class MainActivity extends BaseActivity<ActivityMainBinding>
        implements OnAppActiveListener {
    private static final String TAB_VOICE = "tab_voice";
    private static final String TAB_MESSAGE = "tab_message";
    private static final String TAB_DYNAMIC = "tab_dynamic";
    private static final String TAB_MINE = "tab_mine";
    private static final String TAB_NEARBY = "tab_nearby";

    private final String[] TABS = {TAB_MESSAGE, TAB_VOICE, TAB_DYNAMIC, TAB_MINE, TAB_NEARBY};

    private VoiceFragment mVoiceFragment;
    private MessageMainFragment mMessageFragment;
    private MineFragment mMineFragment;

    private Fragment[] fragments;
    private int mPrePos;

    private long mExitTime = 0;


    private LoginAnotherReceiver mLoginAnotherReceiver;
    @Inject
    RoomJoinController mRoomJump;

    @Inject
    AppActiveController mActiveController;

    @Inject
    ViewModelFactory mFactory;
    private MainViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    boolean hasFaceBeauty = false;

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mFactory).get(MainViewModel.class);
        initNavigation();
        registerLoginAnotherReceiver();
        EMClient.getInstance().chatManager().addMessageListener(mEMListener);
        OnlineController.getInstance(mContext).startObservable();
        OnlineController.getInstance(mContext).startMarqueeObservable();
        registerEventBus(this);
        mActiveController.setOnAppActiveListener(this);
        mViewModel.loadAndSaveGifts();
        getMakeFace();
        mBinding.navigation.post(() -> {
            int[] location = new int[2];
            mBinding.navigation.getLocationOnScreen(location);
            RoomMiniController.getInstance().setOffsetY(location[1] - ResUtils.getStatusBarHeight(mContext));
        });
        AppConstant.getInstance().setFirstRun(true);
        hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
        boolean inroom = SharedPrefrencesUtil.getData(mContext, "inroom", "inroom", false);

        if (hasFaceBeauty) {
            int height = 1024;
            int width = 760;
//            mVideoManager = VideoManager.createInstance(mContext);
//            mVideoManager.deallocate();
//            mVideoManager.allocate(width, height, -1, com.android.beauty.constant.Constant.CAMERA_FACING_FRONT);
//            mVideoManager = VideoManager.createInstance(mContext);
//            mVideoManager.allocate(width, height, -1, com.android.beauty.constant.Constant.CAMERA_FACING_FRONT);
//
//            mVideoManager.deallocate();
//            mVideoManager.startCapture();
//            mVideoManager.stopCapture();

            showProgress();
            mVideoManager = VideoManager.createInstance(mContext);
//        mVideoManager.deallocate();
//        mHandler.sendEmptyMessageDelayed(1, 800);
            mVideoManager.allocate(width, height, -1, com.android.beauty.constant.Constant.CAMERA_FACING_FRONT);
            mVideoManager.deallocate();
            mVideoManager.detachToRTCEngine();
            mVideoManager.attachToRTCEngine(AgoraClient.create().rtcEngine());

            mVideoManager.stopCapture();
//            mVideoManager.startCapture();
            hideProgress();

        }
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String extrasData = null;
            if (bundle != null) {
                extrasData = bundle.getString(JPushInterface.EXTRA_EXTRA);
                PushEvent event = JsonConverter.fromJson(extrasData, PushEvent.class);
                PageJumpUtils.jumpPushDeeplink(event);
            }
        }
        getFriendList();
        User user = UserUtils.getUser();
//        是否可获赠上麦卡，1:可以   2:不可以
        if (user.getIs_receive() == 1) {
            showCardDialog();
        }
        if (user.getIs_apply_match() == 1) {
            //申请通过
            showApplySuccess();
        } else if (user.getIs_apply_match() == 2) {
            //申请拒绝
            showApplyFail(user.getRefuse_wx(), user.getRefuse_time());
        }
        subscribeClick(mBinding.btnTest1, o -> {
            resetLocalCame();
        });
        subscribeClick(mBinding.btnTest2, o -> {
            reset();
        });
        boolean fromLoginOut = com.deepsea.mua.stub.utils.AppConstant.getInstance().isLoginOut();
//        if (fromLoginOut) {
        com.deepsea.mua.stub.utils.AppConstant.getInstance().setLoginOut(false);
        showProgress();
        resetLocalCame();
        reset();
        hideProgress();
        getMessageNum();
    }

    private void showCardDialog() {
        UpMicroCardReceiveDialog dialog = new UpMicroCardReceiveDialog(mContext);
        dialog.setOnClickListener(new UpMicroCardReceiveDialog.OnClickListener() {
            @Override
            public void onDismiss() {
                UserUtils.getUser().setIs_receive(2);
            }

            @Override
            public void onConfirm() {
                jumpRoomOperate();
            }
        });
        dialog.show();
    }

    private void resetLocalCame() {
        RtcEngine.destroy();
        RoomController.getInstance().release();
        AgoraClient.create().release();
        AgoraClient.create().setUpAgora(getApplicationContext(), "e0972168ff254d7aa05501cd85204692");
    }

    private void reset() {
        int height = 1024;
        int width = 760;
        mVideoManager = VideoManager.createInstance(mContext);
//        mVideoManager.deallocate();
//        mVideoManager.allocate(width, height, -1, com.android.beauty.constant.Constant.CAMERA_FACING_FRONT);
//        mVideoManager.startCapture();
        mVideoManager.allocate(width, height, -1, com.android.beauty.constant.Constant.CAMERA_FACING_FRONT);
        mVideoManager.deallocate();
        mVideoManager.detachToRTCEngine();
        mVideoManager.attachToRTCEngine(AgoraClient.create().rtcEngine());
        mVideoManager.stopCapture();
    }

    private void showApplySuccess() {//申请通过
        AAlertDialog dialog = new AAlertDialog(mContext);
        dialog.setMessage("恭喜您已成为主播", com.deepsea.mua.mine.R.color.black, 18);
        dialog.setButton("去开播", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                dialog.dismiss();
                EventBus.getDefault().post(new GotoShowEvent());
            }
        });
        dialog.show();
    }

    private void showApplyFail(String wx, String time) {//申请拒绝
        UserUtils.getUser().setIs_apply_match(0);
        AAlertDialog dialog = new AAlertDialog(mContext);
        dialog.setMessage(String.format("抱歉，您未通过主持申请审核\n详情请咨询客服%s\n您可以在%s再次申请", wx, time));
        dialog.setButton("确定", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void jumpRoomOperate() {
        UserUtils.getUser().setIs_apply_match(0);
        mViewModel.jumpRoom().observe(this, new ProgressObserver<JumpRoomVo>(mContext) {
                    @Override
                    public void onSuccess(JumpRoomVo result) {
                        if (result != null && !TextUtils.isEmpty(result.getId())) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    boolean hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
                                    if (!hasFaceBeauty || com.deepsea.mua.stub.utils.AppConstant.getInstance().isRtcEngineDestroy()) {
                                        AgoraClient.create().release();
                                        AgoraClient.create().setUpAgora(getApplicationContext(), "e0972168ff254d7aa05501cd85204692");
                                    }
                                }
                            }).start();
                            mRoomJump.startJump(result.getId(), Integer.valueOf(result.getRoom_type()), mContext, null);
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        hideProgress();
                    }
                }
        );
    }




    @Override
    protected void onNewIntent(Intent intent) {
        int index = intent.getIntExtra("index", 0);
        int[] itemIds = new int[]{R.id.navigation_msg, R.id.navigation_voice, R.id.navigation_mine};
        if (mBinding.navigation != null) {
            mBinding.navigation.setSelectedItemId(itemIds[index]);
        }
    }

    private void initNavigation() {
        mBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBinding.navigation.setItemIconTintList(null);

        mVoiceFragment = new VoiceFragment();
        mMessageFragment = new MessageMainFragment();
        mMineFragment = new MineFragment();
        fragments = new Fragment[]{mMessageFragment, mVoiceFragment, mMineFragment};
        mBinding.navigation.setSelectedItemId(R.id.navigation_voice);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FragmentTabEvent event) {
        if (event.tabTag.equals("msgPage")) {
            //消息
            setFragmentPosition(0);
        } else if (event.tabTag.equals("roomPage")) {
            //大厅
            setFragmentPosition(1);
        } else if (event.tabTag.equals("profilePage")) {
            //发现
            setFragmentPosition(4);
        } else if (event.tabTag.equals("feedPage")) {
            //发现
            setFragmentPosition(2);
        }
    }

    VideoManager mVideoManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_voice:
                setFragmentPosition(1);
                return true;
            case R.id.navigation_msg:
                setFragmentPosition(0);
                return true;
            case R.id.navigation_mine:
                hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
                if (hasFaceBeauty) {
                    mVideoManager = VideoManager.createInstance(mContext);
                    if (mVideoManager != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mVideoManager.deallocate();
                                mVideoManager.stopCapture();
                            }
                        }).start();
                    }
                }
                setFragmentPosition(2);
                return true;
        }
        return false;
    };


    private void setFragmentPosition(int pos) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment from = manager.findFragmentByTag(TABS[mPrePos]);
        Fragment to = manager.findFragmentByTag(TABS[pos]);
        if (from != null) {
            transaction.hide(from);
        }
        if (to == null) {
            to = fragments[pos];
            transaction.add(R.id.container, to, TABS[pos]).commitAllowingStateLoss();
        } else {
            transaction.show(to).commitAllowingStateLoss();
        }
        mPrePos = pos;
    }

    private void getMakeFace() {
        mViewModel.getMakeFace().observe(this, new BaseObserver<FaceRequestBean>() {
            @Override
            public void onError(String msg, int code) {
                SharedPrefrencesUtil.saveData(mContext, "face", "face", "");
            }

            @Override
            public void onSuccess(FaceRequestBean result) {
                if (result != null) {
                    if (!TextUtils.isEmpty(result.getFace())) {
                        try {
                            FaceBean bean = JsonConverter.fromJson(result.getFace(), FaceBean.class);
                            AppConstant.getInstance().setFaceBean(bean);
                            SharedPrefrencesUtil.saveData(mContext, "face", "face", result.getFace());
                        } catch (Exception e) {
                            String faceJson = "{\"beautyToothLevel\":0,\"blurLevel_0\":0,\"blurLevel_1\":0.699999988079071,\"blurLevel_2\":0,\"blurType\":1,\"eggLevel\":0,\"enlargingLevel\":0,\"eyelightingLevel\":0,\"foreheadLevel\":0.5,\"jewLevel\":0.30000001192092896,\"mouthLevel\":0.5,\"narrowLevel\":0,\"noseLevel\":0,\"redLevel\":0,\"selectedFilter\":\"fennen1\",\"selectedFilterLevel\":0,\"skinDetectEnable\":true,\"smallLevel\":0,\"thinningLevel\":0,\"vLevel\":0,\"whiteLevel\":0.800000011920929}";
                            FaceBean bean = JsonConverter.fromJson(faceJson, FaceBean.class);
                            AppConstant.getInstance().setFaceBean(bean);
                            SharedPrefrencesUtil.saveData(mContext, "face", "face", result.getFace());
                        }

                    } else {
                        SharedPrefrencesUtil.saveData(mContext, "face", "face", "");
                    }
                }
            }
        });
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                toastShort("再按一次返回桌面");
//                mExitTime = System.currentTimeMillis();
//            } else {
//                App.getApp().exitApp();
////                System.exit(0);
//                finish();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        setUnreadMsgCount();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActiveController.removeActivityLifecycle();
        unregisterLoginAnotherReceiver();
        EMClient.getInstance().chatManager().removeMessageListener(mEMListener);
        OnlineController.getInstance(mContext).stopObservable();
        OnlineController.getInstance(mContext).stopMarqueeObservable();
        inviteListBeans = null;
        unregisterEventBus(this);
        RtcEngine.destroy();
        if (mVideoManager != null) {

//            mVideoManager.detachToRTCEngine();
            mVideoManager.deallocate();
            mVideoManager.stopCapture();

            mVideoManager = null;
        }

    }

    private EMMessageListener mEMListener = new IEMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            setUnreadMsgCount();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            setUnreadMsgCount();
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
            setUnreadMsgCount();
        }
    };

    public void setUnreadMsgCount() {
        runOnUiThread(() -> {
            int chatCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
            int count = chatCount + sysUnreadNum + applyUnreadNum + myApplyUnreadNum;
            ViewBindUtils.setVisible(mBinding.tvMsgUnread, count > 0);
            ViewBindUtils.setText(mBinding.tvMsgUnread, String.valueOf(count));
        });
    }

    private void registerLoginAnotherReceiver() {
        if (mLoginAnotherReceiver == null) {
            mLoginAnotherReceiver = new LoginAnotherReceiver(this);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(HxHelper.ACTION_LOGIN_ANOTHER);
        filter.addAction(ResultValid.ACTION_TOKEN_EXPIRED);
        registerReceiver(mLoginAnotherReceiver, filter);
    }

    private void unregisterLoginAnotherReceiver() {
        if (mLoginAnotherReceiver != null) {
            unregisterReceiver(mLoginAnotherReceiver);
        }
    }

    @Override
    public void onAppJoinBackground() {
        Log.e(TAG, "onAppJoinBackground: ");
    }

    @Override
    public void onAppJoinForeground(Activity activity) {
        Log.e(TAG, "onAppJoinForeground: ");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ClickEvent event) {
        switch (event.getClick()) {
            case ClickEventType.Click9:
                setUnreadMsgCount();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageTipsEvent event) {
        getMessageNum();
    }


    private List<HeartBeatBean.InviteListBean> inviteListBeans = null;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InviteDialogEvent event) {
        if (inviteListBeans == null && !AppManager.getAppManager().currentActivity().getClass().equals(InviteDialogActivity.class)) {
            inviteListBeans = event.inviteListBeans;
//            showInvitedOperate();
            ArouterUtils.build(ArouterConst.PAGE_INVITE_DIALOG)
                    .withSerializable("inviteListBeans", (Serializable) inviteListBeans)
                    .withTransition(R.anim.push_bottom_in, R.anim.push_bottom_out)
                    .navigation();

        } else {
            inviteListBeans.addAll(event.inviteListBeans);
            EventBus.getDefault().post(new InviteOtherEvent(event.inviteListBeans));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resetInviteList(InviteDialogCloseEvent inviteDialogCloseEvent) {
        inviteListBeans = null;
    }


    private class LoginAnotherReceiver extends BroadcastReceiver {

        private WeakReference<MainActivity> activityWr;

        public LoginAnotherReceiver(MainActivity activity) {
            activityWr = new WeakReference<>(activity);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Logg.d("MainActivity", "请求重新登录 action = " + intent.getAction());
            if (HxHelper.ACTION_LOGIN_ANOTHER.equals(intent.getAction())
                    || ResultValid.ACTION_TOKEN_EXPIRED.equals(intent.getAction())) {
                MainActivity activity = activityWr.get();
                if (activity != null) {
                    AppClient.getInstance().logout(p -> {
                        p.apply();
                        LogoutUtils.logout(MainActivity.this);
                    });
                }
            }
        }
    }

    private void getFriendList() {
        mViewModel.getFriendList().observe(this, new BaseObserver<FriendInfoListBean>() {
            @Override
            public void onSuccess(FriendInfoListBean result) {

                if (result != null) {
                    if (result.getList() != null && result.getList().size() > 0) {
                        FriendsUtils.getInstance().saveFriendUtils(result.getList());
                    }
                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
            }
        });
    }

    private int sysUnreadNum = 0;//系统消息未读
    private int applyUnreadNum = 0;//好友申请未读
    private int myApplyUnreadNum = 0;//我的申请未读

    //未读消息数量
    private void getMessageNum() {
        mViewModel.getMessageNum().observe(this, new BaseObserver<MessageNumVo>() {
            @Override
            public void onSuccess(MessageNumVo result) {
                if (result != null) {
                    applyUnreadNum = TextUtils.isEmpty(result.getApply_my()) ? 0 : Integer.valueOf(result.getApply_my());
                    myApplyUnreadNum = TextUtils.isEmpty(result.getMy_apply()) ? 0 : Integer.valueOf(result.getMy_apply());
                    sysUnreadNum = TextUtils.isEmpty(result.getSystem_num()) ? 0 : Integer.valueOf(result.getSystem_num());
                    setUnreadMsgCount();

                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void JumpToRoom(OpenRoom openRoom) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
                if (!hasFaceBeauty || com.deepsea.mua.stub.utils.AppConstant.getInstance().isRtcEngineDestroy()) {
//                            RoomController.getInstance().release();
                    AgoraClient.create().release();
                    AgoraClient.create().setUpAgora(mContext.getApplicationContext(), "e0972168ff254d7aa05501cd85204692");
                }
            }
        }).start();
        mRoomJump.startJump(openRoom.roomId, openRoom.roomMode, mContext, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeFindTab(ShowRankStepOne one) {
        if (one.getType() == 0) {
            setFragmentPosition(4);
            mBinding.btnTest1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new ShowRankStepTwo(one.getCurrentPos()));
                }
            }, 200);
        } else if (one.getType() == 1) {
            setFragmentPosition(3);
            mBinding.btnTest1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new ShowMineDialog(0));
                }
            }, 200);
        } else if (one.getType() == 2) {
            setFragmentPosition(3);
            mBinding.btnTest1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new ShowMineDialog(1));
                }
            }, 1000);
        }
    }

}
