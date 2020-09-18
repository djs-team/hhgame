package com.deepsea.mua.mine.activity;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.faceliveness.helper.AuditHelper;
import com.deepsea.mua.faceliveness.listener.RPAuditListener;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.ApplyHostAdapter;
import com.deepsea.mua.mine.databinding.ActivityApplyHostBinding;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.dialog.ApplyInfoDialog;
import com.deepsea.mua.stub.dialog.AuthenticationAlertDialog;
import com.deepsea.mua.stub.entity.ApplyHost;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.JumpRoomVo;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by JUN on 2019/5/6
 * 申请主持
 */
@Route(path = ArouterConst.PAGE_ME_MINE_APPLYHOST)

public class ApplyHostActivity extends BaseActivity<ActivityApplyHostBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private ProfileViewModel mViewModel;

    @Inject
    RoomJoinController mRoomJump;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_host;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileViewModel.class);
        requestData();
    }

    private boolean isRefresh = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefresh) {
            isRefresh = false;
            requestData();
        }

    }

    private int state = 1;//任务状态
    private String attestation = "0";

    @Override
    protected void initListener() {
        subscribeClick(mBinding.tvOk, o -> {
            if (state == 2) {
                isRefresh = true;
                if (attestation.equals("0")) {
                    //去实名认证
                    showAuthDialog();
                } else {
                    //申请主持填写微信和年龄
                    showApplyDialog();
                }

            }

        });

    }


    private void requestData() {
        mViewModel.init_apply().observe(this, new BaseObserver<ApplyHost>() {
            @Override
            public void onSuccess(ApplyHost result) {
                initData(result);
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }

    private void initData(ApplyHost data) {
//        1:未完成任一任务   2:已完成一个或多个，可以申请主持  3:后台审核拒绝
        state = data.getState();
        attestation = data.getAttestation();
        if (state == 1) {
            mBinding.tvOk.setText("未完成申请条件");
        } else if (data.getState() == 2) {
            mBinding.tvOk.setText("申请成为主持");
        } else if (data.getState() == 3) {
            mBinding.tvOk.setText("后台审核拒绝");
        } else if (data.getState() == 4) {
            mBinding.tvOk.setText("等待官方审核");
        }
        ViewBindUtils.setEnable(mBinding.tvOk, state != 1);
        ViewBindUtils.setText(mBinding.tvWxCode, data.getInfo_up());
        ViewBindUtils.setText(mBinding.tvServiceTime, data.getInfo_down());
        mBinding.rvApply.setLayoutManager(new LinearLayoutManager(mContext));
        ApplyHostAdapter adapter = new ApplyHostAdapter(mContext);
        adapter.setmListener(new ApplyHostAdapter.OnMyClickListener() {
            @Override
            public void confirm(int type) {
                if (type == 1) {
                    //跳转直播间
                    jumpRoomOperate();
                } else if (type == 2) {
                    //跳转到我的邀请码
                    startActivity(new Intent(mContext, InvitationCodeActivity.class));
                }

            }
        });
        mBinding.rvApply.setAdapter(adapter);
        adapter.setNewData(data.getResult());

    }

    /**
     * 随机跳转直播间
     */
    private void jumpRoomOperate() {
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
                            mRoomJump.startJump(result.getId(), Integer.valueOf(result.getRoom_type()), mContext, new RoomJoinController.CallBack() {
                                @Override
                                public void onResult() {
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        hideProgress();

                    }
                }
        );
    }

    private void showAuthDialog() {
        AAlertDialog dialog = new AAlertDialog(mContext);
        dialog.setMessage("申请成为主持需要实名认证", com.deepsea.mua.stub.R.color.black, 15);
        dialog.setButton("去认证", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                auth();
            }
        });
        dialog.show();
    }

    private void auth() {
        RxPermissions permissions = new RxPermissions(this);
        Disposable subscribe = permissions.request(Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        getVerifyToken();
                    }
                });
    }

    /**
     * 实人认证
     */
    private void getVerifyToken() {
        mViewModel.getVerifyToken().observe(this, new BaseObserver<AuditBean>() {
            @Override
            public void onSuccess(AuditBean result) {
                if (result == null)
                    return;
                AuditHelper.start(result.getToken(), mContext, new RPAuditListener() {
                    @Override
                    public void onAuditPass() {
                        createApprove();
                    }

                    @Override
                    public void onAuditNot(String code) {
                        super.onAuditNot(code);
                        switch (code) {
                            case "-1":
                                ToastUtils.showToast("退出认证");
                                break;
                            case "3001":
                                ToastUtils.showToast("认证token无效或已过期");
                                break;
                            case "3101":
                                ToastUtils.showToast("用户姓名身份证实名校验不匹配");
                                break;
                            case "3102":
                                ToastUtils.showToast("实名校验身份证号不存在");
                                break;
                            case "3103":
                                ToastUtils.showToast("实名校验身份证号不合法");
                                break;
                            case "3104":
                                ToastUtils.showToast("认证已通过，重复提交");
                                break;
                            case "3204":
                            case "3206":
                                ToastUtils.showToast("非本人操作");
                                break;
                            case "3208":
                                ToastUtils.showToast("公安网无底照");
                                break;
                        }
                    }
                });
            }
        });
    }

    private void createApprove() {
        mViewModel.createapprove().observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                toastShort(result.getDesc());
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                if (code == 2006) {
                    AuthenticationAlertDialog dialog = new AuthenticationAlertDialog(mContext);
                    dialog.setContent(msg);
                    dialog.show();
                } else {
                    toastShort(msg);
                }
            }
        });

    }

    /**
     * 填写资料
     */
    private void showApplyDialog() {
        ApplyInfoDialog dialog = new ApplyInfoDialog(mContext);
        dialog.setOnClickListener(new ApplyInfoDialog.OnClickListener() {
            @Override
            public void onClick(String wx, String age) {
                if (TextUtils.isEmpty(wx)) {
                    toastShort("请输入您的微信号");
                    return;
                }
                if (TextUtils.isEmpty(age)) {
                    toastShort("请输入您的年龄");
                    return;
                }
                applyInfo(wx, age);

            }
        });
        dialog.show();


    }

    /**
     *
     */
    private void applyInfo(String wx, String age) {
        mViewModel.applyInfo(wx, age).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                toastShort(result.getDesc());

            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                toastShort(msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        mRoomJump.destroy();
        super.onDestroy();
    }


}
