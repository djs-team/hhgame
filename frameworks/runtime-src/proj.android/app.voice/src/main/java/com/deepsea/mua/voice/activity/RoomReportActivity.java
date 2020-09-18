package com.deepsea.mua.voice.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.deepsea.mua.core.dialog.LoadingDialog;
import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.GalleryDialog;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.ReportListBean;
import com.deepsea.mua.stub.entity.ReportPicVo;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.Const;
import com.deepsea.mua.stub.utils.DisposeUtils;
import com.deepsea.mua.stub.utils.OssUpUtil;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.ReportImageAdapter;
import com.deepsea.mua.voice.adapter.RoomReportAdapter;
import com.deepsea.mua.voice.databinding.ActivityRoomReportBinding;
import com.deepsea.mua.voice.viewmodel.RoomReportViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.luck.picture.lib.entity.LocalMedia;
import com.noober.background.BackgroundLibrary;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by JUN on 2019/4/8
 */
@Route(path = ArouterConst.PAGE_REPORT)
public class RoomReportActivity extends FragmentActivity
        implements HasSupportFragmentInjector {

    @Inject
    ViewModelFactory viewModelFactory;
    private RoomReportViewModel mViewModel;
    private RoomReportAdapter mAdapter;
    private ReportImageAdapter reportImageAdapter;
    @Inject
    AppExecutors mExecutors;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    @Autowired
    String roomId;
    @Autowired
    String uid;
    protected ActivityRoomReportBinding mBinding;
    Context mContext;


    private String is_black = "1";
    private String img1 = "";
    private String img2 = "";
    private String img3 = "";

    protected int getLayoutId() {
        return R.layout.activity_room_report;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        mContext = this;
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());

        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RoomReportViewModel.class);
        mViewModel.getReports().observe(this, new BaseObserver<List<ReportListBean>>() {
            @Override
            public void onSuccess(List<ReportListBean> result) {
                if (result != null) {
                    mAdapter.setNewData(result);
                }
            }
        });
        initRecyclerView();
        initListener();

    }

    private void initRecyclerView() {
        mAdapter = new RoomReportAdapter(mContext, R.layout.item_room_report, null);
        mBinding.reportListRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.reportListRv.setAdapter(mAdapter);
        reportImageAdapter = new ReportImageAdapter(mContext);
        reportImageAdapter.setMyListener(new ReportImageAdapter.OnReportListener() {
            @Override
            public void imgDel(int pos) {
                reportImageAdapter.getData().remove(pos);
                reportImageAdapter.notifyItemRemoved(pos);
                checkImgSelect();
            }
        });
        mBinding.rvReportImg.setLayoutManager(new GridLayoutManager(mContext, 3));
        mBinding.rvReportImg.setAdapter(reportImageAdapter);

    }

    private void checkImgSelect() {
        List<ReportPicVo> imgs = reportImageAdapter.getData();
        int selectedNum = 0;
        if (imgs != null && imgs.size() > 0) {
            selectedNum = imgs.size();
        }
        ViewBindUtils.setText(mBinding.tvImgNum, selectedNum + "/3张");

    }

    protected void initListener() {
        subscribeClick(mBinding.commitBtn, o -> {
            if (!TextUtils.isEmpty(uid)) {
                reportUser();
            }
        });
        subscribeClick(mBinding.llBlockCheck, o -> {
            if (is_black.equals("1")) {
                is_black = "0";
                ViewBindUtils.setVisible(mBinding.ivBlockSwitch, false);
            } else {
                is_black = "1";
                ViewBindUtils.setVisible(mBinding.ivBlockSwitch, true);
            }

        });
        mBinding.etReportDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ViewBindUtils.setText(mBinding.tvDescNum, s.length() + "/100字");
            }
        });
        subscribeClick(mBinding.rlPlug, o -> {
            showPhotoDialog();
        });
    }

    /*
上传图片证据 ---------------------------------------------------
 */
    GalleryDialog mPhotoDialog;

    private void showPhotoDialog() {
        if (mPhotoDialog == null) {
            mPhotoDialog = new GalleryDialog();
            mPhotoDialog.setMaxSelectdNum(1);
            mPhotoDialog.setMinSelectdNum(1);
            mPhotoDialog.setOnPhotoSelectedListener(path -> {
                showProgress();
                mViewModel.getOssConfig().observe(this, new BaseObserver<OSSConfigBean>() {
                    @Override
                    public void onSuccess(OSSConfigBean result) {
                        if (result != null) {
                            for (LocalMedia localMedia : path) {
                                upLoadHeadIv(result, localMedia.getPath());
                            }
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
        mPhotoDialog.showAtBottom(getSupportFragmentManager());
    }

    private OSSAsyncTask ossAsyncTask;

    private void upLoadHeadIv(OSSConfigBean config, String aHeadIv) {
        mExecutors.diskIO().execute(() -> {
            OSS oSs = OssUpUtil.getInstance().getOssConfig(mContext, config.AccessKeyId, config.AccessKeySecret, config.SecurityToken, config.Expiration);
            ossAsyncTask = OssUpUtil.getInstance().upToOss(5, aHeadIv, oSs, config.BucketName, new OssUpUtil.OssUpCallback() {
                @Override
                public void upSuccessFile(String objectKey) {
                    Log.d("report", "upSuccessFile:" + objectKey);
                    mHandler.post(() -> uploadAvatar(aHeadIv, objectKey));
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

    private void uploadAvatar(String localPath, String avatar) {
        hideProgress();
        ReportPicVo reportPicVo = new ReportPicVo(localPath, avatar);
        reportImageAdapter.addData(reportPicVo);
        checkImgSelect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPhotoDialog != null) {
            mPhotoDialog.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void reportUser() {
        String option_id = mAdapter.getOptionId();
        String option_content = mAdapter.getOptionContent();
        String contents = mBinding.etReportDesc.getText().toString().trim();
        String img1 = reportImageAdapter.getImageOne();
        String img2 = reportImageAdapter.getImageTwo();
        String img3 = reportImageAdapter.getImageThree();
        if (TextUtils.isEmpty(img1)) {
            toastShort("请上传证据");
            return;
        }
        Map<String, String> reportOption = new HashMap<>();
        reportOption.put("user_id", uid);
        reportOption.put("option_id", option_id);
        reportOption.put("option_content", option_content);
        if (!TextUtils.isEmpty(contents)) {
            reportOption.put("contents", contents);
        }
        reportOption.put("img1", img1);
        reportOption.put("img2", img2);
        reportOption.put("img3", img3);
        reportOption.put("is_black", is_black);

        mViewModel.complain(reportOption)
                .observe(this, new BaseObserver<BaseApiResult>() {
                    @Override
                    public void onSuccess(BaseApiResult result) {
                        if (result != null) {
                            toastShort(result.getDesc());
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("roomId", roomId);
        outState.putString("uid", uid);
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

    protected LoadingDialog mLoadingDialog;

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
}
