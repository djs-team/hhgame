package org.cocos2dx.javascript.ui.login.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;

import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.LocationVo;
import com.deepsea.mua.stub.entity.UserBean;
import com.deepsea.mua.stub.jpush.JpushUtils;
import com.deepsea.mua.stub.jpush.OnJpushClickListener;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.MobEventUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.fm.openinstall.OpenInstall;
import com.hh.game.R;
import com.hh.game.databinding.ActivityLoginMainBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.cocos2dx.javascript.ui.login.viewmodel.LoginViewModel;
import org.cocos2dx.javascript.ui.main.MainActivity;
import org.cocos2dx.javascript.utils.LocaltionUtils;

import javax.inject.Inject;

import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.PreLoginListener;
import cn.jiguang.verifysdk.api.RequestCallback;
import cn.jiguang.verifysdk.api.VerifyListener;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by JUN on 2019/3/22
 */
public class LoginMainActivity extends BaseActivity<ActivityLoginMainBinding> {

    @Inject
    ViewModelFactory mViewModelFactory;
    private LoginViewModel mViewModel;
    private int winWidth;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_main;
    }

    /**
     * 播放视频
     */
    private void initVideView() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        Log.d(TAG, "winHeight px=" + point.y);
        if (point.x > point.y) {
            winWidth = point.y;
        } else {
            winWidth = point.x;
        }


        AppConstant.getInstance().setLoginOut(true);
        //播放路径
        mBinding.loginVv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vidio));
        //播放
        mBinding.loginVv.start();
        //循环播放
        mBinding.loginVv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mBinding.loginVv.start();
            }
        });
        JVerificationInterface.preLogin(this, 3000, new PreLoginListener() {
            @Override
            public void onResult(final int code, final String content) {
                Log.d(TAG, "[" + code + "]message=" + content);
            }
        });
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        initVideView();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        mBinding.loginVv.stopPlayback();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        JVerificationInterface.clearPreLoginCache();
        super.onDestroy();
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(LoginViewModel.class);
        initRegisterProtocol();
        requestPermissions();
        initVideView();
        mBinding.ivAgreementCheck.setSelected(true);
    }

    private void requestPermissions() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .as(autoDisposable())
                .subscribe(aBoolean -> {
                    LocaltionUtils.getInstance().location(getApplicationContext(), new LocaltionUtils.OnLocationResultListener() {
                        @Override
                        public void onSuccess(LocationVo location) {

                        }

                        @Override
                        public void OnFail(int code) {
                        }
                    });
                });
    }


    private void initRegisterProtocol() {
        String protocol = "登录注册即同意 《用户协议》 、《合合隐私协议》";
        SpannableString spannableString = new SpannableString(protocol);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#FF0BC9E0"));
            }

            @Override
            public void onClick(@NonNull View widget) {
                PageJumpUtils.jumpToWeb(AddressCenter.getAddress().getRegisterProtocol());
            }
        }, 7, 13, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);

                ds.setColor(Color.parseColor("#FF0BC9E0"));
            }

            @Override
            public void onClick(@NonNull View widget) {
                PageJumpUtils.jumpToWeb(AddressCenter.getAddress().getPrivacyPolicy());
            }
        }, 16, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.protocolTv.setHighlightColor(Color.TRANSPARENT);
        mBinding.protocolTv.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.protocolTv.setText(spannableString);
    }

    private void showWechatConfirmDialog() {
        thirdLogin();
    }

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.ivLoginWechat, o -> {
            if (!mBinding.ivAgreementCheck.isSelected()) {
                toastShort("请勾选底部协议");
                return;
            }
            showWechatConfirmDialog();
//            startActivity(new Intent(mContext,PermissionReqActivity.class));
        });
        subscribeClick(mBinding.ivLoginPhone, o -> {
            if (!mBinding.ivAgreementCheck.isSelected()) {
                toastShort("请勾选底部协议");
                return;
            }
            loginAndRegister();
        });
        subscribeClick(mBinding.rlAgreementCheck, o -> {
            mBinding.ivAgreementCheck.setSelected(!mBinding.ivAgreementCheck.isSelected());
        });


    }

    private OnJpushClickListener onJpushClickListener = new OnJpushClickListener() {
        @Override
        public void wxLogin() {
            showWechatConfirmDialog();
        }

        @Override
        public void phoneLogin() {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
    };

    private void loginAndRegister() {
        JVerificationInterface.setCustomUIWithConfig(JpushUtils.getFullScreenLandscapeConfig(mContext, "android.resource://" + getPackageName() + "/" + R.raw.vidio, onJpushClickListener), JpushUtils.getFullScreenLandscapeConfig(mContext, "android.resource://" + getPackageName() + "/" + R.raw.vidio, onJpushClickListener));
        JVerificationInterface.loginAuth(this, false, new VerifyListener() {
            @Override
            public void onResult(int code, String content, String operator) {
                if (code == 6000) {
                    oneLogin(content);
                    Log.d(TAG, "code=" + code + ", token=" + content + " ,operator=" + operator);
                } else if (code == 6002) {
                    return;
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                JVerificationInterface.dismissLoginAuthActivity(false, new RequestCallback<String>() {
                    @Override
                    public void onResult(int code, String desc) {
                        Log.i(TAG, "[dismissLoginAuthActivity] code = " + code + " desc = " + desc);
                    }
                });
            }
        }, new AuthPageEventListener() {
            @Override
            public void onEvent(int cmd, String msg) {
                Log.d(TAG, "[onEvent]. [" + cmd + "]message=" + msg);
            }
        });
    }

    private void oneLogin(String token) {
        showProgress();
        String registration_id = JPushInterface.getRegistrationID(mContext);
        mViewModel.oneLogin(registration_id, token).observe(this, new BaseObserver<User>() {

            @Override
            public void onError(String msg, int code) {
                switch (code) {
                    //第一次注冊用戶
                    case 2000: {
                        OpenInstall.reportEffectPoint("one_login", 1);
                        hideProgress();
                        Intent intent = new Intent(mContext, RegisterActivity.class);
                        intent.putExtra("mobile", msg);
                        startActivity(intent);
                    }
                    break;
                    default:
                        hideProgress();
                        toastShort(msg);
                        break;
                }
            }

            @Override
            public void onSuccess(User result) {
                saveUserAndNext(result);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void thirdLogin() {
        Log.d("location", JsonConverter.toJson(AppConstant.getInstance().getLocationVo()));
        String registration_id = JPushInterface.getRegistrationID(mContext);
        double latitude = 0;
        double longitude = 0;
        String state = "";
        String city = "";
        String locality = "";
        if (AppConstant.getInstance().getLocationVo() != null) {
            latitude = AppConstant.getInstance().getLocationVo().getLatitude();
            longitude = AppConstant.getInstance().getLocationVo().getLongitude();
            state = AppConstant.getInstance().getLocationVo().getProvince();
            city = AppConstant.getInstance().getLocationVo().getCity();
            locality = String.valueOf(AppConstant.getInstance().getLocationVo().getArea());
        }
        mViewModel.thirdlogin(SHARE_MEDIA.WEIXIN.getName(), LoginMainActivity.this, String.valueOf(latitude), String.valueOf(longitude), state, city, locality, registration_id)
                .observe(this, new BaseObserver<UserBean>() {
                    @Override
                    public void onError(String msg, int code) {
                        if (code == 2000) {
                            startActivity(BindPhoneActivity.newIntent(mContext, mViewModel.getOpenRes()));
                        } else {
                            toastShort(msg);
                        }
                    }

                    @Override
                    public void onSuccess(UserBean result) {
                        //该用户没有绑定过 进入绑定页面
                        if (result != null && result.getInfo() != null) {
                            saveUserAndNext(result.getInfo());
                        }
                    }
                });
    }

    private void saveUserAndNext(User user) {
        UserUtils.saveUser(user);
        mViewModel.loginHx(user.getUid(), user.getUid()).observe(this, new BaseObserver<Object>() {
            @Override
            public void onSuccess(Object result) {
                AppClient.getInstance().login(user.getUid());
                MobclickAgent.onProfileSignIn(user.getUid());
                MobEventUtils.onLoginEvent(mContext);
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String msg, int code) {
                UserUtils.clearUser();
                super.onError(msg, code);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            MobEventUtils.onExitEvent(mContext);
        }
        return super.onKeyDown(keyCode, event);
    }

}
