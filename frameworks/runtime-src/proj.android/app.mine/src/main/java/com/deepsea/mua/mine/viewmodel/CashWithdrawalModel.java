package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.CashWithdrawalRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CashInfo;
import com.deepsea.mua.stub.entity.WalletBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class CashWithdrawalModel extends ViewModel {

    private final CashWithdrawalRepository mRepository;

    @Inject
    public CashWithdrawalModel(CashWithdrawalRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<BaseApiResult>> cash(String apliuserid, String cash, String totalcash, String type, boolean isRedpackage) {
        if (isRedpackage) {
            return mRepository.redpacketCash(apliuserid, cash, totalcash, type);
        } else {
            return mRepository.cash(apliuserid, cash, totalcash, type);
        }
    }

    public LiveData<Resource<CashInfo>> fetchCashInfo(boolean isRedpackage) {
        if (isRedpackage) {
            return mRepository.fetchCashRedpackageInfo();

        } else {
            return mRepository.fetchCashInfo();
        }
    }
}
