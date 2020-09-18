package org.cocos2dx.javascript.ui.login.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.LocationVo;
import com.deepsea.mua.stub.utils.MobEventUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.StringUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.fm.openinstall.OpenInstall;
import com.hh.game.R;
import com.hh.game.databinding.ActivityLoginBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import org.cocos2dx.javascript.ui.login.viewmodel.LoginViewModel;
import org.cocos2dx.javascript.ui.main.MainActivity;
import org.cocos2dx.javascript.utils.LocaltionUtils;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by JUN on 2019/3/22
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    @Inject
    ViewModelFactory mViewModelFactory;
    private LoginViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(LoginViewModel.class);
        mBinding.setIsPasswordLogin(false);
        initVideView();
        int matchNum = (int) (7000 * Math.random() + 2000);
        ViewBindUtils.setText(mBinding.tvMatchNum, String.valueOf(matchNum));
        initRegisterProtocol();
        requestPermissions();
    }

    /**
     * 播放视频
     */
    private void initVideView() {
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

    private void requestPermissions() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .as(autoDisposable())
                .subscribe(aBoolean -> {
                    showProgress();
                    LocaltionUtils.getInstance().location(getApplicationContext(), new LocaltionUtils.OnLocationResultListener() {
                        @Override
                        public void onSuccess(LocationVo location) {
                            hideProgress();
                        }

                        @Override
                        public void OnFail(int code) {
                            hideProgress();

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
                ds.setColor(Color.parseColor("#FFFFFF"));
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

                ds.setColor(Color.parseColor("#FFFFFF"));
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

    private void showLoginLoading() {
        GlideUtils.loadGif(mBinding.loginBtn, R.drawable.icon_login_gif_loading);
    }

    private void hideLoginLoading() {
        ViewBindUtils.setImageRes(mBinding.loginBtn, R.drawable.icon_login_arrow);
    }

    public void onClick(View view) {
        //切换登录方式
        if (view == mBinding.vertifyLoginTv) {
            mBinding.setIsPasswordLogin(!mBinding.getIsPasswordLogin());
            mBinding.passwordEdit.setText("");
            mBinding.passwordEdit.setHint(mBinding.getIsPasswordLogin() ? "请输入密码" : "请输入验证码");
            mBinding.vertifyLoginTv.setText(mBinding.getIsPasswordLogin() ? "验证码登录" : "密码登录");
        }

        String account = mBinding.accountEdit.getText().toString();
        String password = mBinding.passwordEdit.getText().toString();
        //登录
        if (view == mBinding.loginBtn) {
            if (!StringUtils.isMobileNO(account)) {
                toastShort("请输入正确的手机号");
                return;
            }
            if (mBinding.passwordEdit.getText().toString().length() != 6) {
                toastShort("请输入正确的验证码");
                return;
            }
            showLoginLoading();
            String registration_id = JPushInterface.getRegistrationID(mContext);
            mViewModel.login(account, password, registration_id).observe(this, new BaseObserver<User>() {

                @Override
                public void onError(String msg, int code) {
                    switch (code) {
                        //第一次注冊用戶
                        case 2000: {
                            OpenInstall.reportEffectPoint("mobile_verified", 1);
                            mBinding.passwordEdit.getEditableText().clear();
                            hideLoginLoading();
                            Intent intent = new Intent(mContext, RegisterActivity.class);
                            intent.putExtra("mobile", account);
                            startActivity(intent);
                        }
                        break;
                        default:
                            hideLoginLoading();
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
        //获取验证码
        else if (view == mBinding.getCodeTv) {
            if (!StringUtils.isMobileNO(account)) {
                toastShort("请输入正确的手机号");
                return;
            }
            mViewModel.sendSMS(account, "1").observe(this, new BaseObserver<BaseApiResult>() {
                @Override
                public void onSuccess(BaseApiResult result) {
                    if (result != null) {
                        toastShort(result.getDesc());
                    }
                }
            });
            mViewModel.initSendCaptchaTv(mBinding.getCodeTv);
            mBinding.passwordEdit.setFocusable(true);
            mBinding.passwordEdit.setFocusableInTouchMode(true);
            mBinding.passwordEdit.requestFocus();
        }
    }

    private void saveUserAndNext(User user) {
        UserUtils.saveUser(user);
        mViewModel.loginHx(user.getUid(), user.getUid()).observe(this, new BaseObserver<Object>() {
            @Override
            public void onSuccess(Object result) {
                hideLoginLoading();
                AppClient.getInstance().login(user.getUid());
                MobclickAgent.onProfileSignIn(user.getUid());
                MobEventUtils.onLoginEvent(mContext);
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String msg, int code) {
                hideLoginLoading();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.cancelTimer();
    }
}
