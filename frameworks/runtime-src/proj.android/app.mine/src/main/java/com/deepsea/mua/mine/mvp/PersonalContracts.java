package com.deepsea.mua.mine.mvp;


import com.deepsea.mua.stub.entity.PresentWallBean;
import com.deepsea.mua.stub.mvp.BaseView;

import java.util.List;

/**
 * author : liyaxing
 * date   : 2019/3/23 10:39
 * desc   : 个人 Contracts
 */


public class PersonalContracts {

    public interface Presenter {


        void detachView();

        void realuser(String name, String idcard);

        void getPresenList(String touid);

    }


    public interface PresentIdentityAuthView extends BaseView<Presenter> {

        void showErrorMsg(int aType, String msg);
        void realuserOk(String response);


    }

    public interface PresentWallListView extends BaseView<Presenter> {

        void showErrorMsg(int aType, String msg);


        void getPresenListOk(List<PresentWallBean> response);



    }


}
