package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.RechargeRepository;
import com.deepsea.mua.mine.repository.WalletRepository;
import com.deepsea.mua.stub.entity.ChargeBean;
import com.deepsea.mua.stub.entity.HaiPayBean;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.UserUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/16
 */
public class RechargeViewModel extends ViewModel {

    private final RechargeRepository mRepository;
    private final WalletRepository mWalletRepository;

    @Inject
    public RechargeViewModel(RechargeRepository repository, WalletRepository walletRepository) {
        mRepository = repository;
        mWalletRepository = walletRepository;
    }

    /**
     * 微信支付
     *
     * @param rmb
     * @return
     */
    public LiveData<Resource<WxOrder>> wxpay(String rmb, String chargeid) {
        String uid = UserUtils.getUser().getUid();
        return mRepository.wxpay(rmb, Constant.CHARGE_NORMAL, Constant.CHARGE_ACT_N, uid, chargeid,"","","");
    }

    /**
     * 海贝支付
     *
     * @param rmb
     * @return
     */
    public LiveData<Resource<HaiPayBean>> haipay(String rmb, String chargeid) {
        String uid = UserUtils.getUser().getUid();
        return mRepository.haipay(rmb, Constant.CHARGE_NORMAL, Constant.CHARGE_ACT_N, uid, chargeid,"","","");
    }

    /**
     * 支付宝支付
     *
     * @param rmb
     * @return
     */
    public LiveData<Resource<String>> alipay(String rmb, String chargeid) {
        String uid = UserUtils.getUser().getUid();
        return mRepository.alipay(rmb, Constant.CHARGE_NORMAL, Constant.CHARGE_ACT_N, uid, chargeid,"","","");
    }

    public LiveData<Resource<ChargeBean>> chargelist() {
        return mRepository.chargelist();
    }

    public LiveData<Resource<WalletBean>> wallet() {
        return mWalletRepository.wallet();
    }
}
