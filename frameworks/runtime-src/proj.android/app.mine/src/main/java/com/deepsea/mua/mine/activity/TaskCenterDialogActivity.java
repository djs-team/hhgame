package com.deepsea.mua.mine.activity;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.faceliveness.helper.AuditHelper;
import com.deepsea.mua.faceliveness.listener.RPAuditListener;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.TaskAdapter;
import com.deepsea.mua.mine.databinding.DialogTaskCenterBinding;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.databinding.DialogCityScreeningBinding;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.dialog.AuthenticationAlertDialog;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.JumpRoomVo;
import com.deepsea.mua.stub.entity.TaskBean;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.Const;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.DisposeUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.ShowRankStepOne;
import com.jakewharton.rxbinding2.view.RxView;
import com.noober.background.BackgroundLibrary;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uber.autodispose.AutoDisposeConverter;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by JUN on 2019/4/16
 */
@Route(path = ArouterConst.PAGE_ME_DIALOG_TASK_CENTER)
public class TaskCenterDialogActivity extends FragmentActivity
        implements HasSupportFragmentInjector {

    @Inject
    ViewModelFactory mModelFactory;
    private ProfileViewModel mViewModel;
    Context mContext;
    protected DialogTaskCenterBinding mBinding;
    private TaskAdapter newuserTaskAdapter;
    private TaskAdapter dailyTaskAdapter;
    @Inject
    RoomJoinController mRoomJump;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        mContext = this;
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileViewModel.class);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mBinding.rvNewuserTask.setLayoutManager(new LinearLayoutManager(mContext));
        newuserTaskAdapter = new TaskAdapter(mContext, AppConstant.getInstance().getNewuserTaskIcons());
        mBinding.rvNewuserTask.setAdapter(newuserTaskAdapter);
        dailyTaskAdapter = new TaskAdapter(mContext, AppConstant.getInstance().getDailyTaskIcons());
        mBinding.rvDaily.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rvDaily.setAdapter(dailyTaskAdapter);
        newuserTaskAdapter.setmListener(new TaskAdapter.OnMyClickListener() {
            @Override
            public void confirm(int taskPos, String taskId, String state) {
//                点击跳转的类型:
//                6:上传头像
//                7:实名认证
//                8:基本资料
//                9:绑定微信
//                10/16:充值
//                11/12/15/18:随即进入前6房间
//                13:
//                14:
//                17/19:我的邀请码分享页
                onClickAction("6", taskPos, taskId, state);
            }
        });
        dailyTaskAdapter.setmListener(new TaskAdapter.OnMyClickListener() {
            @Override
            public void confirm(int taskPos, String taskId, String state) {
                onClickAction("7", taskPos, taskId, state);
            }
        });
        fetchData();
        initListener();

    }

    protected int getLayoutId() {
        return R.layout.dialog_task_center;
    }


    protected void initListener() {
        subscribeClick(mBinding.llLookMore, o -> {
            newuserTaskAdapter.setShowMore(!newuserTaskAdapter.isShowMore());
            newuserTaskAdapter.notifyDataSetChanged();
        });
        subscribeClick(mBinding.rlGroup, o -> {
            finish();
        });
    }

    private void fetchData() {
        mViewModel.getTaskList().observe(this, new ProgressObserver<TaskBean>(mContext) {
            @Override
            public void onSuccess(TaskBean result) {
                if (result != null) {
                    setData(result);
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }

    private void setData(TaskBean bean) {
        newuserTaskAdapter.setNewData(bean.getNew_data());
        dailyTaskAdapter.setNewData(bean.getTask_data());

    }

    // taskType  0 新手任务  1 日常任务
    private void onClickAction(String taskType, int taskPos, String taskId, String state) {
        if (state.equals("2") || state.equals("3")) {
            return;
        }
        if (state.equals("4")) {
            taskReceive(taskType, taskId, taskPos);
            return;
        }
        AAlertDialog aAlertDialog = new AAlertDialog(mContext);
        aAlertDialog.setMessage(taskType.equals("6") ? "您是否要跳出直播间，进行新手任务?" : "您是否要跳出直播间，进行每日任务?");
        aAlertDialog.setLeftButton("否", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                dialog.dismiss();
            }
        });
        aAlertDialog.setRightButton("是", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {

                gotoFinishTask(taskId);
            }
        });
        aAlertDialog.show();

    }

    private void gotoFinishTask(String taskId) {
        this.taskId = taskId;
        switch (taskId) {
            case "6":
                ShowRankStepOne showRankStepOne = new ShowRankStepOne();
                showRankStepOne.setType(1);
                EventBus.getDefault().post(showRankStepOne);
                break;
            case "7":
                showRankStepOne = new ShowRankStepOne();
                showRankStepOne.setType(2);
                EventBus.getDefault().post(showRankStepOne);
                break;
            case "81":
                startActivity(new Intent(mContext, ProfileEditActivity.class));
                break;
            case "82":
                startActivity(new Intent(mContext, MarriageSeekingActivity.class));
                break;
            case "9":
                startActivity(BindWechatActivity.newIntent(mContext,1));
                break;
            case "10":
            case "16":
                PageJumpUtils.jumpToRecharge("");
                break;
            case "11":
            case "12":
            case "13":
            case "15":
            case "18":
                jumpRoomOperate();
                break;
            case "14":
                showRankStepOne = new ShowRankStepOne();
                showRankStepOne.setType(0);
                showRankStepOne.setCurrentPos(2);
                EventBus.getDefault().post(showRankStepOne);
                break;
            case "17":
            case "19":
                startActivity(new Intent(mContext, InvitationCodeActivity.class));
                break;

        }
        isGotoTask = true;
        finish();
    }

    private void taskReceive(String type, String taskId, int taskPos) {
        if (taskId.equals("81") || taskId.equals("82")) {
            taskId = "8";
        }
        mViewModel.taskReceive(type, taskId).observe(this, new ProgressObserver<BaseApiResult>(mContext) {
            @Override
            public void onSuccess(BaseApiResult result) {
                if (result != null) {
                    toastShort(result.getDesc());
                    if (type.equals("6")) {
                        newuserTaskAdapter.getItem(taskPos).setState("2");
                        newuserTaskAdapter.notifyDataSetChanged();
                    } else {
                        dailyTaskAdapter.getItem(taskPos).setState("2");
                        dailyTaskAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
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
        dialog.setMessage("实名认证", com.deepsea.mua.stub.R.color.black, 15);
        dialog.setRightButton("去认证", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                auth();
            }
        });
        dialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                dialog.dismiss();
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

    boolean isGotoTask = false;
    private String taskId;

    @Override
    public void onDestroy() {
        mRoomJump.destroy();
        super.onDestroy();
    }

    @Override
    public void finish() {
        Intent intent = getIntent();
        intent.putExtra("isGotoTask", isGotoTask);
        intent.putExtra("taskId", taskId);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    protected <T> AutoDisposeConverter<T> autoDisposable() {
        return DisposeUtils.autoDisposable(this);
    }

    protected void toastShort(String msg) {
        ToastUtils.showToast(msg);
    }

    protected void subscribeClick(View view, Consumer<Object> consumer) {
        Disposable subscribe = RxView.clicks(view)
                .throttleFirst(Const.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(consumer);
    }

}
