package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.android.beauty.VideoManager;
import com.deepsea.mua.core.login.ApiUser;
import com.deepsea.mua.core.login.LoginApi;
import com.deepsea.mua.core.login.OnLoginListener;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityBindWxBinding;
import com.deepsea.mua.mine.databinding.ActivitySettingBinding;
import com.deepsea.mua.mine.viewmodel.SettingViewModel;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.entity.BindWx;
import com.deepsea.mua.stub.entity.CheckBindWx;
import com.deepsea.mua.stub.entity.UserBean;
import com.deepsea.mua.stub.utils.ApkUtils;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.deepsea.mua.stub.utils.CacheUtils;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.DataClearUtil;
import com.deepsea.mua.stub.utils.LogoutUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.agora.rtc.RtcEngine;

/**
 * Created by JUN on 2019/5/7
 */
public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private SettingViewModel mViewModel;
    private int isBindWx = 2;
    private String wx_id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

        mViewModel = ViewModelProviders.of(this, mModelFactory).get(SettingViewModel.class);
        initVersion();
        boolean hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
        mBinding.openBeautyIv.setSelected(hasFaceBeauty);
        initRegisterProtocol();
        isBindWx();

    }

    private void initVersion() {
        String version = "v " + ApkUtils.getApkVersionName(mContext);
        ViewBindUtils.setText(mBinding.versionTv, version);
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.notifyLayout, o -> {
            ArouterUtils.build(ArouterConst.PAGE_MSG_SETTING).navigation();
        });
        subscribeClick(mBinding.feedLayout, o -> {
            startActivity(new Intent(mContext, FeedbackActivity.class));
        });
        subscribeClick(mBinding.logoutTv, o -> {
            AAlertDialog dialog = new AAlertDialog(mContext);
            dialog.setMessage("确认退出登录", R.color.black, 18);
            dialog.setRightButton("确定", R.color.light_orange, (v, dialog1) -> {
                logout();
                dialog1.dismiss();
            });
            dialog.setLeftButton("取消", R.color.light_orange, null);
            dialog.show();
        });

        subscribeClick(mBinding.safetyCenterLayout, o -> {
            startActivity(new Intent(mContext, SafetyCenterActivity.class));
        });
        subscribeClick(mBinding.openBeautyIv, o -> {
            boolean hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
            mBinding.openBeautyIv.setSelected(!hasFaceBeauty);
            SharedPrefrencesUtil.saveData(mContext, "hasFaceBeauty", "hasFaceBeauty", !hasFaceBeauty);
        });
        subscribeClick(mBinding.clearChacheLayout, o -> {
            AAlertDialog dialog = new AAlertDialog(mContext);
            dialog.setTitle("清除缓存");
//            dialog.setMessage("确定清除缓存？");
            dialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
                @Override
                public void onClick(View v, Dialog dialog1) {
                    dialog1.dismiss();

                }
            });
            dialog.setRightButton("确定", new AAlertDialog.OnClickListener() {
                @Override
                public void onClick(View v, Dialog dialog1) {
                    dialog1.dismiss();
                    DataClearUtil.cleanApplicationData(mContext, CacheUtils.getAppBaseDir());
                    toastShort("清除缓存成功");
                }
            });
            dialog.show();
        });
        subscribeClick(mBinding.bindwxLayout, o -> {
            startActivity(BindWechatActivity.newIntent(mContext,1));
        });
    }

    private void logout() {
        AppClient.getInstance().logout(p ->
                mViewModel.logout().observe(this, new ProgressObserver<BaseApiResult>(mContext) {
                    @Override
                    public void onSuccess(BaseApiResult result) {
                        /**
                         * @link {SharePDataBaseUtils # saveAgreement , SharePDataBaseUtils # delDynamicEdit }
                         */
                        SharedPrefrencesUtil.saveData(mContext, "user", "Agreement", false);
                        SharedPrefrencesUtil.deleteData(mContext, "user", "DynamicEditSaveBean");
                        p.apply();
                        LogoutUtils.logout(mContext);
                    }

                    @Override
                    public void onError(String msg, int code) {
                        p.apply();
                        LogoutUtils.logout(mContext);
                    }
                }));
    }

    private void isBindWx() {
        mViewModel.isBindWx().observe(this, new ProgressObserver<CheckBindWx>(mContext) {
                    @Override
                    public void onSuccess(CheckBindWx result) {
                        if (result != null) {
                            updateWxBindUi(result.getIs_band(), result.getWx_id());
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        super.onError(msg, code);
                    }
                }
        );

    }

    @Override
    protected void onDestroy() {
        boolean hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
        if (hasFaceBeauty) {
            reset();
        } else {
            resetLocalCame();
        }
        super.onDestroy();
    }

    VideoManager mVideoManager;

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

    private void initRegisterProtocol() {
        String protocol = "《合合交友用户协议》和《用户隐私政策》";
        SpannableString spannableString = new SpannableString(protocol);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.parseColor("#FF8C74"));
            }

            @Override
            public void onClick(@NonNull View widget) {
                PageJumpUtils.jumpToWeb(AddressCenter.getAddress().getRegisterProtocol());
            }
        }, 0, 10, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(Color.parseColor("#FF8C74"));
            }

            @Override
            public void onClick(@NonNull View widget) {
                PageJumpUtils.jumpToWeb(AddressCenter.getAddress().getPrivacyPolicy());
            }
        }, 11, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.protocolTv.setHighlightColor(Color.TRANSPARENT);
        mBinding.protocolTv.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.protocolTv.setText(spannableString);
    }

    private void updateWxBindUi(int isBind, String wx_id) {
        isBindWx = isBind;
        this.wx_id = wx_id;
        ViewBindUtils.setText(mBinding.tvBindWx, isBind == 1 ? "已绑定" : "未绑定");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBindWx();
    }
}
