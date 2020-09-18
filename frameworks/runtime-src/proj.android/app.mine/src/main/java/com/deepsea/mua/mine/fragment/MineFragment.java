package com.deepsea.mua.mine.fragment;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.faceliveness.helper.AuditHelper;
import com.deepsea.mua.faceliveness.listener.RPAuditListener;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.activity.AssistActivity;
import com.deepsea.mua.mine.activity.BindWechatActivity;
import com.deepsea.mua.mine.activity.BlockListActivity;
import com.deepsea.mua.mine.activity.CodeOfConductActivity;
import com.deepsea.mua.mine.activity.InvitationMineActivity;
import com.deepsea.mua.mine.activity.MarriageSeekingActivity;
import com.deepsea.mua.mine.activity.MyGuardActivity;
import com.deepsea.mua.mine.activity.MyTagsActivity;
import com.deepsea.mua.mine.activity.PersonalLevelActivity;
import com.deepsea.mua.mine.activity.ProfileEditActivity;
import com.deepsea.mua.mine.activity.SettingActivity;
import com.deepsea.mua.mine.activity.VisitorsActivity;
import com.deepsea.mua.mine.activity.WalletActivity;
import com.deepsea.mua.mine.databinding.FragmentMineBinding;
import com.deepsea.mua.mine.dialog.InputBindSuccessDialog;
import com.deepsea.mua.mine.viewmodel.ProfileEditViewModel;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.callback.CommonCallback;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.dialog.AuthenticationAlertDialog;
import com.deepsea.mua.stub.dialog.PhotoDialog;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.InitInviteBean;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.ProfileBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.OssUpUtil;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.ShowMineDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by JUN on 2019/3/22
 */
public class MineFragment extends BaseFragment<FragmentMineBinding> {

    @Inject
    AppExecutors mExecutors;
    @Inject
    ViewModelFactory mModelFactory;
    private ProfileViewModel mViewModel;
    private ProfileEditViewModel mEditViewModel;
    private User mUser;
    private ProfileBean mProfile;

    private PhotoDialog mPhotoDialog;

    private boolean isResumed, isHidden;
    int bind_type;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
    }

    @Override
    protected void initView(View view) {

        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileViewModel.class);
        mEditViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileEditViewModel.class);
        mUser = UserUtils.getUser();
