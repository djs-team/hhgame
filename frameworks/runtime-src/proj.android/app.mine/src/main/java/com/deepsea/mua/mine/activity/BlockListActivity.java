package com.deepsea.mua.mine.activity;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.BlockAdapter;
import com.deepsea.mua.mine.databinding.ActivityBlockListBinding;
import com.deepsea.mua.mine.dialog.BlockOperateDialog;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.entity.BlockVo;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;

/**
 * 黑名单
 */

public class BlockListActivity extends BaseActivity<ActivityBlockListBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private ProfileViewModel mViewModel;

    private BlockAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_block_list;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileViewModel.class);
        initRecyclerView();
        fetchData();
    }

    private void initRecyclerView() {
        mAdapter = new BlockAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            BlockVo blockVo = mAdapter.getItem(position);

            showOperateDialog(blockVo.getUserid(), position);
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    /**
     * 操作
     */
    private void showOperateDialog(String uid, int pos) {
        BlockOperateDialog operateDialog = new BlockOperateDialog(mContext);
        operateDialog.setOnClickListener(new BlockOperateDialog.OnClickListener() {
            @Override
            public void onLookDetailsClick() {
                PageJumpUtils.jumpToProfile(uid);
            }

            @Override
            public void onBlockCancelClick() {
                AAlertDialog confirmDialog = new AAlertDialog(mContext);
                confirmDialog.setMessage("");
                confirmDialog.setLeftButton("确定", new AAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View v, Dialog dialog) {
                        confirmDialog.dismiss();
                        fetchBlockCancel(uid, pos);
                    }
                });
                confirmDialog.setRightButton("取消", new AAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View v, Dialog dialog) {
                        confirmDialog.dismiss();
                    }
                });
                confirmDialog.show();
            }

            @Override
            public void onReportClick() {
                PageJumpUtils.jumpToReport(uid);
            }
        });
    }

    private void fetchBlockCancel(String uid, int pos) {
        mViewModel.blackout(uid).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                mAdapter.getData().remove(pos);
                mAdapter.notifyItemRemoved(pos);
            }
        });
    }

    private void fetchData() {
        mViewModel.getBlockList().observe(this, new BaseObserver<List<BlockVo>>() {
            @Override
            public void onSuccess(List<BlockVo> result) {
                if (result != null && result.size() > 0) {
                    mBinding.emptyTv.setVisibility(View.GONE);
                    mAdapter.setNewData(result);
                } else {
                    mBinding.emptyTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
                mBinding.refreshLayout.finishRefresh();
            }
        });
    }

}
