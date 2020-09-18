package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.dialog.AgeWheelDialog;
import com.deepsea.mua.mine.dialog.ScreenCityDialog;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.callback.CommonCallback;
import com.deepsea.mua.stub.databinding.DialogCityScreeningBinding;
import com.deepsea.mua.stub.entity.AreaVo;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.Const;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.DisposeUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.view.DoubleSlideSeekBar;
import com.jakewharton.rxbinding2.view.RxView;
import com.noober.background.BackgroundLibrary;
import com.uber.autodispose.AutoDisposeConverter;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.ArrayList;
import java.util.List;
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
@Route(path = ArouterConst.PAGE_SCREEN_DIALOG)
public class ScreenDialogActivity extends FragmentActivity
        implements HasSupportFragmentInjector {

    @Inject
    ViewModelFactory mModelFactory;
    private ProfileViewModel mViewModel;

    protected DialogCityScreeningBinding mBinding;

    private Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        mContext = this;
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileViewModel.class);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initListener();
    }

    protected int getLayoutId() {
        return R.layout.dialog_city_screening;
    }

    //    private int level;
    private String provinceCode = "0";
    private String provinceName;
    private String cityCode = "0";
    private String cityName;
    private String areaCode = "0";
    private String areaName;
    private String age;
    AgeWheelDialog ageWheelDialog = null;


    protected void initListener() {
        subscribeClick(mBinding.rlProvince, o -> {
            int level = getLevel(0);
            getArea(level, getCodeForLevel(level));
        });
        subscribeClick(mBinding.rlCity, o -> {
            int level = getLevel(1);

            getArea(level, getCodeForLevel(level));
        });
        subscribeClick(mBinding.rlArea, o -> {
            int level = getLevel(2);
            getArea(level, getCodeForLevel(level));
        });


        subscribeClick(mBinding.rlGroup, o -> {
            finish();
        });
        subscribeClick(mBinding.llBottom, o -> {

        });

        subscribeClick(mBinding.tvConfirm, o -> {
            confirm();
        });
        subscribeClick(mBinding.rlMyage, o -> {
            showAgeDialog();
        });
    }

    private void showAgeDialog() {
        if (ageWheelDialog == null) {
            ageWheelDialog = new AgeWheelDialog(mContext);
        }
        ageWheelDialog.setEnsureCallback(new CommonCallback<String>() {
            @Override
            public void onSuccess(String data) {
                age = data;
                ViewBindUtils.setText(mBinding.tvMyage, data);
            }
        });
        ageWheelDialog.setTitle("请选择年龄");
        List<String> ageList = new ArrayList<>();
        for (int i = 0; i < 43; i++) {
            ageList.add(String.valueOf(i + 18));
        }
        ageWheelDialog.setData(ageList, "", ageList, "");
        Window window = ageWheelDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = DisplayUtil.dip2px(mContext, 200);

        params.y = DisplayUtil.dip2px(mContext, 60);
        window.setAttributes(params);
        ageWheelDialog.showAtBottom();
    }

    private int getLevel(int level) {
        if (level == 1) {
            if (TextUtils.isEmpty(provinceName)) {
                return 0;
            } else {
                return 1;
            }
        } else if (level == 2) {
            if (TextUtils.isEmpty(provinceName)) {
                return 0;
            } else if (TextUtils.isEmpty(cityName)) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 0;
        }
    }

    private void getArea(int level, int code) {
        mViewModel.getArea(level, code).observe(this, new BaseObserver<List<AreaVo>>() {
            @Override
            public void onSuccess(List<AreaVo> result) {
                showAreaDialog(level, result);
            }
        });
    }

    /**
     * 通过level获取code
     *
     * @param level
     * @return
     */
    private int getCodeForLevel(int level) {
        if (level == 1) {
            return Integer.valueOf(provinceCode);
        } else if (level == 2) {
            return Integer.valueOf(cityCode);
        } else {
            return 0;
        }
    }


    /**
     * 确定
     */
    private void confirm() {
        if (TextUtils.isEmpty(provinceName)) {
            toastShort("请输入您所在的省份");
            return;
        } else if (TextUtils.isEmpty(cityName)) {
            toastShort("请输入您所在的城市");
            return;
        } else if (TextUtils.isEmpty(areaName)) {
            toastShort("请输入您所在的县城");
            return;
        }
        Intent intent = getIntent();
        intent.putExtra(Constant.Province, provinceName);
        intent.putExtra(Constant.City, cityName);
        intent.putExtra(Constant.AREA, areaName);
        intent.putExtra(Constant.Age, age);

        setResult(RESULT_OK, intent);
        finish();

    }

    /**
     * 用户上麦提示
     */
    private void showAreaDialog(int level, List<AreaVo> data) {

        ScreenCityDialog screenCityDialog = new ScreenCityDialog(mContext);
        screenCityDialog.setOnClickListener(new ScreenCityDialog.OnClickListener() {
            @Override
            public void onClick(AreaVo vo) {
                setArea(level, vo);
            }
        });
        screenCityDialog.setData(data);

        Window window = screenCityDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = DisplayUtil.dip2px(mContext, 175);
        if (level == 0) {
            params.y = DisplayUtil.dip2px(mContext, 100);
        } else if (level == 1) {
            params.y = DisplayUtil.dip2px(mContext, 72);

        } else if (level == 2) {
            params.y = DisplayUtil.dip2px(mContext, 42);
        }
        window.setAttributes(params);
        screenCityDialog.showAtBottom();
    }

    private void setArea(int level, AreaVo vo) {

        if (level == 0) {
            provinceCode = vo.getCode();
            provinceName = vo.getName();
            ViewBindUtils.setText(mBinding.tvProvince, provinceName);
        } else if (level == 1) {
            cityCode = vo.getCode();
            cityName = vo.getName();
            ViewBindUtils.setText(mBinding.tvCity, cityName);
        } else if (level == 2) {
            areaCode = vo.getCode();
            areaName = vo.getName();
            ViewBindUtils.setText(mBinding.tvArea, areaName);
        }

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
