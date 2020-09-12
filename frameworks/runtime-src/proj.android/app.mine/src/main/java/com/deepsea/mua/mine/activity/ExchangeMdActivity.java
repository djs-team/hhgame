package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.core.utils.UiUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityExchangeMdBinding;
import com.deepsea.mua.mine.dialog.ExchangeDialog;
import com.deepsea.mua.mine.viewmodel.ExchangeMdViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.ViewUtils;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/6
 * 兑换M豆
 */
@Route(path = ArouterConst.PAGE_ME_MINE_REDROSE)

public class ExchangeMdActivity extends BaseActivity<ActivityExchangeMdBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private ExchangeMdViewModel mViewModel;

    private WalletBean mWalletBean;

    private ExchangeDialog mDialog;
    boolean isRedpackage = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_md;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ExchangeMdViewModel.class);
        isRedpackage=getIntent().getBooleanExtra("isRedpackage",false);
        ViewUtils.setViewSize(mBinding.diamondLayout, 331, 120);
        addTextChangedListener();
        requestBalance();
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.consTitlebar.getRightTv(), o -> {
            Intent intent = new Intent(mContext, ExchangeMdDetailsActivity.class);
            intent.putExtra("isRedpackage",isRedpackage);
            startActivity(intent);
        });
        subscribeClick(mBinding.commitTv, o -> {
            String amount = mBinding.mdEdit.getText().toString();
            if (TextUtils.isEmpty(amount)) {
                toastShort("请输入余额数量");
                return;
            }
            if (amount.startsWith("0")) {
                toastShort("数量输入有误");
                return;
            }
//            if (mWalletBean != null && FormatUtils.moreThanZero(mWalletBean.getScale())) {
//                long coin = Long.parseLong(amount);
//                long scale = Long.parseLong(mWalletBean.getScale());
//                if (coin % scale != 0) {
//                    toastShort("数量输入有误");
//                    return;
//                }
//            }

            mViewModel.mdExchange(amount,isRedpackage).observe(this, new BaseObserver<BaseApiResult>() {
                @Override
                public void onSuccess(BaseApiResult result) {
                    showExchangedDialog(true);
                    if (result != null) {
                        setResult(Activity.RESULT_OK);
                        requestBalance();
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    showExchangedDialog(false);
                }
            });
        });
    }

    private void showExchangedDialog(boolean success) {
        if (mDialog == null) {
            mDialog = new ExchangeDialog(mContext);
            mDialog.setOnExchangedListener(new ExchangeDialog.OnExchangedListener() {
                @Override
                public void onExchanged() {
                    UiUtils.toggleSoftInput(mBinding.mdEdit);
                }

                @Override
                public void onBack() {
                    ArouterUtils.build(ArouterConst.PAGE_MAIN).withInt("index", 1).navigation();
                }
            });
        }
        mDialog.setStatus(success);
        mDialog.show();
    }

    private void addTextChangedListener() {
        mBinding.mdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mWalletBean != null) {
                    String scale = mWalletBean.getScale();
                    String coin = "0";
                    if (!TextUtils.isEmpty(s) && FormatUtils.isNumber(s.toString())) {
                        coin = s.toString();
                    }
                    if (FormatUtils.moreThanZero(scale)) {
//                        String result = FormatUtils.divideDown(coin, scale, 0);
                        String result = FormatUtils.multiply(coin, scale);
                        mBinding.coinTv.setText(result);
                    }
                }
            }
        });
    }

    private void requestBalance() {
        mViewModel.initExchange(isRedpackage).observe(this, new BaseObserver<WalletBean>() {
            @Override
            public void onSuccess(WalletBean result) {
                mWalletBean = result;
                if (result != null) {
                    mBinding.diamondTv.setText(mWalletBean.getDiamond() + "");
//                    mBinding.scaleTv.setText(String.format(Locale.CHINA, "%s钻石兑换1玫瑰，只能填%s的倍数", result.getScale(), result.getScale()));
                    mBinding.scaleTv.setText(String.format("1￥兑换" + result.getScale() + "玫瑰"));

                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        super.onDestroy();
    }
}
