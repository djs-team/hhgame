package com.deepsea.mua.mine.mvp.manager;


import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.entity.PresentWallBean;
import com.deepsea.mua.stub.mvp.ResponseModel;
import com.deepsea.mua.stub.network.HttpHelper;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author : liyaxing
 * date   : 2019/3/23 10:39
 * desc   : 个人DataManager
 */
public class PersonalDataManager {
    private static PersonalDataManager instance;

    public static PersonalDataManager getInstance() {
        return instance == null ? new PersonalDataManager() : instance;
    }


    public Observable<ResponseModel<String>> toDefriend(String user_id, String signature) {
        return HttpHelper.instance().getApi(RetrofitApi.class).toDefriend(user_id, signature)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResponseModel<List<PresentWallBean>>> getPresenList(String touid) {
        return HttpHelper.instance().getApi(RetrofitApi.class).getPresenList(touid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResponseModel<String>> realuser(String name, String idcard) {
        return HttpHelper.instance().getApi(RetrofitApi.class).realuser(name, idcard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
