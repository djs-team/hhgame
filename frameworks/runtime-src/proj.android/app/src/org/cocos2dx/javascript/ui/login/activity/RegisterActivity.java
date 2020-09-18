package org.cocos2dx.javascript.ui.login.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.deepsea.mua.core.login.ApiUser;
import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.dialog.WheelDialog;
import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.dialog.PhotoDialog;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.network.download.DownloadListener;
import com.deepsea.mua.stub.network.download.DownloadUtils;
import com.deepsea.mua.stub.utils.CacheUtils;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.MobEventUtils;
import com.deepsea.mua.stub.utils.OssUpUtil;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.fm.openinstall.OpenInstall;
import com.hh.game.R;
import com.hh.game.databinding.ActivityRegisterBinding;
import com.umeng.analytics.MobclickAgent;

import org.cocos2dx.javascript.ui.login.viewmodel.LoginViewModel;
import org.cocos2dx.javascript.ui.login.viewmodel.RegisterViewModel;
import org.cocos2dx.javascript.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by JUN on 2019/10/16
 */
public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    @Inject
    ViewModelFactory mFactory;
    private RegisterViewModel mViewModel;
    private LoginViewModel mLoginViewModel;

    private String mobile;
    private ApiUser mOpenUser;
    private String avatarKey = "";
    private int age;
    private String sex;
    String inviteCode = "";
    private PhotoDialog mPhotoDialog;
    private WheelDialog mWheelDialog;
    private String wx_id = "";

    @Inject
    AppExecutors mExecutors;

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        mobile = intent.getStringExtra("mobile");
        mOpenUser = (ApiUser) intent.getSerializableExtra("mOpenUser");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mFactory).get(RegisterViewModel.class);
        mLoginViewModel = ViewModelProviders.of(this, mFactory).get(LoginViewModel.class);
        ViewBindUtils.setText(mBinding.nickEdit, "游客_" + (int) ((Math.random() * 9 + 1) * 100000));
        inviteCode = SharedPrefrencesUtil.getData(mContext, "inviteCode", "inviteCode", "");
        mBinding.manIv.setSelected(true);
        addTextWatcher();
        if (!TextUtils.isEmpty(inviteCode)) {
            PageJumpUtils.jumpToInviteAlert(inviteCode);
        }
        if (mOpenUser != null) {
            if (!TextUtils.isEmpty(mOpenUser.getUserIcon())) {
                downLoad(mOpenUser.getUserIcon());
            }
            if (!TextUtils.isEmpty(mOpenUser.getUserName())) {
                ViewBindUtils.setText(mBinding.nickEdit, mOpenUser.getUserName());
            }
            if (!TextUtils.isEmpty(mOpenUser.getSex())) {
                if (mOpenUser.getSex().equals("男")) {
                    mBinding.manIv.setSelected(true);
                    mBinding.womenIv.setSelected(false);
                } else if (mOpenUser.getSex().equals("女")) {
                    mBinding.manIv.setSelected(false);
                    mBinding.womenIv.setSelected(true);
                }
            }
            if (!TextUtils.isEmpty(mOpenUser.getOpenId())) {
                wx_id = mOpenUser.getOpenId();
            }
        }
    }

    @Override
    protected void initListener() {

        subscribeClick(mBinding.avatarIv, o -> {
            showPhotoDialog();
        });
        subscribeClick(mBinding.ageLayout, o -> {
            showAgeWheel();
        });
        subscribeClick(mBinding.registerBtn, o -> {
            sex = mBinding.manIv.isSelected() ? "1" : "2";
            register();
        });
        subscribeClick(mBinding.manIv, o -> {
            mBinding.manIv.setSelected(true);
            mBinding.womenIv.setSelected(false);
        });
        subscribeClick(mBinding.womenIv, o -> {
            mBinding.manIv.setSelected(false);
            mBinding.womenIv.setSelected(true);
        });
    }

    private void addTextWatcher() {
        mBinding.nickEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                toggleRegisterStatus();
            }
        });
    }

    private void toggleRegisterStatus() {
        boolean enable = !TextUtils.isEmpty(mBinding.nickEdit.getText().toString())
                && mBinding.nickEdit.getText().toString().length() >= 2
//                && !TextUtils.isEmpty(avatarKey)
                && age > 0;
        ViewBindUtils.setEnable(mBinding.registerBtn, enable);
        ViewBindUtils.setTextColor(mBinding.registerBtn, enable ? R.color.black : R.color.white);
    }

    private void showAgeWheel() {
        if (mWheelDialog == null) {
            mWheelDialog = new WheelDialog(mContext);
            mWheelDialog.setWheelDialogListener(value -> {
                if (!TextUtils.isEmpty(value)) {
                    age = Integer.parseInt(value);
                    toggleRegisterStatus();
                    mBinding.ageTv.setText(value);
                }
            });
        }
        List<String> ages = new ArrayList<>();
        for (int i = 18; i <= 60; i++) {
            ages.add(String.valueOf(i));
        }
        mWheelDialog.setTitle("选择年龄");
        mWheelDialog.setEntries(ages);
        int index = ages.indexOf(String.valueOf(age));
        mWheelDialog.setCurrentIndex(index >= 0 ? index : 0);
        mWheelDialog.show();
    }

    private void showPhotoDialog() {
        if (mPhotoDialog == null) {
            mPhotoDialog = new PhotoDialog();
            mPhotoDialog.setOnPhotoSelectedListener(path -> {
                Log.d(TAG, "showPhotoDialog: " + path);
                showProgress();
                getOssConfig(path);
            });
        }
        mPhotoDialog.showAtBottom(getSupportFragmentManager());
    }

    private void getOssConfig(String path) {
        mViewModel.getOssConfig().observe(this, new BaseObserver<OSSConfigBean>() {
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
    }

    private OSSAsyncTask ossAsyncTask;

    private void upLoadHeadIv(OSSConfigBean config, String aHeadIv) {
        mExecutors.diskIO().execute(() -> {
            OSS oSs = OssUpUtil.getInstance().getOssConfig(this, config.AccessKeyId, config.AccessKeySecret, config.SecurityToken, config.Expiration);
            ossAsyncTask = OssUpUtil.getInstance().upToOss(3, aHeadIv, oSs, config.BucketName, new OssUpUtil.OssUpCallback() {
                @Override
                public void upSuccessFile(String objectKey) {
                    Log.d(TAG, "upSuccessFile: " + objectKey);
                    avatarKey = objectKey;
                    runOnUiThread(() -> {
                        GlideUtils.circleImageByFile(mBinding.avatarIv, aHeadIv, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
                        hideProgress();
                        toggleRegisterStatus();
                    });
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
                    runOnUiThread(() -> {
                        hideProgress();
                        ToastUtils.showToast("上传失败");
                    });
                }
            });
        });
    }


    private void register() {

//        Log.d("OpenInstall", "register : inviteCode = " + inviteCode);
//        String channelName= ApkUtils.getChannelName(mContext);
//        if (channelName.contains("QuDao")){
//            inviteCode= BuildConfig.InviteCode;
//            Log.d("OpenInstall", "containsQuDao = " + channelName);
//        }else {
//            Log.d("OpenInstall", "QuDao = " + channelName);
//        }
        String registration_id = JPushInterface.getRegistrationID(mContext);

        mViewModel.register(mobile, avatarKey, mBinding.nickEdit.getText().toString(), age, sex, inviteCode, registration_id, wx_id)
                .observe(this, new ProgressObserver<User>(mContext) {
                    @Override
                    public void onSuccess(User result) {
                        OpenInstall.reportRegister();
                        OpenInstall.reportEffectPoint("register_ok", 1);
                        SharedPrefrencesUtil.saveData(mContext, "inviteCode", "inviteCode", "");
                        saveUserAndNext(result);
                        MobEventUtils.onRegisterEvent(mContext, mobile);
                    }

                    @Override
                    public void onError(String msg, int code) {
                        super.onError(msg, code);
                    }
                });
    }

    private void saveUserAndNext(User user) {
        UserUtils.saveUser(user);
        mLoginViewModel.loginHx(user.getUid(), user.getUid()).observe(this, new BaseObserver<Object>() {
            @Override
            public void onSuccess(Object result) {
                hideProgress();
                AppClient.getInstance().login(user.getUid());
                MobclickAgent.onProfileSignIn(user.getUid());
                MobEventUtils.onLoginEvent(mContext);
                ActivityCache.getInstance().finish(BindPhoneActivity.class);

                ActivityCache.getInstance().finish(LoginActivity.class);
                ActivityCache.getInstance().finish(LoginMainActivity.class);
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String msg, int code) {
                hideProgress();
                UserUtils.clearUser();
                super.onError(msg, code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mPhotoDialog != null) {
            mPhotoDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void downLoad(String picUrl) {
        showProgress();
        DownloadUtils.download(picUrl, CacheUtils.getAvatarDir(), new DownloadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long total, long current) {
                hideProgress();
            }

            @Override
            public void onFinish(String path) {
                Message message = Message.obtain();
                message.what = Constant.MessageCode.msg_getoss;
                message.obj = path;
                mHandler.sendMessage(message);

            }

            @Override
            public void onFail(String error) {
                hideProgress();
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.MessageCode.msg_getoss:
                    String path = (String) msg.obj;
                    getOssConfig(path);
                    break;
            }
        }
    };
}
