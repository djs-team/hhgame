package org.cocos2dx.javascript.ui.splash.activity;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.utils.NetWorkUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.dialog.SexEditDialog;
import com.deepsea.mua.stub.entity.ChessLoginParam;
import com.deepsea.mua.stub.entity.LocationVo;
import com.deepsea.mua.stub.utils.MobEventUtils;
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
import com.umeng.analytics.MobclickAgent;

import org.cocos2dx.javascript.app.App;
import org.cocos2dx.javascript.ui.main.MainActivity;
import org.cocos2dx.javascript.ui.splash.viewmodel.SplashViewModel;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;

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


    private Handler mHandler = new Handler();
    private ChessLoginParam param;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mFactory).get(SplashViewModel.class);
        param = (ChessLoginParam) getIntent().getSerializableExtra("chessLoginParam");
        requestPermissions();
    }


    private void requestPermissions() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION)
                .as(autoDisposable())
                .subscribe(aBoolean -> {
                    gotoNextPage();


                });
    }

    private void gotoNextPage() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                autoLogin();
                loadAllConversations();
            }
        });
    }

    private void startSplash() {
        long startTime = SPUtils.getLong(App.START_TIME_KEY, 0);
        if (startTime == 0) {
            mHandler.postDelayed(() -> {
                startNext();
            }, mSplashTime);
        } else {
            SPUtils.remove(App.START_TIME_KEY);
            startNext();
        }
    }

    private void startNext() {


        Intent intent = new Intent(mContext, MainActivity.class);
        if (null != getIntent()) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                intent.putExtras(bundle);
            }
        }
        startActivity(intent);
        finish();

    }

    private void autoLogin() {
        String registration_id = JPushInterface.getRegistrationID(mContext);
        param.setRegistration_id(registration_id);
        mViewModel.login(param).observe(this, new BaseObserver<User>() {
            @Override
            public void onError(String msg, int code) {
                Log.d("-login----------", code + msg);
                if (code == 2002) {
                    AAlertDialog dialog = new AAlertDialog(mContext);
                    dialog.setTitle("提示");
                    dialog.setMessage(msg);
                    dialog.setButton("确定", new AAlertDialog.OnClickListener() {
                        @Override
                        public void onClick(View v, Dialog d) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    dialog.show();
                }
                ToastUtils.showToast(msg);
            }

            @Override
            public void onSuccess(User user) {
                fetchLoginHX(user);

            }
        });
    }

    private void fetchLoginHX(User user) {
        mViewModel.loginHx(user.getUid(), user.getUid()).observe(this, new BaseObserver<Object>() {
            @Override
            public void onSuccess(Object result) {
                UserUtils.saveUser(user);
                AppClient.getInstance().login(user.getUid());
                String sex = user.getSex();
                if (sex.equals("0")) {
                    SexEditDialog sexEditDialog = new SexEditDialog(mContext);
                    sexEditDialog.setCancelable(false);
                    sexEditDialog.setListener(new SexEditDialog.onSexEditListener() {
                        @Override
                        public void onResult(int result) {
                            if (result == -1) {
                                finish();
                            } else if (result == 0) {
                                startSplash();
                            } else {
                                //修改性别失败--暂不处理
                            }
                        }
                    });
                    sexEditDialog.show();
                } else {
                    startSplash();
                }
            }

            @Override
            public void onError(String msg, int code) {
                ToastUtils.showToast(msg);
                UserUtils.clearUser();
                super.onError(msg, code);
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
    }
}
