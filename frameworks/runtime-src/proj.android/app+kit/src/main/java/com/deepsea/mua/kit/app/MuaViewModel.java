package com.deepsea.mua.kit.app;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.db.XyDao;
import com.deepsea.mua.stub.entity.GiftBean;
import com.deepsea.mua.stub.entity.GiftInfoBean;
import com.deepsea.mua.stub.entity.NightLockBean;
import com.deepsea.mua.stub.entity.YoungerTimeBean;
import com.deepsea.mua.stub.mvp.NewSubscriberCallBack;
import com.deepsea.mua.stub.network.HttpHelper;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.voice.activity.RoomActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2019/7/17
 */
public class MuaViewModel extends ViewModel {

    @Inject
    AppExecutors mExecutors;

    private final MuaRepository mRepository;

    @Inject
    public MuaViewModel(MuaRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<YoungerTimeBean>> checkLock() {
        return mRepository.checkLock();
    }

    public LiveData<Resource<BaseApiResult>> checkPop() {
        return mRepository.checkPop();
    }

    public LiveData<Resource<NightLockBean>> checkConsLock() {
        return mRepository.checkConsLock();
    }





    private Context getContext() {
        Activity activity = ActivityCache.getInstance().getTopActivity();
        Context context = activity;
        if (activity instanceof RoomActivity) {
            LinkedHashMap<Class<? extends Activity>, Activity> activityMap = ActivityCache.getInstance().getActivityMap();

            ListIterator<Map.Entry> iterator = new ArrayList<Map.Entry>(activityMap.entrySet()).listIterator(activityMap.size());


            while (iterator.hasPrevious()) {
                Map.Entry entry = iterator.previous();

                if (entry.getValue() != activity) {
                    context = (Context) entry.getValue();
                    break;
                }
            }

            activity.finish();
        }

        return context;
    }

    public void loadAndSaveGifts() {
        String type = "1";
        HttpHelper.instance().getApi(RetrofitApi.class).getAllGifts(type, SignatureUtils.signWith(type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewSubscriberCallBack<GiftInfoBean>() {
                    @Override
                    protected void onSuccess(GiftInfoBean response) {
                        if (response == null || CollectionUtils.isEmpty(response.getGift_info()))
                            return;
                        List<GiftBean> list = response.getGift_info();
                        mExecutors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {

                                XyDao.getInstance().saveGifts(list);
                            }
                        });
                    }

                    @Override
                    protected void onError(int errorCode, String errorMsg) {

                    }
                });

    }
}
