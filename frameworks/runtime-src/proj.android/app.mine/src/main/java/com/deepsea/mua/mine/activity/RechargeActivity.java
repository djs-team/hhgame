package com.deepsea.mua.mine.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import com.deepsea.mua.core.alipay.Alipay;
import com.deepsea.mua.core.alipay.PayResult;
import com.deepsea.mua.core.wxpay.WxPay;
import com.deepsea.mua.core.wxpay.WxpayBroadcast;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.ChargesAdapter;
import com.deepsea.mua.mine.databinding.ActivityRechargeBinding;
import com.deepsea.mua.mine.dialog.ChargeTypeDialog;
import com.deepsea.mua.mine.viewmodel.RechargeViewModel;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.entity.ChargeBean;
import com.deepsea.mua.stub.entity.HaiPayBean;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.GridItemDecoration;
import com.deepsea.mua.stub.utils.MobEventUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.StringUtils;
import com.deepsea.mua.stub.utils.UIUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.ViewUtils;
import com.sdk.base.api.ToolUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/16
 */
@Route(path = ArouterConst.PAGE_RECHARGE)
public class RechargeActivity extends BaseActivity<ActivityRechargeBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private RechargeViewModel mViewModel;
    private WxPay mWxPay;

    private ChargesAdapter mAdapter;
    private ChargeTypeDialog mChargeDialog;

    @Autowired
    String balance;

    //充值金额
    private String mRechargeAmount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(RechargeViewModel.class);
        mBinding.balanceTv.setText(balance);
//        ViewUtils.setViewSize(mBinding.mdLayout, 331, 120);
        initRecyclerView();
        chargelist();
        initRechargeProtocol();

        if (TextUtils.isEmpty(balance)) {
            requestBalance();
        }
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
                PageJumpUtils.jumpToWeb(AddressCenter.getAddress().getRegisterProtocol());
            }
        }, 7, 17, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.tvRechargeAgreement.setHighlightColor(Color.TRANSPARENT);
        mBinding.tvRechargeAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvRechargeAgreement.setText(spannableString);
    }

    private int payTypeFlag = -1;

    @Override
    protected void initListener() {
        subscribeClick(mBinding.titleBar.getRightTv(), o -> {
            startActivity(new Intent(mContext, WalletRecordActivity.class));
        });
//        subscribeClick(mBinding.protocolTv, o -> {
//        });
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
    }


    List<String> payTypes;
    private int is_first;//是否首充1:是   2:不是

    private void initRecyclerView() {
        mAdapter = new ChargesAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            mAdapter.setSelectPos(position);
            mBinding.rechargeTv.setText("充值¥" + mAdapter.getChargeAmount());
            String payType = mAdapter.getPayType();
            payTypes = StringUtils.extractMessageByRegular(payType);
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
                }
            }
        });
    }

    private void chargelist() {
        mViewModel.chargelist().observe(this, new BaseObserver<ChargeBean>() {
            @Override
            public void onSuccess(ChargeBean result) {
                ViewBindUtils.setVisible(mBinding.tvRechargeDesc, result.getIs_first() == 1 && TextUtils.equals(UserUtils.getUser().getSex(), "1"));
                mAdapter.setIs_first(result.getIs_first());
                mAdapter.setNewData(result.getCharge_list());
                mBinding.rechargeTv.setText("充值¥" + mAdapter.getChargeAmount());
                String payType = mAdapter.getPayType();
                payTypes = StringUtils.extractMessageByRegular(payType);
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
        mRechargeAmount = mAdapter.getChargeAmount();
        String chargeId = mAdapter.getChargeId();
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
        mRechargeAmount = mAdapter.getChargeAmount();
        String chargeId = mAdapter.getChargeId();
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
        mRechargeAmount = mAdapter.getChargeAmount();
        String chargeId = mAdapter.getChargeId();
        mViewModel.alipay(mRechargeAmount, chargeId)
                .observe(this, new ProgressObserver<String>(mContext) {
                    @Override
                    public void onSuccess(String result) {
                        RxPermissions permissions = new RxPermissions(RechargeActivity.this);
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
                chargelist();
                requestBalance();
                MobEventUtils.onRechargeEvent(mContext, mRechargeAmount);
                mRechargeAmount = "0";
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
            chargelist();
            requestBalance();
            MobEventUtils.onRechargeEvent(mContext, mRechargeAmount);
            mRechargeAmount = "0";
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
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }
}
