package com.deepsea.mua.mine.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.deepsea.mua.core.alipay.Alipay;
import com.deepsea.mua.core.alipay.PayResult;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.core.wxpay.WxPay;
import com.deepsea.mua.core.wxpay.WxpayBroadcast;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.ChargesDilalogAdapter;
import com.deepsea.mua.mine.databinding.ActivityRechargeDialogBinding;
import com.deepsea.mua.mine.dialog.ChargeTypeDialog;
import com.deepsea.mua.stub.dialog.RechargeFirstWelfareDialog;
import com.deepsea.mua.mine.viewmodel.RechargeViewModel;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.entity.ChargeBean;
import com.deepsea.mua.stub.entity.HaiPayBean;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.Const;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.DisposeUtils;
import com.deepsea.mua.stub.utils.GridItemDecoration;
import com.deepsea.mua.stub.utils.MobEventUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.jakewharton.rxbinding2.view.RxView;
import com.noober.background.BackgroundLibrary;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by JUN on 2019/4/16
 */
@Route(path = ArouterConst.PAGE_RECHARGE_DIALOG)
public class RechargeDialogActivity extends FragmentActivity
        implements HasSupportFragmentInjector {

    @Inject
    ViewModelFactory mModelFactory;
    private RechargeViewModel mViewModel;
    private WxPay mWxPay;

    private ChargesDilalogAdapter mAdapter;
    private ChargeTypeDialog mChargeDialog;

    @Autowired
    String balance;
    @Autowired
    boolean isWelfare;
    @Autowired
    String wf_rmb;
    @Autowired
    String wf_chargeid;
    boolean isFinishUi = false;
    //充值金额
    private String mRechargeAmount;
    Context mContext;
    protected ActivityRechargeDialogBinding mBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        mContext = this;
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(RechargeViewModel.class);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mBinding.balanceTv.setText(balance);
//        ViewUtils.setViewSize(mBinding.mdLayout, 331, 120);
        initRecyclerView();
        chargelist();
        initRechargeProtocol();

        if (TextUtils.isEmpty(balance)) {
            requestBalance();
        }
        initListener();
        if (isWelfare) {
            ViewBindUtils.setVisible(mBinding.mdLayout, false);
            ViewBindUtils.setVisible(mBinding.recyclerView, false);
        }
    }

    protected int getLayoutId() {
        return R.layout.activity_recharge_dialog;
    }


    private void initRechargeProtocol() {
        String protocol = "充值即代表同意 《合合有约充值协议》";
        SpannableString spannableString = new SpannableString(protocol);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#EA5BBB"));
            }

            @Override
            public void onClick(@NonNull View widget) {
                PageJumpUtils.jumpToWeb(AddressCenter.getAddress().getRechargeProtocol());
            }
        }, 7, 17, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.tvRechargeAgreement.setHighlightColor(Color.TRANSPARENT);
        mBinding.tvRechargeAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvRechargeAgreement.setText(spannableString);
    }

    private int payTypeFlag = -1;

    protected void initListener() {
        subscribeClick(mBinding.rechargeTv, o -> {
            if (payTypeFlag == -1) {
                toastShort("请选择充值方式");
            } else if (payTypeFlag == 1) {
                alipay();
            } else if (payTypeFlag == 2) {
                wxpay();
            }
        });
        subscribeClick(mBinding.ivPayWx, o -> {
            mBinding.ivPayWx.setSelected(true);
            mBinding.ivPayAlipay.setSelected(false);
            payTypeFlag = 2;
        });
        subscribeClick(mBinding.ivPayAlipay, o -> {
            payTypeFlag = 1;
            mBinding.ivPayWx.setSelected(false);
            mBinding.ivPayAlipay.setSelected(true);
        });
        subscribeClick(mBinding.rlGroup, o -> {
            finish();
        });
        subscribeClick(mBinding.llBottom, o -> {

        });
    }

    /**
     * 使用正则表达式提取中括号中的内容
     *
     * @param msg
     * @return
     */
    public List<String> extractMessageByRegular(String msg) {

        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
        Matcher m = p.matcher(msg);
        while (m.find()) {
            list.add(m.group().substring(1, m.group().length() - 1));
        }
        return list;
    }

    List<String> payTypes;

    private void initRecyclerView() {
        mAdapter = new ChargesDilalogAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {

            mAdapter.setSelectPos(position);
            mBinding.rechargeTv.setText("充值¥" + mAdapter.getChargeAmount());
            String payType = mAdapter.getPayType();
            payTypes = extractMessageByRegular(payType);
            setPaytypeParam();
        });
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mBinding.recyclerView.addItemDecoration(new GridItemDecoration(3, ResUtils.dp2px(mContext, 11)));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void setPaytypeParam() {
        if (payTypes != null) {
            if (payTypes.size() == 2) {
                ViewBindUtils.setVisible(mBinding.ivPayAlipay, true);
                ViewBindUtils.setVisible(mBinding.ivPayWx, true);
                mBinding.ivPayWx.setSelected(true);
                payTypeFlag = 2;
            } else if (payTypes.size() == 1) {
                if (payTypes.get(0).equals("1")) {
                    ViewBindUtils.setVisible(mBinding.ivPayAlipay, true);
                    ViewBindUtils.setVisible(mBinding.ivPayWx, false);
                    mBinding.ivPayAlipay.setSelected(true);
                    payTypeFlag = 1;
                } else if (payTypes.get(0).equals("2")) {
                    ViewBindUtils.setVisible(mBinding.ivPayAlipay, false);
                    ViewBindUtils.setVisible(mBinding.ivPayWx, true);
                    mBinding.ivPayWx.setSelected(true);
                    payTypeFlag = 2;
                }
            }
        } else {
            payTypeFlag = -1;
            ViewBindUtils.setVisible(mBinding.ivPayAlipay, false);
            ViewBindUtils.setVisible(mBinding.ivPayWx, false);
        }
    }


    private void requestBalance() {
        mViewModel.wallet().observe(this, new BaseObserver<WalletBean>() {
            @Override
            public void onSuccess(WalletBean result) {
                if (result != null) {
                    balance = String.valueOf(result.getCoin());
                    mBinding.balanceTv.setText(String.valueOf(result.getCoin()));
                    if (isFinishUi) {
                        finish();
                    }
                }
            }
        });
    }

    private void chargelist() {
        mViewModel.chargelist().observe(this, new BaseObserver<ChargeBean>() {
            @Override
            public void onSuccess(ChargeBean result) {
                ViewBindUtils.setVisible(mBinding.tvRechargeDesc, result.getIs_first() == 1 && TextUtils.equals(UserUtils.getUser().getSex(), "1"));
                ViewBindUtils.setVisible(mBinding.ivFirstWelfare, result.getIs_first() == 1);
                mAdapter.setIs_first(result.getIs_first());
                mAdapter.setNewData(result.getCharge_list());
                mBinding.rechargeTv.setText("充值¥" + mAdapter.getChargeAmount());
                String payType = mAdapter.getPayType();
                payTypes = extractMessageByRegular(payType);
                setPaytypeParam();
            }
        });
    }

    private void showRechargeDialog() {
        if (mChargeDialog == null) {
            mChargeDialog = new ChargeTypeDialog(mContext);
            mChargeDialog.setPayTypes(payTypes);
            mChargeDialog.setOnChargeTypeListener(new ChargeTypeDialog.OnChargeTypeListener() {
                @Override
                public void onAliPay() {
                    alipay();
                }

                @Override
                public void onWxPay() {
                    wxpay();
                }

                @Override
                public void onHaibeiPay() {
                    haibeiPay();
                }
            });
        }
        mChargeDialog.showAtBottom();
    }

    private void haibeiPay() {
        String chargeId = "";
        if (isWelfare) {
            mRechargeAmount = wf_rmb;
            chargeId = wf_chargeid;
        } else {
            mRechargeAmount = mAdapter.getChargeAmount();
            chargeId = mAdapter.getChargeId();
        }
        mViewModel.haipay(mRechargeAmount, chargeId)
                .observe(this, new ProgressObserver<HaiPayBean>(mContext) {
                    @Override
                    public void onSuccess(HaiPayBean result) {
                        if (result != null) {
                            isNeedRequestBanlance = true;
                            PageJumpUtils.jumpToWeb("微信支付", result.getPayUrl());

                        }
                    }
                });
    }

    boolean isNeedRequestBanlance = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedRequestBanlance) {
            requestBalance();
        }
    }

    private void wxpay() {
        String chargeId = "";
        if (isWelfare) {
            mRechargeAmount = wf_rmb;
            chargeId = wf_chargeid;
        } else {
            mRechargeAmount = mAdapter.getChargeAmount();
            chargeId = mAdapter.getChargeId();
        }
        mViewModel.wxpay(mRechargeAmount, chargeId)
                .observe(this, new ProgressObserver<WxOrder>(mContext) {
                    @Override
                    public void onSuccess(WxOrder result) {
                        if (result != null) {
                            WxPay.WXPayBuilder builder = new WxPay.WXPayBuilder();
                            builder.setAppId(result.getAppid());
                            builder.setNonceStr(result.getNoncestr());
                            builder.setPackageValue(result.getPackageX());
                            builder.setPartnerId(result.getPartnerid());
                            builder.setPrepayId(result.getPrepayid());
                            builder.setSign(result.getSign());
                            builder.setTimeStamp(result.getTimestamp());
                            mWxPay = builder.build();
                            mWxPay.startPay(mContext);
                            mWxPay.registerWxpayResult(mWxpayReceiver);
                        }
                    }
                });
    }

    private void alipay() {
        String chargeId = "";
        if (isWelfare) {
            mRechargeAmount = wf_rmb;
            chargeId = wf_chargeid;
        } else {
            mRechargeAmount = mAdapter.getChargeAmount();
            chargeId = mAdapter.getChargeId();
        }
        mViewModel.alipay(mRechargeAmount, chargeId)
                .observe(this, new ProgressObserver<String>(mContext) {
                    @Override
                    public void onSuccess(String result) {
                        RxPermissions permissions = new RxPermissions(RechargeDialogActivity.this);
                        permissions.request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .as(autoDisposable())
                                .subscribe(aBoolean -> {
                                    if (aBoolean) {
                                        onStartAlipay(result);
                                    }
                                });
                    }
                });
    }

    private void onStartAlipay(String signature) {
        Alipay alipay = new Alipay(this);
        alipay.startPay(signature);
        alipay.setAlipayListener(new Alipay.AlipayListener() {
            @Override
            public void onSuccess(PayResult result) {
                isFinishUi = true;
                MobEventUtils.onRechargeEvent(mContext, mRechargeAmount);
                mRechargeAmount = "0";
                chargelist();
                requestBalance();

            }

            @Override
            public void onError(String msg) {
                toastShort(msg);
            }
        });
    }

    private WxpayBroadcast.WxpayReceiver mWxpayReceiver = new WxpayBroadcast.WxpayReceiver() {
        @Override
        public void onSuccess() {
            unregisterWxpayResult();
            MobEventUtils.onRechargeEvent(mContext, mRechargeAmount);
            mRechargeAmount = "0";
            isFinishUi = true;
            chargelist();
            requestBalance();
        }

        @Override
        public void onError() {
            unregisterWxpayResult();
        }
    };

    private void unregisterWxpayResult() {
        if (mWxPay != null) {
            mWxPay.unregisterWxpayResult(mWxpayReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterWxpayResult();
    }

    @Override
    public void finish() {
        if (!TextUtils.isEmpty(balance)) {
            Intent intent = getIntent();
            intent.putExtra(Constant.BALANCE, balance);
            intent.putExtra(Constant.RECHARGE_WELFARE, true);
            intent.putExtra(Constant.RECHARGE_SUCCESS, isFinishUi);
            setResult(RESULT_OK, intent);
        }
        super.finish();
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
