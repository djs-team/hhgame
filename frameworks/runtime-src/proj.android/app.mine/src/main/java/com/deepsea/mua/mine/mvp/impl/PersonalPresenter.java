package com.deepsea.mua.mine.mvp.impl;


import com.deepsea.mua.core.utils.LogUtils;
import com.deepsea.mua.mine.mvp.PersonalContracts;
import com.deepsea.mua.mine.mvp.manager.PersonalDataManager;
import com.deepsea.mua.stub.entity.PresentWallBean;
import com.deepsea.mua.stub.mvp.NewSubscriberCallBack;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * author : liyaxing
 * date   : 2019/3/23 10:39
 * desc   : 个人 Presenter
 */

public class PersonalPresenter implements PersonalContracts.Presenter {

    private PersonalContracts.PresentIdentityAuthView mPersonalLevelView;
    private PersonalContracts.PresentWallListView mPresentWallListView;

    private PersonalDataManager mDataManager;
    public CompositeSubscription mCompositeSubscription;

    public PersonalPresenter(PersonalContracts.PresentIdentityAuthView mView, int aType) {
        this.mPersonalLevelView = mView;
        mView.setPresenter(this);
        mDataManager = PersonalDataManager.getInstance();
        mCompositeSubscription = new CompositeSubscription();

    }

    public PersonalPresenter(PersonalContracts.PresentWallListView mView, int aType) {
        this.mPresentWallListView = mView;
        mView.setPresenter(this);
        mDataManager = PersonalDataManager.getInstance();
        mCompositeSubscription = new CompositeSubscription();

    }


    @Override
    public void detachView() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }

    @Override
    public void realuser(String name, String idcard) {
        mCompositeSubscription.add(mDataManager.realuser(name, idcard).subscribe(new NewSubscriberCallBack<String>() {
            @Override
            protected void onError(int errorCode, String errorMsg) {
                mPersonalLevelView.showErrorMsg(1, errorMsg);
            }

            @Override
            protected void onSuccess(String response) {
                mPersonalLevelView.realuserOk(response);
            }

        }));
    }

    @Override
    public void getPresenList(String touid) {
        LogUtils.i("getPresenList-111--" + touid);
        mCompositeSubscription.add(mDataManager.getPresenList(touid).subscribe(new NewSubscriberCallBack<List<PresentWallBean>>() {
            @Override
            protected void onError(int errorCode, String errorMsg) {
                mPresentWallListView.showErrorMsg(1, errorMsg);
            }

            @Override
            protected void onSuccess(List<PresentWallBean> response) {
                mPresentWallListView.getPresenListOk(response);
            }

        }));
    }


}
