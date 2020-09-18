package com.deepsea.mua.stub.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.deepsea.mua.core.dialog.LoadingDialog;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.BuildConfig;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.utils.AppManager;
import com.deepsea.mua.stub.utils.Const;
import com.deepsea.mua.stub.utils.DisposeUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.view.MarqueeTextView;
import com.jakewharton.rxbinding2.view.RxView;
import com.noober.background.BackgroundLibrary;
import com.uber.autodispose.AutoDisposeConverter;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by JUN on 2019/3/22
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends FragmentActivity
        implements HasSupportFragmentInjector {

    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;
    protected T mBinding;
    protected boolean isFinishing = false;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    protected LoadingDialog mLoadingDialog;

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void initListener() {
    }

    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
    }

    protected void handleSavedInstanceState(Bundle savedInstanceState) {
    }

    protected void setStatusBarColor(int colorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ResUtils.getColor(mContext, colorRes));
        }
    }

    protected void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //去除半透明状态栏   
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    protected void setNativeLightStatusBar(boolean dark) {
        View decor = getWindow().getDecorView();
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        AppManager.getAppManager().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTranslucentStatus();
        setNativeLightStatusBar(true);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mContext = this;

//        MarqueeTextView maquee=findViewById(R.id.tv_marquee);
//        if (maquee!=null){
//            toastShort("maquee");
//         String maqueeData=   SharedPrefrencesUtil.getData(this,"maquee","maquee","");
//         if (!TextUtils.isEmpty(maqueeData)){
//             //
//             maquee.setMarqueeNum(-1);
//             maquee.setText(maqueeData);
//             findViewById(R.id.rl_marquee).setVisibility(View.VISIBLE);
//         }else {
//             findViewById(R.id.rl_marquee).setVisibility(View.GONE);
//         }
//        }
        try {
            ARouter.getInstance().inject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (savedInstanceState != null) {
            handleSavedInstanceState(savedInstanceState);
        } else if (getIntent() != null) {
            handleIntent(getIntent(), false);
        }
        View view = findViewById(R.id.back_iv);
        if (view != null) {
            subscribeClick(view, o -> finish());
        }
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause: ");
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            outState.remove(FRAGMENTS_TAG);
        }
    }

    protected void toastShort(String msg) {
        ToastUtils.showToast(msg);
    }

    protected void toastShort(BaseApiResult result) {
        if (result != null) {
            toastShort(result.getDesc());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
//            handleIntent(intent, true);
        }
    }

    protected void subscribeClick(View view, Consumer<Object> consumer) {
        Disposable subscribe = RxView.clicks(view)
                .throttleFirst(Const.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(consumer);
    }

    protected <T> AutoDisposeConverter<T> autoDisposable() {
        return DisposeUtils.autoDisposable(this);
    }

    public void pushFragment(int containerId, BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(containerId, fragment)
                    .addToBackStack(((Object) fragment).getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    public boolean popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        hideProgress();
        mLoadingDialog = null;
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        AudioManager am = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, 0);
//                am.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_SAME, 0);
//                am.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, 0);
//                am.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_SAME, 0);
//                am.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
//                return true;
//            default:
//                break;
//        }

        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示进度条
     *
     * @param message
     */
    public void showProgress(String message) {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(mContext, message);
            }
            mLoadingDialog.show();
        } catch (Exception e) {

        }

    }

    protected void showProgress() {
        try {
            showProgress(null);
        } catch (Exception e) {

        }

    }

    /**
     * 隐藏进度条
     */
    public void hideProgress() {
//        if (isFinishing()) {
//            return;
//        }
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: requestCode = " + requestCode + " resultCode = " + resultCode + " data = " + data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int index = 0; index < fragmentManager.getFragments().size(); index++) {
            Fragment fragment = fragmentManager.getFragments().get(index);
            if (fragment == null)
                Log.w(TAG, "Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            else
                handleResult(fragment, requestCode, resultCode, data);
        }
    }

    /**
     * 递归调用，对所有的子Fragment生效
     *
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragment.getChildFragmentManager().getFragments();
        if (!fragments.isEmpty())
            for (Fragment f : fragments)
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
    }

    @Override
    public void finish() {
        isFinishing = true;
        super.finish();
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
