package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.WalletRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.BlueRoseExchange;
import com.deepsea.mua.stub.entity.ExchangeBlueRoseRecordListParam;
import com.deepsea.mua.stub.entity.ExchangeBuleRoseVo;
import com.deepsea.mua.stub.entity.InitCash;
import com.deepsea.mua.stub.entity.WalletBean;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class WalletViewModel extends ViewModel {

    private final WalletRepository mRepository;

    @Inject
    public WalletViewModel(WalletRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<WalletBean>> wallet() {
        return mRepository.wallet();
    }

    /**
     * 初始化蓝玫瑰兑换
     *
     * @return
     */
    public LiveData<Resource<List<BlueRoseExchange>>> initBlueExchange() {
        return mRepository.initBlueExchange();
    }
    public int pageNumber;

    /**
     * 蓝玫瑰兑换记录
     *
     * @return
     */
    public LiveData<Resource<ExchangeBlueRoseRecordListParam>> refreshBlueExchangeInfo() {
        pageNumber=1;
        return mRepository.blueExchangeInfo(pageNumber);
    }
    /**
     * 蓝玫瑰兑换记录
     *
     * @return
     */
    public LiveData<Resource<ExchangeBlueRoseRecordListParam>> loadModeBlueExchangeInfo() {
        pageNumber++;
        return mRepository.blueExchangeInfo(pageNumber);
    }

    public LiveData<Resource<BaseApiResult>> checkSatus() {
        return mRepository.checkSatus();
    }

    public LiveData<Resource<BaseApiResult>> exchangeRose(int coin) {
        return mRepository.exchangeRose(coin);
    }

    public LiveData<Resource<BaseApiResult>> exchangeBlue(String num, String giftId) {
        return mRepository.exchangeBlue(num, giftId);
    }

    public LiveData<Resource<InitCash>> initCash() {
        return mRepository.initCash();
    }
}
