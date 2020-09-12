package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityCrashWithdrawalBinding;
import com.deepsea.mua.mine.viewmodel.CashWithdrawalModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CashInfo;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.NumberUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

public class CrashWithdrawalActivity extends BaseActivity<ActivityCrashWithdrawalBinding> {
    @Inject
    ViewModelFactory mModelFactory;
    private CashWithdrawalModel mViewModel;

    private long cash;//最低提现余额
    private long cash_price;//提现手续费
    private double cash_tax;//提现税费
    private boolean isCanCash = false;
    private String alipayAcount = "";//支付宝账号
    boolean needRefresh = true;
    boolean isRedWithdrawal = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crash_withdrawal;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(CashWithdrawalModel.class);
        isRedWithdrawal = getIntent().getBooleanExtra("isRedWithdrawal", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needRefresh) {
            fetchCashInfo();
            needRefresh = false;
        }
    }


    @Override
    protected void initListener() {
        super.initListener();
        //提交
        subscribeClick(mBinding.tvSubmit, o -> {
            if (isCanCash) {
                fetchCash();
            } else {
                toastShort("请输入正确的提现金额");
            }

        });
        subscribeClick(mBinding.tvEditAccount, o -> {
            needRefresh = true;
            Intent intent = new Intent(mContext, CollectionAccountSettingActivity.class);
            startActivity(intent);
        });
        mBinding.etDrawingCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 2 + 1);
                        mBinding.etDrawingCount.setText(s);
                        mBinding.etDrawingCount.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().equals(".")) {
                    s = "0" + s;
                    mBinding.etDrawingCount.setText(s);
                    mBinding.etDrawingCount.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etDrawingCount.setText(s.subSequence(0, 1));
                        mBinding.etDrawingCount.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputString = mBinding.etDrawingCount.getText().toString();
                if (!TextUtils.isEmpty(inputString)) {

                    if (inputString.endsWith(".")) {
                        inputString = inputString + "0";
                    }
                    double inputCount = Double.valueOf(inputString);
                    if (inputCount >= cash && inputCount <= balance) {
                        isCanCash = true;
                        //输入的金额大于等于最低提现金额
                        double arrivalAccount = NumberUtils.calculateArrivalAccount(inputCount, cash_price, cash_tax);
                        ViewBindUtils.setText(mBinding.tvArrivalAccountCount, FormatUtils.subZeroAndDot(String.valueOf(arrivalAccount)));
                    } else if (inputCount > balance) {
                        isCanCash = false;
                        ViewBindUtils.setText(mBinding.etDrawingCount, FormatUtils.subZeroAndDot(String.valueOf(balance)));
                        mBinding.etDrawingCount.setSelection(mBinding.etDrawingCount.getText().toString().length());

                    } else {
                        isCanCash = false;
                    }
                } else {
                    ViewBindUtils.setText(mBinding.tvArrivalAccountCount, "");
                    isCanCash = false;
                }
            }
        });

    }

    private void fetchCash() {
        String cash = mBinding.etDrawingCount.getText().toString();
        String totalcash = mBinding.tvArrivalAccountCount.getText().toString();

        mViewModel.cash(alipayAcount, cash, totalcash, "1", isRedWithdrawal).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                if (result.getCode() == 200) {
                    toastShort("提交提现申请成功");
                    startActivity(new Intent(mContext, CrashWithDrawalDetailsActivity.class));
                    finish();
                } else {
                    toastShort(result);
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                toastShort(msg);
            }
        });
    }

    private double balance;

    private void fetchCashInfo() {
        mViewModel.fetchCashInfo(isRedWithdrawal).observe(this, new BaseObserver<CashInfo>() {
            @Override
            public void onSuccess(CashInfo result) {
                if (result != null) {
                    balance = result.getBalance();
                    ViewBindUtils.setText(mBinding.tvBalance, FormatUtils.subZeroAndDot(String.valueOf(balance)));
                    alipayAcount = result.getZfb();
                    ViewBindUtils.setText(mBinding.tvAlipayAccount, ("收款支付宝账号：" + alipayAcount));
                    if (!isRedWithdrawal) {
                        cash = result.getCash();
                        cash_price = result.getCash_price();
                        cash_tax = 0;
                    } else {
                        cash = result.getRedpack_cash();
                        cash_price = result.getRedpack_cash_price();
                        cash_tax = 0;
                    }
                    StringBuilder builder = new StringBuilder();
                    builder.append("1.单比最低￥");
                    builder.append(cash);
                    builder.append("可以提现\n2.提款金额需要首先扣除固定¥");
                    builder.append(cash_price);
                    builder.append("\n3.提现为实时到账，请注意查收支付宝账号信息，如遇到提\n现问题，及时联系客服。");


//                    builder.append("手续费\n• 扣除手续费的余额需要在扣除");
//                    builder.append((cash_tax * 100));
//                    builder.append("%的税费");
                    ViewBindUtils.setText(mBinding.tvCrashWithdrawalDesc, builder.toString());


                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }
}
