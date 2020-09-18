package org.cocos2dx.javascript.ui.splash.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.utils.NetWorkUtils;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.LocationVo;
import com.deepsea.mua.stub.utils.SPUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;
import com.hh.game.R;
import com.hh.game.databinding.ActivitySplashBinding;
import com.hyphenate.chat.EMClient;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.cocos2dx.javascript.app.App;
import org.cocos2dx.javascript.ui.login.activity.LoginMainActivity;
import org.cocos2dx.javascript.ui.main.MainActivity;
import org.cocos2dx.javascript.ui.splash.viewmodel.SplashViewModel;
import org.cocos2dx.javascript.utils.LocaltionUtils;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/15
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    @Inject
    AppExecutors mExecutors;
    @Inject
    ViewModelFactory mFactory;
    private SplashViewModel mViewModel;

    private int mSplashTime = 2000;

    private boolean mSplashEnd;
    private int mLoginStatus;

    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 此处要调用，否则App在后台运行时，会无法截获
        OpenInstall.getWakeUp(intent, wakeUpAdapter);

    }


    @Override
    protected void initView() {
//        int height = 1024;
//        int width = 760;
//        VideoManager  mVideoManager = VideoManager.createInstance(mContext);
//        mVideoManager.allocate(width, height, -1, com.android.beauty.constant.Constant.CAMERA_FACING_FRONT);
//        mVideoManager.stopCapture();
//        mVideoManager.deallocate();


        OpenInstall.getWakeUp(getIntent(), wakeUpAdapter);
        boolean isFirst = SharedPrefrencesUtil.getData(this, "isFirstInstall", "isFirstInstall", true);
        if (isFirst) {
            //获取OpenInstall安装数
            OpenInstall.getInstall(new AppInstallAdapter() {
                @Override
                public void onInstall(AppData appData) {
                    //获取渠道数据
                    String channelCode = appData.getChannel();
                    //获取自定义数据
                    String bindData = appData.getData();
                    Log.d("OpenInstall", "getInstall : inviteCode = " + bindData);
                    mBinding.ivBg.post(new Runnable() {
                        @Override
                        public void run() {
                            SharedPrefrencesUtil.saveData(mContext, "inviteCode", "inviteCode", bindData);
                            SharedPrefrencesUtil.saveData(mContext, "channelCode", "channelCode", channelCode);
                        }
                    });
                }

                @Override
                public void onInstallFinish(AppData appData, Error error) {
                    super.onInstallFinish(appData, error);
                    SharedPrefrencesUtil.saveData(SplashActivity.this, "isFirstInstall", "isFirstInstall", false);

                }
            });
        }

        mViewModel = ViewModelProviders.of(this, mFactory).get(SplashViewModel.class);

//        if (!isTaskRoot()) {
//            finish();
//            return;
//        }

        requestPermissions();


    }


    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(AppData appData) {
            //获取渠道数据
            String channelCode = appData.getChannel();
            //获取绑定数据
            String bindData = appData.getData();
            mBinding.ivBg.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("OpenInstall", "onWakeUp : inviteCode = " + bindData);
                    SharedPrefrencesUtil.saveData(mContext, "inviteCode", "inviteCode", bindData);
                    SharedPrefrencesUtil.saveData(mContext, "channelCode", "channelCode", channelCode);
                }
            });
        }
    };

    private void requestPermissions() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION)
                .as(autoDisposable())
                .subscribe(aBoolean -> {
                    LocaltionUtils.getInstance().location(getApplicationContext(), new LocaltionUtils.OnLocationResultListener() {
                        @Override
                        public void onSuccess(LocationVo location) {
                            if (location != null) {
                                gotoNextPage();
                            } else {
                                gotoNextPage();

                            }
                        }

                        @Override
                        public void OnFail(int code) {
                            gotoNextPage();
                        }
                    });

                });
    }

    private void gotoNextPage() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                startSplash();
                autoLogin();
                loadAllConversations();
            }
        });
    }

    private void startSplash() {
        long startTime = SPUtils.getLong(App.START_TIME_KEY, 0);
        if (startTime == 0) {
            mHandler.postDelayed(() -> {
                mSplashEnd = true;
                startNext();
            }, mSplashTime);
        } else {
            SPUtils.remove(App.START_TIME_KEY);
            mSplashEnd = true;
            startNext();
        }
    }

    private void startNext() {
        if (mSplashEnd) {
            switch (mLoginStatus) {
                case 1:
                    Intent intent = new Intent(mContext, LoginMainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    intent = new Intent(mContext, MainActivity.class);
                    if (null != getIntent()) {
                        Bundle bundle = getIntent().getExtras();
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                    }
                    startActivity(intent);
//                    PageJumpUtils.jumpToWeb("file:////android_asset/test.html");
                    finish();
                    break;
            }
        }
    }

    private void autoLogin() {
        mLoginStatus = 0;
        mViewModel.autologin().observe(this, new BaseObserver<User>() {
            @Override
            public void onError(String msg, int code) {
                //版本更新
                if (code == 3000) {
                    return;
                }
                //code 5001 token无效 5002 token过期
                mLoginStatus = 1;
                if (!NetWorkUtils.IsNetWorkEnable(mContext) && UserUtils.getUser() != null) {
                    mLoginStatus = 2;
                }
                startNext();
            }

            @Override
            public void onSuccess(User user) {
                UserUtils.getUser().setIs_receive(user.getIs_receive());
                UserUtils.getUser().setIs_apply_match(user.getIs_apply_match());
                user = UserUtils.getUser();
                AppClient.getInstance().login(user.getUid());
                mViewModel.loginHx(user.getUid());
                mLoginStatus = 2;
                startNext();
            }
        });
    }

    private void loadAllConversations() {
        mExecutors.networkIO().execute(() -> {
            if (HxHelper.getInstance().isLoggedIn()) {
                EMClient.getInstance().chatManager().loadAllConversations();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeUpAdapter = null;
    }
}
