package com.deepsea.mua.mine.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.deepsea.mua.core.login.ApiUser;
import com.deepsea.mua.core.login.LoginApi;
import com.deepsea.mua.core.login.OnLoginListener;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.mine.repository.ProfileRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.AreaVo;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.BindWx;
import com.deepsea.mua.stub.entity.BlockVo;
import com.deepsea.mua.stub.entity.CheckBindWx;
import com.deepsea.mua.stub.entity.GuardInfoBean;
import com.deepsea.mua.stub.entity.JumpRoomVo;
import com.deepsea.mua.stub.entity.LookGuardUserVo;
import com.deepsea.mua.stub.entity.ProfileBean;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.entity.TaskBean;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/12
 */
public class ProfileViewModel extends ViewModel {

    private final ProfileRepository repository;

    @Inject
    public ProfileViewModel(ProfileRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<ProfileBean>> user_info(String uid) {
        return repository.user_info(uid, SignatureUtils.signWith(uid));
    }



    public LiveData<Resource<BaseApiResult>> defriend(String uid) {
        return repository.defriend(uid, SignatureUtils.signWith(uid));
    }

    public LiveData<Resource<BaseApiResult>> blackout(String uid) {
        return repository.blackout(uid, SignatureUtils.signWith(uid));
    }

    public LiveData<Resource<AuditBean>> getVerifyToken() {
        String signature = SignatureUtils.signByToken();
        return repository.getVerifyToken(signature);
    }


    public LiveData<Resource<BaseApiResult>> createapprove() {
        String signature = SignatureUtils.signByToken();
        return repository.createapprove(signature);
    }




    /**
     * 随机跳转前六直播间接口
     *
     * @return
     */
    public LiveData<Resource<JumpRoomVo>> jumpRoom() {
        return repository.jumpRoom();
    }

    public LiveData<Resource<BaseApiResult>> applyInfo(String wx, String age) {
        return repository.applyInfo(wx, age);
    }

    public LiveData<Resource<List<AreaVo>>> getArea(int level, int pcode) {
        return repository.getArea(level, pcode);
    }

    public int page;
    private int pageNumber = 20;

    public LiveData<Resource<LookGuardUserVo>> guardRefresh(String userid) {
        page = 1;
        return repository.getGuardList(userid, page);
    }

    public LiveData<Resource<LookGuardUserVo>> guardLoadMore(String userid) {
        page++;
        return repository.getGuardList(userid, page);
    }

    public LiveData<Resource<TaskBean>> getTaskList() {
        return repository.getTaskList();
    }

    public LiveData<Resource<BaseApiResult>> taskReceive(String type, String taskId) {
        return repository.taskReceive(type, taskId);
    }

    public LiveData<Resource<GuardInfoBean>> guardInfoRefresh(String type) {
        pageNumber = 1;
        return repository.guardInfoList(type, pageNumber);
    }

    public LiveData<Resource<GuardInfoBean>> guardInfoLoadMore(String type) {
        pageNumber++;
        return repository.guardInfoList(type, pageNumber);
    }

    public LiveData<Resource<RenewInitVo>> initGuard(String target_id) {
        return repository.initGuard(target_id);
    }

    /**
     * 微信支付
     *
     * @param rmb
     * @return
     */
    public LiveData<Resource<WxOrder>> wxpay(String rmb, String is_active, String chargeid, String target_id, String guard_id, String long_day) {
        String uid = UserUtils.getUser().getUid();
        return repository.wxpay(rmb, Constant.CHARGE_Guard, is_active, uid, chargeid, target_id, guard_id, long_day);
    }


    /**
     * 支付宝支付
     *
     * @param rmb
     * @return
     */
    public LiveData<Resource<String>> alipay(String rmb, String is_active, String chargeid, String target_id, String guard_id, String long_day) {
        String uid = UserUtils.getUser().getUid();
        return repository.alipay(rmb, Constant.CHARGE_Guard, is_active, uid, chargeid, target_id, guard_id, long_day);
    }

    /**
     * 黑名单
     */
    public LiveData<Resource<List<BlockVo>>> getBlockList() {
        return repository.getBlockList();
    }


    public LiveData<Resource<BaseApiResult>> setFeedback(String content) {
        return repository.setFeedback(content, SignatureUtils.signByToken());
    }

    public LiveData<Resource<BaseApiResult>> unBindWx(String wx_id) {
        return repository.unBindWx(wx_id);
    }

    public LiveData<Resource<CheckBindWx>> isBindWx() {
        return repository.isBindWx();
    }

    public LiveData<Resource<BaseApiResult<BindWx>>> thirdlogin(String platform, Activity activity) {
        MediatorLiveData<Resource<BaseApiResult<BindWx>>> result = new MediatorLiveData<>();

        LoginApi loginApi = new LoginApi();
        loginApi.setPlatform(platform, activity);
        loginApi.setOnLoginListener(new OnLoginListener() {
            @Override
            public void onLogin(ApiUser apiUser) {
                String extras = JsonConverter.toJson(apiUser.getRes());
                LiveData<Resource<BaseApiResult<BindWx>>> source = repository.bindWx(apiUser.getOpenId());
                result.addSource(source, result::postValue);

            }

            @Override
            public void onCancel() {
                result.postValue(Resource.error("取消登录", null));
            }

            @Override
            public void onError(String msg) {
                result.postValue(Resource.error(msg, null));
            }
        });
        loginApi.login();

        return result;
    }
    public LiveData<Resource<BaseApiResult>> bindPhone(String phone, String pcode) {
        return repository.bindPhone(phone, pcode);
    }
    private CountDownTimer mTimer;
    private static final int COUNT_DOWN_TOTAL_MILLIS = 60 * 1000;
    private static final int COUNT_DOWN_INTERVAL_MILLIS = 1000;
    public void initSendCaptchaTv(final TextView sendCaptchaTv) {
        if (mTimer == null) {
            ViewBindUtils.setEnable(sendCaptchaTv, false);
            mTimer = new CountDownTimer(COUNT_DOWN_TOTAL_MILLIS, COUNT_DOWN_INTERVAL_MILLIS) {
                @Override
                public void onTick(long millisUntilFinished) {
                    ViewBindUtils.setText(sendCaptchaTv, millisUntilFinished / 1000 + "s");
                }

                @Override
                public void onFinish() {
                    ViewBindUtils.setEnable(sendCaptchaTv, true);
                    ViewBindUtils.setText(sendCaptchaTv, "验证码");
                    mTimer = null;
                }

            };
        }
        mTimer.start();
    }

    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
    public LiveData<Resource<BaseApiResult>> sendSMS(String phone, String type) {
        return repository.sendSMS(phone, type);
    }
}
