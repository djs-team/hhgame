package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.MePackAdapter;
import com.deepsea.mua.mine.databinding.ActivityMePackBinding;
import com.deepsea.mua.mine.viewmodel.MePackViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/7/24
 */
@Route(path = ArouterConst.PAGE_ME_PACK)
public class MePackActivity extends BaseActivity<ActivityMePackBinding> {

    @Inject
    ViewModelFactory mFactory;
    private MePackViewModel mViewModel;

    private MePackAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_pack;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mFactory).get(MePackViewModel.class);
        initRecyclerView();
        initRefreshLayout();
    }

    private void initRecyclerView() {
        mAdapter = new MePackAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
        });
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setEnableLoadMore(false);
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.getMePacks().observe(this, new BaseObserver<List<PackBean>>() {
                @Override
                public void onSuccess(List<PackBean> list) {
                    ViewBindUtils.setVisible(mBinding.line, list != null && !list.isEmpty());
                    mAdapter.setNewData(list);
                    mBinding.refreshLayout.finishRefresh();
                }

                @Override
                public void onError(String msg, int code) {
                    super.onError(msg, code);
                    mBinding.refreshLayout.finishRefresh();
                }
            });
        });
        mBinding.refreshLayout.autoRefresh();
    }
}
