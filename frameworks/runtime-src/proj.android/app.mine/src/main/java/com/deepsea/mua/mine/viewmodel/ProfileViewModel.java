package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.ProfileRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.AreaVo;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.BlockVo;
import com.deepsea.mua.stub.entity.GuardInfoBean;
import com.deepsea.mua.stub.entity.InitInviteBean;
import com.deepsea.mua.stub.entity.JumpRoomVo;
import com.deepsea.mua.stub.entity.LookGuardUserVo;
import com.deepsea.mua.stub.entity.ProfileBean;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.entity.TaskBean;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.stub.utils.UserUtils;

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

    public LiveData<Resource<BaseApiResult>> attention_member(String uid, String type) {
        return repository.attention_member(uid, type);
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

    public LiveData<Resource<InitInviteBean>> initInvite() {
        return repository.initInvite();
    }

    public LiveData<Resource<BaseApiResult>> createapprove() {
        String signature = SignatureUtils.signByToken();
        return repository.createapprove(signature);
    }

    public LiveData<Resource<BaseApiResult>> bindReferrer(String referrer_code) {
        return repository.bindReferrer(referrer_code);
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
}
