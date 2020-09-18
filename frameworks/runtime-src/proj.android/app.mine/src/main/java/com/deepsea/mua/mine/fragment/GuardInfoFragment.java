package com.deepsea.mua.mine.fragment;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.core.alipay.Alipay;
import com.deepsea.mua.core.alipay.PayResult;
import com.deepsea.mua.core.wxpay.WxPay;
import com.deepsea.mua.core.wxpay.WxpayBroadcast;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.GuardMineAdapter;
import com.deepsea.mua.mine.databinding.FragmentGuardMineBinding;
import com.deepsea.mua.mine.dialog.GuardRenewCloseDialog;
import com.deepsea.mua.stub.dialog.GuardRenewDialog;
import com.deepsea.mua.mine.dialog.GuardRenewOpenDialog;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.entity.GuardInfoBean;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/3/22
 */
public class GuardInfoFragment extends BaseFragment<FragmentGuardMineBinding> {
    @Inject
    ViewModelFactory mModelFactory;
    private ProfileViewModel mViewModel;
    private GuardMineAdapter mAdapter;
    private String type = "1";//1:我守护的人    2:守护我的人
    private WxPay mWxPay;

    public static BaseFragment newInstance(String type) {
        GuardInfoFragment instance = new GuardInfoFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("type", type);
            instance.setArguments(bundle);
        } else {
            bundle.putString("type", type);
        }
        return instance;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guard_mine;
    }

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileViewModel.class);
        type = mBundle.getString("type");
        initRecyclerView();
        initRefreshLayout();

    }


    private void initRecyclerView() {
        mAdapter = new GuardMineAdapter(mContext, type);
        mAdapter.setmListener(new GuardMineAdapter.OnMyClickListener() {
            @Override
            public void renew(String target_id) {
                fetchRenew(target_id);
            }

            @Override
            public void autoRenew(String is_auto) {
                if (is_auto.equals("1")) {
                    renewClose();
                } else {
                    renewOpen();
                }
            }
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.guardInfoRefresh(type).observe(this, new BaseObserver<GuardInfoBean>() {
                @Override
                public void onSuccess(GuardInfoBean result) {
                    if (result != null) {
                        GuardInfoBean.PageInfoBean pageInfo = result.getPageInfo();
                        mAdapter.setNewData(result.getGuard_memberlist());
                        mBinding.refreshLayout.finishRefresh();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    toastShort(msg);
                    mBinding.refreshLayout.finishRefresh();
                }
            });
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mViewModel.guardInfoLoadMore(type).observe(this, new BaseObserver<GuardInfoBean>() {
                @Override
                public void onSuccess(GuardInfoBean result) {
                    if (result != null) {
                        GuardInfoBean.PageInfoBean pageInfo = result.getPageInfo();
                        mAdapter.addData(result.getGuard_memberlist());
                        mBinding.refreshLayout.finishLoadMore();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    toastShort(msg);
                    mBinding.refreshLayout.finishLoadMore();
                    mViewModel.page--;
                }
            });
        });
        mBinding.refreshLayout.autoRefresh();
    }

    //关闭自动续费
    private void renewClose() {
        GuardRenewCloseDialog renewCloseDialog = new GuardRenewCloseDialog(mContext);
        renewCloseDialog.setOnClickListener(new GuardRenewCloseDialog.OnClickListener() {
            @Override
            public void onClickOk() {

            }
        });
        renewCloseDialog.show();
    }

    //开启自动续费
    private void renewOpen() {
        GuardRenewOpenDialog renewCloseDialog = new GuardRenewOpenDialog(mContext);
        renewCloseDialog.setOnClickListener(new GuardRenewOpenDialog.OnClickListener() {
            @Override
            public void onClickOk() {

            }
        });
        renewCloseDialog.show();
    }

    //续费
    private void fetchRenew(String target_id) {
        mViewModel.initGuard(target_id).observe(this, new BaseObserver<RenewInitVo>() {
            @Override
            public void onSuccess(RenewInitVo result) {
                GuardRenewDialog renewCloseDialog = new GuardRenewDialog(mContext);
                renewCloseDialog.setData(result);
                renewCloseDialog.setOnClickListener(new GuardRenewDialog.OnClickListener() {
                    @Override
                    public void onClickOk(int payType, String coin, String chargeId, String guard_id, String long_day) {
                        if (payType == 0) {
                            wxpay(coin, chargeId, target_id, guard_id, long_day);
                        } else {
                            alipay(coin, chargeId, target_id, guard_id, long_day);
                        }
                    }
                });
                renewCloseDialog.show();
            }
        });

    }

    private void alipay(String coin, String chargeId, String target_id, String guard_id, String long_day) {
        mViewModel.alipay(coin, Constant.CHARGE_ACT_X,chargeId, target_id, guard_id, long_day)
                .observe(this, new ProgressObserver<String>(mContext) {
                    @Override
                    public void onSuccess(String result) {
                        RxPermissions permissions = new RxPermissions(getActivity());
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
        Alipay alipay = new Alipay(getActivity());
        alipay.startPay(signature);
        alipay.setAlipayListener(new Alipay.AlipayListener() {
            @Override
            public void onSuccess(PayResult result) {
                mBinding.refreshLayout.autoRefresh();
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
//            requestBalance();
//            MobEventUtils.onRechargeEvent(mContext, mRechargeAmount);
//            mRechargeAmount = "0";
            mBinding.refreshLayout.autoRefresh();
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
    public void onDestroy() {
        super.onDestroy();
        unregisterWxpayResult();

    }

    private void wxpay(String coin, String chargeId, String target_id, String guard_id, String long_day) {

        mViewModel.wxpay(coin, Constant.CHARGE_ACT_X,chargeId, target_id, guard_id, long_day)
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
}
