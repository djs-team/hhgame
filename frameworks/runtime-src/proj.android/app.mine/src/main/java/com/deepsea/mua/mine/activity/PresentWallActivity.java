package com.deepsea.mua.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.deepsea.mua.core.utils.LogUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.PresentWallAdapter;
import com.deepsea.mua.mine.databinding.ActivityPresentWallBinding;
import com.deepsea.mua.mine.mvp.PersonalContracts;
import com.deepsea.mua.mine.mvp.impl.PersonalPresenter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.entity.PresentWallBean;

import java.util.List;

/**
 * 作者：liyaxing  2019/9/2 16:14
 * 类别 ：
 */
public class PresentWallActivity extends BaseActivity<ActivityPresentWallBinding> implements PersonalContracts.PresentWallListView {


    private String uid;

    public static Intent newIntent(Context context, String uid) {
        Intent intent = new Intent(context, PresentWallActivity.class);
        intent.putExtra("uid", uid);
        return intent;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_present_wall;
    }

    PersonalContracts.Presenter mPresenter;
    PresentWallAdapter mEasyAdapter;

    @Override
    protected void initView() {
        uid = getIntent().getStringExtra("uid");
        mPresenter = new PersonalPresenter(this, 0);
        mEasyAdapter = new PresentWallAdapter(R.layout.present_wall_item);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        mBinding.aRecyclerView.setLayoutManager(manager);
        mBinding.aRecyclerView.setAdapter(mEasyAdapter);

//        mEasyAdapter.setOnLoadMoreListener(this, mBinding.aRecyclerView);//加载更多
        LogUtils.i("getPresenList--00-" + uid);
        mPresenter.getPresenList(uid);

    }


    @Override
    public void getPresenListOk(List<PresentWallBean> response) {
        LogUtils.i("getPresenListOk----" + response.get(0).toString());
        if (response.size() > 0) {
            mEasyAdapter.addData(response);
            mEasyAdapter.loadMoreComplete();
        } else {
            mEasyAdapter.loadMoreEnd();
        }


    }


    @Override
    public void setPresenter(PersonalContracts.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void showErrorMsg(int aType, String msg) {
        mEasyAdapter.loadMoreEnd();

    }

    @Override
    public void onFailure() {
        mEasyAdapter.loadMoreEnd();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
