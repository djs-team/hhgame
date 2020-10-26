package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityWalletBinding;
import com.deepsea.mua.mine.viewmodel.WalletViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.InitCash;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 * 我的钱包
 */
@Route(path = ArouterConst.PAGE_ME_WALLET)

public class WalletActivity extends BaseActivity<ActivityWalletBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private WalletViewModel mViewModel;
    private String balance;
    private boolean needRefresh = true;
    private final int requestCode_receive_account = 1001;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(WalletViewModel.class);
        initViewParams();
        mBinding.mdRechargeTv.setEnabled(false);
        mBinding.diamondRechargeTv.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == requestCode_receive_account) {
                requestBalance();
            }
        }
    }

    private void initViewParams() {
//        ViewUtils.setViewSize(mBinding.mdLayout, 331, 120);
//        ViewUtils.setViewSize(mBinding.diamondLayout, 331, 120);
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.mdRechargeTv, o -> {
            needRefresh = true;
            checkParentLock();
        });
        subscribeClick(mBinding.diamondRechargeTv, o -> {
            needRefresh = true;
            Intent intent = new Intent(mContext, ExchangeMdActivity.class);
            intent.putExtra("isRedpackage", false);
            startActivity(intent);
        });
        //收入明细
        subscribeClick(mBinding.tvIncomeDetails, o -> {
            needRefresh = true;
            Intent intent = new Intent(mContext, IncomeDetailsActivity.class);
            startActivity(intent);
        });
        //提现明细
        subscribeClick(mBinding.tvCrashWithdrawalDetails, o -> {
            needRefresh = true;
            Intent intent = new Intent(mContext, CrashWithDrawalDetailsActivity.class);
            intent.putExtra("isRedpackage", false);

            startActivity(intent);
        });
        //提现
        subscribeClick(mBinding.tvCrashWithdrawal, o -> {
            needRefresh = true;
            initCash(false);
        });
        //收款账号
        subscribeClick(mBinding.tvReceivingAccount, o -> {
            needRefresh = true;
            Intent intent = new Intent(mContext, CollectionAccountSettingActivity.class);
            startActivityForResult(intent, requestCode_receive_account);
        });
        //红包兑换
        subscribeClick(mBinding.redpakageRechargeTv, o -> {
            needRefresh = true;
            Intent intent = new Intent(mContext, ExchangeMdActivity.class);
            intent.putExtra("isRedpackage", true);
            startActivity(intent);
        });
        //红包收入明细
        subscribeClick(mBinding.tvRedpakageIncomeDetails, o -> {
            needRefresh = true;
            Intent intent = new Intent(mContext, IncomeRedpackageDetailsActivity.class);
            startActivity(intent);
        });
        //红包提现明细
        subscribeClick(mBinding.tvRedpakageCrashWithdrawalDetails, o -> {
            needRefresh = true;
            Intent intent = new Intent(mContext, CrashWithDrawalDetailsActivity.class);
            intent.putExtra("isRedpackage", true);
            startActivity(intent);
        });
        //红包提现
        subscribeClick(mBinding.tvRedpakageCrashWithdrawal, o -> {
            needRefresh = true;
            initCash(true);
        });
        //红包收款账号
        subscribeClick(mBinding.tvRedpakageReceivingAccount, o -> {
            needRefresh = true;
            Intent intent = new Intent(mContext, CollectionAccountSettingActivity.class);
            startActivityForResult(intent, requestCode_receive_account);
        });


    }

    private void initCash(boolean isRedpackage) {
        mViewModel.initCash().observe(this, new BaseObserver<InitCash>() {
            @Override
            public void onSuccess(InitCash result) {
                if (result.getBindstatus() == 1) {
                    if (result.getIs_edit() == 1) {
                        toastShort("账号正在审核中，请耐心等待");
                        return;
                    }
                    if (result.getIs_check() == 1) {
                        toastShort("您有一笔金额正在体现审核，请耐心等待");
                        return;
                    }
                    //已经绑定alipay
                    Intent intent = new Intent(mContext, CrashWithdrawalActivity.class);
                    intent.putExtra("isRedWithdrawal", isRedpackage);
                    startActivity(intent);
                } else {
                    //没有绑定alipay
//                    Intent intent = new Intent(mContext, CollectionAccountSettingActivity.class);
//                    startActivity(intent);

                    toastShort("请先设置收款账号");
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                toastShort(msg);
            }
        });

    }

    private void initRedCash() {
        mViewModel.initCash().observe(this, new BaseObserver<InitCash>() {
            @Override
            public void onSuccess(InitCash result) {
                if (result.getBindstatus() == 1) {
                    if (result.getIs_edit() == 1) {
                        toastShort("账号正在审核中，请耐心等待");
                        return;
                    }
                    if (result.getIs_check() == 1) {
                        toastShort("您有一笔金额正在体现审核，请耐心等待");
                        return;
                    }
                    //已经绑定alipay
                    Intent intent = new Intent(mContext, CrashWithdrawalActivity.class);
                    startActivity(intent);
                } else {
                    //没有绑定alipay
//                    Intent intent = new Intent(mContext, CollectionAccountSettingActivity.class);
//                    startActivity(intent);

                    toastShort("请先设置收款账号");
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                toastShort(msg);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needRefresh) {
            requestBalance();
            needRefresh = false;
        }
    }

    private void requestBalance() {
        mViewModel.wallet().observe(this, new BaseObserver<WalletBean>() {
            @Override
            public void onSuccess(WalletBean result) {
                if (result != null) {
                    balance = String.valueOf(result.getCoin());
                    mBinding.balanceMdTv.setText(String.valueOf(result.getCoin()));
                    mBinding.diamondTv.setText(FormatUtils.formatNumberScale(result.getDiamond(), 2));
                    mBinding.tvIncomeToday.setText(String.format("今日：￥%s", FormatUtils.formatNumberScale(result.getList_today_price(), 2)));
                    mBinding.tvIncomeYesterday.setText(String.format("昨日：￥%s", FormatUtils.formatNumberScale(result.getList_yes_price(), 2)));
                    mBinding.tvIncomeWeek.setText(String.format("本周：￥%s", FormatUtils.formatNumberScale(result.getList_week_price(), 2)));
                    mBinding.tvIncomeMonth.setText(String.format("本月：￥%s", FormatUtils.formatNumberScale(result.getList_month_price(), 2)));
                    mBinding.redpakageTv.setText(FormatUtils.formatNumberScale(result.getRedpacket_coin(), 2));
                    ViewBindUtils.setText(mBinding.tvRedpakageIncomeToday, (String.format("今日：￥%s", result.getRed_list_today_price())));
                    ViewBindUtils.setText(mBinding.tvRedpakageIncomeYesterday, (String.format("昨日：￥%s", result.getRed_list_yes_price())));
                    ViewBindUtils.setText(mBinding.tvRedpakageIncomeWeek, (String.format("本周：￥%s", result.getRed_list_week_price())));
                    ViewBindUtils.setText(mBinding.tvRedpakageIncomeMonth, (String.format("本月：￥%s", result.getRed_list_month_price())));


                    mBinding.mdRechargeTv.setEnabled(true);
                    mBinding.diamondRechargeTv.setEnabled(true);
                    int type = result.getType();
                    if (type == 1) {
                        mBinding.diamondLayout.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.diamondLayout.setVisibility(View.GONE);
                    }
                    int exchange_switch = result.getExchange_switch();
//                        是否显示兑换1:显示 2:不显示
                    ViewBindUtils.setVisible(mBinding.diamondRechargeTv, exchange_switch == 1);
                    int is_cash = result.getIs_cash();
                    //是否显示提现1：显示 2:不显示
                    if (is_cash == 1) {
                        ViewBindUtils.setVisible(mBinding.tvCrashWithdrawalDetails, true);
                        ViewBindUtils.setVisible(mBinding.tvCrashWithdrawal, true);
                        ViewBindUtils.setVisible(mBinding.tvReceivingAccount, true);
                    } else {
                        ViewBindUtils.setVisible(mBinding.tvCrashWithdrawalDetails, false);
                        ViewBindUtils.setVisible(mBinding.tvCrashWithdrawal, false);
                        ViewBindUtils.setVisible(mBinding.tvReceivingAccount, false);

                    }
//                    double check = result.getCheck();
//                    if (check != 0&&is_cash==1) {
//                        ViewBindUtils.setVisible(mBinding.tvUnderReview, true);
//                        ViewBindUtils.setText(mBinding.tvUnderReview, "正在审核提现￥" + check);
//                    }else{
//                        ViewBindUtils.setVisible(mBinding.tvUnderReview, false);
//                    }
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                mBinding.mdRechargeTv.setEnabled(true);
                mBinding.diamondRechargeTv.setEnabled(true);
            }
        });
    }

    private void checkParentLock() {
        showProgress();
        mViewModel.checkSatus().observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                hideProgress();
                PageJumpUtils.jumpToRecharge(String.valueOf(balance));
            }

            @Override
            public void onError(String msg, int code) {
                hideProgress();
                if (code == 204 || code == 205) {
                    toastShort(msg);
                } else {
                    super.onError(msg, code);
                }
            }
        });
    }


}
