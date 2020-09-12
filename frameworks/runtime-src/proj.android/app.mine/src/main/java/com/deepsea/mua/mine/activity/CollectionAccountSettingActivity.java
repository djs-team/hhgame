package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.CollectionAccountSettingBinding;
import com.deepsea.mua.mine.viewmodel.CollectionAccountModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.PhotoDialog;
import com.deepsea.mua.stub.entity.CashInfo;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.utils.OssUpUtil;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * 收款账户设置
 */
public class CollectionAccountSettingActivity extends BaseActivity<CollectionAccountSettingBinding> {
    @Inject
    ViewModelFactory mModelFactory;
    private CollectionAccountModel mViewModel;
    private String status = "";
    @Inject
    AppExecutors mExecutors;

    @Override
    protected int getLayoutId() {
        return R.layout.collection_account_setting;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(CollectionAccountModel.class);
        fetchCashInfo();
    }

    private void fetchCashInfo() {
        mViewModel.fetchCashInfo().observe(this, new BaseObserver<CashInfo>() {
            @Override
            public void onSuccess(CashInfo result) {
                if (result != null) {
                    status = result.getStatus();
                    if (status.equals("0")) {//已经绑定
                        mBinding.etAlipayAcount.setText(result.getZfb());
                        mBinding.etAlipayAcount.setEnabled(false);
                        mBinding.tvSubmit.setText("修改");
                    } else if (status.equals("1")) {//未绑定
                        mBinding.etAlipayAcount.setEnabled(true);
                        mBinding.tvSubmit.setText("提交");
                    } else if (status.equals("2")) {
                        mBinding.etAlipayAcount.setText(result.getZfb());
                        mBinding.etAlipayAcount.setEnabled(false);
                        mBinding.tvSubmit.setText("审核中");
                        mBinding.tvSubmit.setEnabled(false);
                    }
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        //提交
        subscribeClick(mBinding.tvSubmit, o -> {
            if (status.equals("0")) {
                status = "1";
                mBinding.etAlipayAcount.setEnabled(true);
                mBinding.etAlipayAcount.setSelection(mBinding.etAlipayAcount.getText().toString().length());
                mBinding.etAlipayAcount.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                mBinding.tvSubmit.setText("提交");
                return;
            }
            //绑定支付宝
            String alipayAccount = mBinding.etAlipayAcount.getText().toString().trim();
            if (TextUtils.isEmpty(alipayAccount)) {
                toastShort("请输入支付宝账号");
                return;
            }
            if (TextUtils.isEmpty(identity_front)) {
                toastShort("请设置支付宝正面照");
                return;
            }
            if (TextUtils.isEmpty(identity_back)) {
                toastShort("请设置支付宝背面");
                return;
            }

            bindAlipay(alipayAccount);
        });
        subscribeClick(mBinding.ivIdentityFront, o -> {
            position = 0;
            showPhotoDialog();
        });
        subscribeClick(mBinding.ivIdentityBack, o -> {
            position = 1;
            showPhotoDialog();
        });
    }

    private String identity_front;
    private String identity_back;
    PhotoDialog mPhotoDialog = null;
    private int position;

    private void showPhotoDialog() {
        if (mPhotoDialog == null) {
            mPhotoDialog = new PhotoDialog();
            mPhotoDialog.setOnPhotoSelectedListener(path -> {
                Log.d(TAG, "showPhotoDialog: " + path);
                showProgress();
                mViewModel.getOssConfig().observe(this, new BaseObserver<OSSConfigBean>() {
                    @Override
                    public void onSuccess(OSSConfigBean result) {
                        if (result != null) {
                            upLoadHeadIv(position, result, path);
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

    private void upLoadHeadIv(int position, OSSConfigBean config, String aHeadIv) {
        mExecutors.diskIO().execute(() -> {
            OSS oSs = OssUpUtil.getInstance().getOssConfig(this, config.AccessKeyId, config.AccessKeySecret, config.SecurityToken, config.Expiration);
            ossAsyncTask = OssUpUtil.getInstance().upToOss(4, aHeadIv, oSs, config.BucketName, new OssUpUtil.OssUpCallback() {
                @Override
                public void upSuccessFile(String objectKey) {
                    Log.d(TAG, "upSuccessFile: " + objectKey);
                    if (position == 0) {
                        identity_front = objectKey;
                    } else {
                        identity_back = objectKey;
                    }
                    runOnUiThread(() -> {
                        ImageView identityImg = null;
                        if (position == 0) {
                            identityImg = mBinding.ivIdentityFront;
                        } else {
                            identityImg = mBinding.ivIdentityBack;
                        }
                        GlideUtils.loadImage(identityImg, aHeadIv, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
                        hideProgress();
//                        toggleRegisterStatus();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mPhotoDialog != null) {
            mPhotoDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void bindAlipay(String alipayAccount) {
        mViewModel.bindaplipay(alipayAccount, "1", identity_front, identity_back).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                if (result.getCode() == 200) {
                    //绑定成功
                    ToastUtils.showToast("申请已提交");
                    CollectionAccountSettingActivity.this.finish();
                    setResult(Activity.RESULT_OK);

                } else {
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
}
