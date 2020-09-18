package com.deepsea.mua.stub.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepsea.mua.core.di.Injectable;
import com.deepsea.mua.core.dialog.LoadingDialog;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.utils.Const;
import com.deepsea.mua.stub.utils.DisposeUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.uber.autodispose.AutoDisposeConverter;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by JUN on 2019/3/22
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment implements Injectable {

    protected final String TAG = getClass().getSimpleName();

    protected T mBinding;
    private View mRootView;
    protected Context mContext;
    protected Bundle mBundle;

    //lazy
    private boolean isVisible;
    private boolean isPrepared;
    private boolean isLoaded;

    /**
     * 设置xml布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     *
     * @param view
     */
    protected abstract void initView(View view);

    protected void initListener() {
    }

    /**
     * 控制是否缓存view
     *
     * @return
     */
    protected boolean cacheView() {
        return true;
    }

    protected boolean isLazyView() {
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBundle != null) {
            outState.putBundle("bundle", mBundle);
        }
    }

    /**
     * 绑定activity
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * 运行在onAttach之后
     * 可以接受别人传递过来的参数,实例化对象.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取bundle,并保存起来
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle("bundle");
        } else {
            mBundle = getArguments() == null ? new Bundle() : getArguments();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
        if (isVisible) {
            onLazyLoad();
        }
    }

    private void onLazyLoad() {
        if (isPrepared && isVisible && !isLoaded) {
            init();
        }
    }

    private void init() {
        isLoaded = true;
        initView(mRootView);
        initListener();
    }

    /**
     * 运行在onCreate之后
     * 生成view视图
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cacheView()) {
            if (mRootView == null) {
                createView(inflater, container);
            }
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        } else {
            createView(inflater, container);
        }
        if (isLazyView()) {
            onLazyLoad();
        } else {
            init();
        }
        return mRootView;
    }

    private void createView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mRootView = mBinding.getRoot();
        isPrepared = true;
    }

    /**
     * 运行在onCreateView之后
     * 加载数据
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void subscribeClick(View view, Consumer<Object> consumer) {
        Disposable subscribe = RxView.clicks(view)
                .throttleFirst(Const.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(consumer);
    }

    protected <T> AutoDisposeConverter<T> autoDisposable() {
        return DisposeUtils.autoDisposable(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = LeakcanaryHelp.create().getRefWatcher();
//        if (refWatcher != null) {
//            refWatcher.watch(this);
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void toastShort(String msg) {
        ToastUtils.showToast(msg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }


    /**
     * 显示进度条
     *
     * @param message
     */
    protected LoadingDialog mLoadingDialog;

    protected void showProgress(String message) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext, message);
        }
        mLoadingDialog.show();
    }

    protected void showProgress() {
        showProgress(null);
    }

    /**
     * 隐藏进度条
     */
    protected void hideProgress() {

        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面("MainScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());

    }
}
