package com.deepsea.mua.mine.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.core.alipay.Alipay;
import com.deepsea.mua.core.alipay.PayResult;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.wxpay.WxPay;
import com.deepsea.mua.core.wxpay.WxpayBroadcast;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.GuardAdapter;
import com.deepsea.mua.mine.databinding.ActivityGuardListBinding;
import com.deepsea.mua.mine.dialog.GuardPrivilegeDialog;
import com.deepsea.mua.stub.dialog.GuardRenewDialog;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.adapter.RecyclerAdapterWithHF;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.databinding.ItemGuardListTopBinding;
import com.deepsea.mua.stub.entity.GuardResultBean;
import com.deepsea.mua.stub.entity.LookGuardUserVo;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.deepsea.mua.stub.utils.Constant.CHARGE_ACT_J;
import static com.deepsea.mua.stub.utils.Constant.CHARGE_ACT_X;

/**
 * @zu 守护榜
 */
@Route(path = ArouterConst.PAGE_ME_MINE_GUARD)

public class GuardActivity extends BaseActivity<ActivityGuardListBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private ProfileViewModel mViewModel;
    private GuardAdapter mAdapter;
    @Autowired
    String userid;

    private int is_guard = 2;//1 yes 2 no


    @Override
    protected int getLayoutId() {
        return R.layout.activity_guard_list;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileViewModel.class);
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.titleBar.getRightTv(), o -> {
            showRightRule();
        });
        subscribeClick(mBinding.tvRenew, o -> {
            String is_active = "";
            if (is_guard == 1) {
                //是守护 --去续费
                is_active = CHARGE_ACT_X;
            } else {
                //不是守护--立即守护
                is_active = CHARGE_ACT_J;
            }
            fetchRenew(is_active, userid);

        });
    }

    GuardPrivilegeDialog dialog = null;

    private void showRightRule() {
//        startActivity(new Intent(mContext, GuardRuleActivity.class));
        if (dialog == null) {
            dialog = new GuardPrivilegeDialog(mContext);
        }
        dialog.show();

    }

    RecyclerAdapterWithHF mAdapterWithHF;

    private void initRecyclerView() {
        mAdapter = new GuardAdapter(mContext,userid);
        mBinding.rvGuard.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        mAdapterWithHF.setManagerType(RecyclerAdapterWithHF.TYPE_MANAGER_LINEAR);
        mAdapterWithHF.setRecycleView(mBinding.rvGuard);
        mBinding.rvGuard.setAdapter(mAdapterWithHF);
        addHeader();
    }


    private void initRefreshLayout() {
        mBinding.refreshLayout.setMaterialHeader();
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.guardRefresh(userid).observe(this, new BaseObserver<LookGuardUserVo>() {
                @Override
                public void onSuccess(LookGuardUserVo result) {
                    if (result != null) {
                        is_guard = result.getIs_guard();
                        ViewBindUtils.setText(mBinding.tvRenew, is_guard == 1 ? "续费" : "立即守护");
                        LookGuardUserVo.UserInfoBean userInfoBean = result.getUser_info();
                        ViewBindUtils.setVisible(mBinding.tvRenew, !TextUtils.equals(userInfoBean.getId(), UserUtils.getUser().getUid()));
                        LookGuardUserVo.PageInfoBean pageInfo = result.getPageInfo();
                        int topSize = setTopRanks(result.getGuard_memberlist());
                        if (result.getGuard_memberlist() != null && result.getGuard_memberlist().size() > topSize) {
                            mAdapter.setNewData(result.getGuard_memberlist().subList(topSize, result.getGuard_memberlist().size()));
                        } else {
                            mAdapter.setNewData(null);
                        }
                        mBinding.refreshLayout.finishRefresh();
                        mBinding.refreshLayout.setEnableLoadMore(Integer.valueOf(pageInfo.getPage()) < pageInfo.getTotalPage());
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
            mViewModel.guardLoadMore(userid).observe(this, new BaseObserver<LookGuardUserVo>() {
                @Override
                public void onSuccess(LookGuardUserVo result) {
                    if (result != null) {
                        LookGuardUserVo.PageInfoBean pageInfo = result.getPageInfo();
                        mAdapter.addData(result.getGuard_memberlist());
                        mBinding.refreshLayout.finishLoadMore();
                        mBinding.refreshLayout.setEnableLoadMore(Integer.valueOf(pageInfo.getPage()) < pageInfo.getTotalPage());
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

    ItemGuardListTopBinding mHeaderBinding;

    private void addHeader() {
        mHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), com.deepsea.mua.stub.R.layout.item_guard_list_top, null, false);
        mAdapterWithHF.addHeader(mHeaderBinding.getRoot());
        mAdapterWithHF.showHeaderView(true);
    }

    private int setTopRanks(List<LookGuardUserVo.GuardMemberlistBean> result) {
        int topSize = 0;
        if (result == null) {
            result = new ArrayList<>();
        }
        //top 1
        int index = 0;
        if (index < result.size()) {
            topSize++;
            ViewBindUtils.setVisible(mHeaderBinding.ivRankOneHead, true);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankOneName, true);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankOneIntimacy, true);
            LookGuardUserVo.GuardMemberlistBean bean = result.get(index);
            GlideUtils.circleImage(mHeaderBinding.ivRankOneHead, bean.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            ViewBindUtils.setText(mHeaderBinding.tvRankOneName, bean.getNickname());
            ViewBindUtils.setText(mHeaderBinding.tvRankOneIntimacy, "亲密值：" + bean.getIntimacy());
            mHeaderBinding.ivRankOneHead.setOnClickListener(v -> {
                PageJumpUtils.jumpToProfile(bean.getUserId());
            });
        } else {
            ViewBindUtils.setVisible(mHeaderBinding.ivRankOneHead, false);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankOneName, false);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankOneIntimacy, false);
        }
        //top 2
        index = 1;
        if (index < result.size()) {
            topSize++;
            ViewBindUtils.setVisible(mHeaderBinding.ivRankTwoHead, true);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankTwoName, true);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankTwoIntimacy, true);
            LookGuardUserVo.GuardMemberlistBean bean = result.get(index);
            GlideUtils.circleImage(mHeaderBinding.ivRankTwoHead, bean.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            ViewBindUtils.setText(mHeaderBinding.tvRankTwoName, bean.getNickname());
            ViewBindUtils.setText(mHeaderBinding.tvRankTwoIntimacy, "亲密值：" + bean.getIntimacy());
            mHeaderBinding.ivRankTwoHead.setOnClickListener(v -> {
                PageJumpUtils.jumpToProfile(String.valueOf(bean.getUserId()));
            });
        } else {
            ViewBindUtils.setVisible(mHeaderBinding.ivRankTwoHead, false);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankTwoName, false);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankTwoIntimacy, false);
        }
        //top 3
        index = 2;
        if (index < result.size()) {
            topSize++;
            ViewBindUtils.setVisible(mHeaderBinding.ivRankThreeHead, true);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankThreeName, true);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankThreeIntimacy, true);
            LookGuardUserVo.GuardMemberlistBean bean = result.get(index);
            GlideUtils.circleImage(mHeaderBinding.ivRankThreeHead, bean.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            ViewBindUtils.setText(mHeaderBinding.tvRankThreeName, bean.getNickname());
            ViewBindUtils.setText(mHeaderBinding.tvRankThreeIntimacy, "亲密值：" + bean.getIntimacy());
            mHeaderBinding.ivRankThreeHead.setOnClickListener(v -> {
                PageJumpUtils.jumpToProfile(String.valueOf(bean.getUserId()));
            });
        } else {
            ViewBindUtils.setVisible(mHeaderBinding.ivRankThreeHead, false);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankThreeName, false);
            ViewBindUtils.setVisible(mHeaderBinding.tvRankThreeIntimacy, false);
        }
        return topSize;
    }

    //续费
    private void fetchRenew(String is_active, String target_id) {
        mViewModel.initGuard(target_id).observe(this, new BaseObserver<RenewInitVo>() {
            @Override
            public void onSuccess(RenewInitVo result) {
                GuardRenewDialog renewCloseDialog = new GuardRenewDialog(mContext);
                renewCloseDialog.setData(result);
                renewCloseDialog.setOnClickListener(new GuardRenewDialog.OnClickListener() {
                    @Override
                    public void onClickOk(int payType, String coin, String chargeId, String guard_id, String long_day) {
                        if (payType == 0) {
                            wxpay(coin, is_active, chargeId, target_id, guard_id, long_day);
                        } else {
                            alipay(coin, is_active, chargeId, target_id, guard_id, long_day);
                        }
                    }
                });
                renewCloseDialog.show();
            }
        });
    }

    private WxPay mWxPay;

    private void alipay(String coin, String is_active, String chargeId, String target_id, String guard_id, String long_day) {
        mViewModel.alipay(coin, is_active, chargeId, target_id, guard_id, long_day)
                .observe(this, new ProgressObserver<String>(mContext) {
                    @Override
                    public void onSuccess(String result) {
                        RxPermissions permissions = new RxPermissions(GuardActivity.this);
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
        Alipay alipay = new Alipay(GuardActivity.this);
        alipay.startPay(signature);
        alipay.setAlipayListener(new Alipay.AlipayListener() {
            @Override
            public void onSuccess(PayResult result) {
//                requestBalance();
//                MobEventUtils.onRechargeEvent(mContext, mRechargeAmount);
//                mRechargeAmount = "0";
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

    private void wxpay(String coin, String is_active, String chargeId, String target_id, String guard_id, String long_day) {

        mViewModel.wxpay(coin, is_active, chargeId, target_id, guard_id, long_day)
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
