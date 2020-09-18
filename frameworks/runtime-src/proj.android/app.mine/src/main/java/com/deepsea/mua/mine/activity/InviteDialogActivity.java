package com.deepsea.mua.mine.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.dialog.UpdateDialog;
import com.deepsea.mua.mine.viewmodel.IncomeDetailsModel;
import com.deepsea.mua.mine.viewmodel.InviteDialogViewModel;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.InviteOutRoomDialog;
import com.deepsea.mua.stub.entity.HeartBeatBean;
import com.deepsea.mua.stub.entity.IncomeListBean;
import com.deepsea.mua.stub.utils.AppManager;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.deepsea.mua.stub.utils.DisposeUtils;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.ExitRoomEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteDialogCloseEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteDialogEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteInRoomEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteOtherEvent;
import com.noober.background.BackgroundLibrary;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by JUN on 2019/7/4
 */
@Route(path = ArouterConst.PAGE_INVITE_DIALOG)
public class InviteDialogActivity extends FragmentActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Inject
    ViewModelFactory mModelFactory;
    private InviteDialogViewModel mViewModel;
    private Context mContext;
    private List<HeartBeatBean.InviteListBean> inviteListBeans = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        registerEventBus(this);
        mContext = this;
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(InviteDialogViewModel.class);
        List<HeartBeatBean.InviteListBean> listBeans = (List<HeartBeatBean.InviteListBean>) getIntent().getSerializableExtra("inviteListBeans");
        if (listBeans != null) {
            inviteListBeans.addAll(listBeans);
        }
        showInvitedOperate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InviteOtherEvent event) {
        inviteListBeans.addAll(event.inviteListBeans);
    }

    int oprateFlag = 0;

    private void showInvitedOperate() {
        if (inviteListBeans != null && (inviteListBeans.size() > oprateFlag)) {
            showInvitedDialog(inviteListBeans.get(oprateFlag), new Callback() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void invitedResult(int result, String roomId, int inviteMicroId,int free,int cost,int mode) {
                    if (result == 0) {
                        resetInviteOpetate();
                        if (AppManager.getAppManager().currentActivity().getComponentName().getClassName().equals("com.deepsea.mua.voice.activity.RoomActivity")) {
                            String inRoomId = SharedPrefrencesUtil.getData(mContext, "mRoomId", "mRoomId", "");
                            if (inRoomId.equals(roomId)) {
                                EventBus.getDefault().post(new InviteInRoomEvent(inviteMicroId));
                                return;
                            }
                            EventBus.getDefault().post(new ExitRoomEvent());
                            AppManager.getAppManager().currentActivity().finish();
                            inviteListBeans = null;
                            oprateFlag = 0;
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new RoomJoinController().startJump(roomId, inviteMicroId, true,free,cost, mode, mContext);
                            }
                        }, 1000);
                    } else if (result == 1) {
                        oprateFlag++;
                        showInvitedOperate();
                    } else if (result == 2) {
                        PageJumpUtils.jumpToRecharge(InviteDialogActivity.this, "");
                    } else if (result == -1) {
                        resetInviteOpetate();
                    }
                }
            });
        } else {
            resetInviteOpetate();
        }
    }

    private Handler mHandler = new Handler() {
    };

    private void resetInviteOpetate() {
        inviteListBeans = null;
        oprateFlag = 0;
        EventBus.getDefault().post(new InviteDialogCloseEvent());
        finish();
    }

    private void showInvitedDialog(HeartBeatBean.InviteListBean bean, Callback mCallback) {
        InviteOutRoomDialog inviteOutRoomDialog = new InviteOutRoomDialog(mContext);
        inviteOutRoomDialog.setContent(bean);
        inviteOutRoomDialog.setListener(new InviteOutRoomDialog.OnClickListener() {
            @Override
            public void onDisagreeClick(View v, Dialog dialog) {
//                fetchInviteHandle(bean, mCallback, 2, inviteOutRoomDialog);
                InviteDialogActivity.this.finish();
            }

            @Override
            public void onAgreeClick(View v, Dialog dialog) {
                fetchInviteHandle(bean, mCallback, 1, inviteOutRoomDialog);
            }

            @Override
            public void onDismiss() {
//                InviteDialogActivity.this.finish();
            }
        });
        inviteOutRoomDialog.show();
    }

    interface Callback {
        //  0 agree    1 disagree 2 needRecharge
        void invitedResult(int result, String roomId, int inviteMicroId,int free,int cost,int mode);
    }

    /**
     * 请求网络--邀请同意拒绝接口
     *
     * @param bean
     * @param mCallback
     * @param status    1:同意 2:拒绝
     */
    private void fetchInviteHandle(HeartBeatBean.InviteListBean bean, Callback mCallback, int status, InviteOutRoomDialog inviteOutRoomDialog) {
        if (status == 1) {
            mCallback.invitedResult(0, String.valueOf(bean.getRoomId()), Integer.valueOf(bean.getId()),Integer.valueOf(bean.getFree()),Integer.valueOf(bean.getMicro_cost()),Integer.valueOf(bean.getRoom_mode()));
        }
        int inviteeId = Integer.valueOf(UserUtils.getUser().getUid());
        mViewModel.inviteHandle(Integer.valueOf(bean.getHongId()), inviteeId, Integer.valueOf(bean.getRoomId()), status, Integer.valueOf(bean.getId())).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
//                int code = result.getCode();
//                if (code == 200) {
//                    //同意请求检查接口
//                    if (status == 1) {
//                        mCallback.invitedResult(0, String.valueOf(bean.getRoomId()), Integer.valueOf(bean.getId()));
//                    } else {
//                        mCallback.invitedResult(1, "", -1);
//                    }
                inviteOutRoomDialog.dismiss();
                InviteDialogActivity.this.finish();

//                } else if (code == 204) {
//                    mCallback.invitedResult(2, "", -1);
//                } else {
//                    inviteOutRoomDialog.dismiss();
//                    ToastUtils.showToast(result.getDesc());
//                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
//                mCallback.invitedResult(-1, "", 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterEventBus(this);
        resetInviteOpetate();
        super.onDestroy();
    }

    public boolean isEventBusRegisted(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    public void registerEventBus(Object subscribe) {
        if (!isEventBusRegisted(subscribe)) {
            EventBus.getDefault().register(subscribe);
        }
    }

    public void unregisterEventBus(Object subscribe) {
        if (isEventBusRegisted(subscribe)) {
            EventBus.getDefault().unregister(subscribe);
        }
    }
}
