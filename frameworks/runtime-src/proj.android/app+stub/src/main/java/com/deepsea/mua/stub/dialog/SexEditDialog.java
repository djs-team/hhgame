package com.deepsea.mua.stub.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.databinding.DialogEditSexBinding;
import com.deepsea.mua.stub.databinding.DialogOpenVipBinding;
import com.deepsea.mua.stub.entity.HeartBeatBean;
import com.deepsea.mua.stub.mvp.NewSubscriberCallBack;
import com.deepsea.mua.stub.network.HttpHelper;
import com.deepsea.mua.stub.utils.AppUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.eventbus.HeartBeatEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteDialogEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2018/9/27
 */
public class SexEditDialog extends BaseDialog<DialogEditSexBinding> {


    public SexEditDialog(@NonNull Context context) {
        super(context);
        init();
    }


    @Override
    public void dismiss() {
        super.dismiss();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_edit_sex;
    }

    @Override
    protected float getWidthPercent() {
        return 0.55F;
    }

    @Override
    protected float getDimAmount() {
        return 0.5F;
    }


    private void init() {
        ViewBindUtils.RxClicks(mBinding.rlSexManSelect, o -> {
            selectSex(true);
        });
        ViewBindUtils.RxClicks(mBinding.rlSexWomanSelect, o -> {
            selectSex(false);
        });
        ViewBindUtils.RxClicks(mBinding.tvConfirm, o -> {
            fetchSexEdit();
        });
        selectSex(true);
    }

    private String defaultSextFlag = "1";

    private void selectSex(boolean selectMan) {
        if (selectMan) {
            defaultSextFlag = "1";
            ViewBindUtils.setVisible(mBinding.ivManCmark, true);
            ViewBindUtils.setVisible(mBinding.ivWomanCmark, false);
        } else {
            defaultSextFlag = "2";
            ViewBindUtils.setVisible(mBinding.ivManCmark, false);
            ViewBindUtils.setVisible(mBinding.ivWomanCmark, true);
        }

    }

    //请求接口修改性别
    private void fetchSexEdit() {
        HttpHelper.instance().getApi(RetrofitApi.class).sexEdit(defaultSextFlag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewSubscriberCallBack<BaseApiResult>() {
                    @Override
                    protected void onError(int errorCode, String errorMsg) {
                        ToastUtils.showToast(errorMsg);
                    }

                    @Override
                    protected void onSuccess(BaseApiResult result) {
                        User user = UserUtils.getUser();
                        user.setSex(defaultSextFlag);
                    }
                });
    }
}