//        ViewBindUtils.setVisible(mBinding.assistLayout, "HuaWei".equals(ApkUtils.getChannelName(mContext)));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (isResumed && !isHidden) {
            getUserInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;
        if (!isHidden) {
            getUserInfo();
        }
    }

    private void getUserInfo() {
        if (mUser != null) {
            mViewModel.user_info(mUser.getUid()).observe(this,
                    new BaseObserver<ProfileBean>() {
                        @Override
                        public void onSuccess(ProfileBean result) {
                            if (result != null) {
                                loadNetData(result);
                            }
                        }
                    });
        }
    }

    @Override
    protected void initListener() {
//        subscribeClick(mBinding.taskLayout, o -> {
//            PageJumpUtils.jumpToTaskCenter(mContext);
//        });
        subscribeClick(mBinding.avatarIv, o -> {
            showPhotoDialog();
        });
        subscribeClick(mBinding.editIv, o -> {
            PageJumpUtils.jumpToProfile(mUser.getUid());
        });

        //我的邀请
        subscribeClick(mBinding.invitionLayout, o -> {
            Intent intent = new Intent(mContext, InvitationMineActivity.class);
            startActivity(intent);
        });
        //认证
        subscribeClick(mBinding.authLayout, o -> {
            auth();
        });
        //钱包
        subscribeClick(mBinding.walletLayout, o -> {
            startActivity(new Intent(mContext, WalletActivity.class));
        });
        //设置
//        subscribeClick(mBinding.settingTv, o -> {
//            startActivity(new Intent(mContext, SettingActivity.class));
//        });
        //联系我们
        subscribeClick(mBinding.assistLayout, o -> {
            startActivity(new Intent(mContext, AssistActivity.class));
        });


        //粉丝贡献
        subscribeClick(mBinding.fansRankLayout, o -> {
            ArouterUtils.build(ArouterConst.PAGE_FANS_RANK).navigation();
        });
        //家长模式
        subscribeClick(mBinding.parentLayout, o -> {
            boolean isOpenParentMode = mProfile != null && mProfile.getMonitoring_info() != null
                    && mProfile.getMonitoring_info().getParents_status() == 1;

            ArouterUtils.build(ArouterConst.PAGE_PARENT)
                    .withBoolean("open", isOpenParentMode)
                    .navigation();
        });
        //青少年模式
        subscribeClick(mBinding.youngerLayout, o -> {
            boolean isOpenYoungerMode = mProfile != null && mProfile.getMonitoring_info() != null
                    && mProfile.getMonitoring_info().getMonitoring_status() == 1;

            ArouterUtils.build(ArouterConst.PAGE_YOUNGER)
                    .withBoolean("open", isOpenYoungerMode)
                    .navigation();
        });
        //基本资料
        subscribeClick(mBinding.basicLayout, o -> {
            startActivity(new Intent(mContext, ProfileEditActivity.class));
        });
        //征友条件
        subscribeClick(mBinding.conditionLayout, o -> {
            startActivity(new Intent(mContext, MarriageSeekingActivity.class));
        });
        //输入邀请码
        subscribeClick(mBinding.inputInviteCodeLayout, o -> {
            if (TextUtils.isEmpty(belongId.trim())) {
                initInvite();
            } else {
                PageJumpUtils.jumpToProfile(belongId.trim());
            }
        });
        //主持行为规范
        subscribeClick(mBinding.codeOfConductLayout, o -> {
            startActivity(new Intent(mContext, CodeOfConductActivity.class));
        });
        //我的标签
        subscribeClick(mBinding.tagsLayout, o -> {
            startActivity(new Intent(mContext, MyTagsActivity.class));
        });
        //用户身份
        subscribeClick(mBinding.userIdentityLayout, o -> {
            String url = AddressCenter.getAddress().getUserIdentityUrl() + UserUtils.getUser().getToken();
            PageJumpUtils.jumpToWeb("用户身份", url);
        });
        //余额
        subscribeClick(mBinding.balanceLayout, o -> {
            PageJumpUtils.jumpToRecharge("");
        });
        //守护
        subscribeClick(mBinding.guardLayout, o -> {
            startActivity(new Intent(mContext, MyGuardActivity.class));
        });
        //绑定手机号
        subscribeClick(mBinding.phoneBindLayout, o -> {
            startActivity(BindWechatActivity.newIntent(mContext, bind_type));
        });
        //黑名单
        subscribeClick(mBinding.blockLayout, o -> {
            startActivity(new Intent(mContext, BlockListActivity.class));
        });


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

    InputBindSuccessDialog mInputDialog = null;

    private void showInputInviteCodeDialog(String coin) {
        if (mInputDialog == null) {
            mInputDialog = new InputBindSuccessDialog(mContext);
        }
        mInputDialog.setEnsureCallback(new CommonCallback<String>() {
            @Override
            public void onSuccess(String data) {
                bindInviteCode(data);
            }
        });
        mInputDialog.setContent(coin);
//        mInputDialog.setData(name);
        mInputDialog.show();
    }

    private void initInvite() {
        mViewModel.initInvite().observe(this, new BaseObserver<InitInviteBean>() {
            @Override
            public void onSuccess(InitInviteBean result) {
                if (result != null) {
                    showInputInviteCodeDialog(result.getCoin());
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                toastShort(msg);
            }
        });


    }

    private void bindInviteCode(String referrer_code) {
        mViewModel.bindReferrer(referrer_code).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                toastShort(result.getDesc());
                if (result.getCode() == 200) {
                    ViewBindUtils.setVisible(mBinding.inputInviteCodeLayout, false);
                    toastShort(result.getDesc());

                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                toastShort(msg);
            }
        });

    }

    private String belongId = "";

    private void loadNetData(ProfileBean result) {
        this.mProfile = result;
        ProfileBean.UserInfoBean userInfo = result.getUser_info();
        //是否开启主播提醒
        User user = UserUtils.getUser();
        // user.setFansmenustatus(userInfo.getFansmenustatus());
        user.setAvatar(userInfo.getAvatar());
        user.setIs_matchmaker(userInfo.getIs_matchmaker());
        UserUtils.saveUser(user);
        //avatar
        GlideUtils.circleImage(mBinding.avatarIv, userInfo.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        //nick
        mBinding.nickTv.setText(userInfo.getNickname());
        //uid
        mBinding.uidTv.setText("ID: " + userInfo.getPretty_id());
        //sex
        SexResUtils.setSexImgInFindPage(mBinding.rlSex, mBinding.sexIv, result.getUser_info().getSex());
        ViewBindUtils.setText(mBinding.ageTv, String.valueOf(result.getUser_info().getAge()));
        //city
        if (!TextUtils.isEmpty(result.getUser_info().getCity())) {
            mBinding.cityTv.setText(result.getUser_info().getCity());
        } else {
            mBinding.cityTv.setText("");
        }
        ViewBindUtils.setVisible(mBinding.ivLocation, !TextUtils.isEmpty(result.getUser_info().getCity().trim()));


        //认证
        mBinding.authTv.setSelected(TextUtils.equals(userInfo.getAttestation(), "1"));
        mBinding.authTv.setText(TextUtils.equals(userInfo.getAttestation(), "1") ? "已认证" : "未认证");
//        ViewBindUtils.setVisible(mBinding.taskProcessLayout, userInfo.getIs_matchmaker().equals("1"));
//        ViewBindUtils.setVisible(mBinding.codeOfConductLayout, userInfo.getIs_matchmaker().equals("1"));
//        ProfileBean.GradeInfoBean gradeInfo = result.getGrade_info();
//
//        ProfileBean.NumberInfoBean numberInfo = result.getNumber_info();

//        //输入邀请码
//        ViewBindUtils.setVisible(mBinding.inputInviteCodeLayout, userInfo.getIs_bind() == 1);
//        if (userInfo.getIs_bind() == 1) {
//            //如果显示绑定邀请码一栏
//            if (!TextUtils.isEmpty(userInfo.getBelongId()) && !userInfo.getBelongId().equals("0")) {
//                //已经绑定邀请人
//                belongId = userInfo.getBelongId();
//                ViewBindUtils.setText(mBinding.tvBindinviteTitle, "邀请码绑定（完成）");
//                ViewBindUtils.setText(mBinding.tvBelongName, userInfo.getBelongName());
//            } else {
//                ViewBindUtils.setText(mBinding.tvBindinviteTitle, "邀请码绑定（获得" + userInfo.getCoin_num() + "朵玫瑰）");
//            }
//        }
        //粉丝贡献
        List<ProfileBean.FansRank> fansRanks = result.getRank_list();
        if (fansRanks != null) {
            for (int i = 0; i < fansRanks.size(); i++) {
                ProfileBean.FansRank bean = fansRanks.get(i);
                ImageView fansIv = null;
                switch (i) {
                    case 0:
                        fansIv = mBinding.goldIv;
                        break;
                    case 1:
                        fansIv = mBinding.silverIv;
                        break;
                    case 2:
                        fansIv = mBinding.copperIv;
                        break;
                }
                if (fansIv != null) {
                    GlideUtils.circleImage(fansIv, bean.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
                }
            }
        }
        //家长、青少年模式
        ProfileBean.SafetyBean info = result.getMonitoring_info();
        boolean isOpenParent = info != null && info.getParents_status() == 1;
        boolean isOpenYounger = info != null && info.getMonitoring_status() == 1;
        mBinding.parentTv.setSelected(isOpenParent);
        mBinding.parentTv.setText(isOpenParent ? "已开启" : "未开启");
        mBinding.youngerTv.setSelected(isOpenYounger);
        mBinding.youngerTv.setText(isOpenYounger ? "已开启" : "未开启");
        UserUtils.getUser().setOpenParent(isOpenParent);
        UserUtils.getUser().setOpenYounger(isOpenYounger);

        bind_type = mProfile.getBind_type();//显示绑定信息 1:绑定微信   2:绑定手机号
        ViewBindUtils.setText(mBinding.tvBindTitle, bind_type == 2 ? "绑定手机号" : "绑定微信");
        ViewBindUtils.setImageRes(mBinding.ivPhoneBind, bind_type == 2 ? R.drawable.ic_bind_phone : R.drawable.icon_wx_bind);

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
                if (result.getCode() == 200) {
                    mBinding.authTv.setSelected(true);
                    mBinding.authTv.setText("已认证");
                }
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

    /*
    上传头像 ---------------------------------------------------
     */
    private String mAvatarFilePath;

    private void showPhotoDialog() {
        if (mPhotoDialog == null) {
            mPhotoDialog = new PhotoDialog();
            mPhotoDialog.setOnPhotoSelectedListener(path -> {
                mAvatarFilePath = path;
                Log.d(TAG, "showPhotoDialog: " + path);
                showProgress();
                mEditViewModel.getOssConfig().observe(this, new BaseObserver<OSSConfigBean>() {
                    @Override
                    public void onSuccess(OSSConfigBean result) {
                        if (result != null) {
                            upLoadHeadIv(result, path);
                        } else {
                            hideProgress();
                            toastShort("上传失败");
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        super.onError(msg, code);
                        hideProgress();
                    }
                });
            });
        }
        mPhotoDialog.showAtBottom(getChildFragmentManager());
    }

    private OSSAsyncTask ossAsyncTask;

    private void upLoadHeadIv(OSSConfigBean config, String aHeadIv) {
        mExecutors.diskIO().execute(() -> {
            OSS oSs = OssUpUtil.getInstance().getOssConfig(mContext, config.AccessKeyId, config.AccessKeySecret, config.SecurityToken, config.Expiration);
            ossAsyncTask = OssUpUtil.getInstance().upToOss(3, aHeadIv, oSs, config.BucketName, new OssUpUtil.OssUpCallback() {
                @Override
                public void upSuccessFile(String objectKey) {
                    mHandler.post(() -> uploadAvatar(objectKey));
                }

                @Override
                public void upProgress(long progress, long zong) {
                }

                @Override
                public void upOnFailure() {
                    if (ossAsyncTask != null) {//取消上传任务
                        ossAsyncTask.cancel();
                        ossAsyncTask = null;
                    }
                    mHandler.post(() -> {
                        hideProgress();
                        ToastUtils.showToast("上传失败");
                    });
                }
            });
        });
    }

    private void uploadAvatar(String avatar) {
        mEditViewModel.setavatar(avatar).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                hideProgress();
                if (!TextUtils.isEmpty(mAvatarFilePath)) {
                    GlideUtils.circleImageByFile(mBinding.avatarIv, mAvatarFilePath, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
                    mAvatarFilePath = null;
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                hideProgress();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPhotoDialog != null) {
            mPhotoDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeFindTab(ShowMineDialog one) {
        if (one.getType() == 0) {
            showPhotoDialog();
        } else if (one.getType() == 1) {
            auth();
        }
    }
}
