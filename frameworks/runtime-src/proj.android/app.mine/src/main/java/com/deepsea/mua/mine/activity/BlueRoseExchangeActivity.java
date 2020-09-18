package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.BlueRoseExchangeAdapter;
import com.deepsea.mua.mine.adapter.IncomeAdapter;
import com.deepsea.mua.mine.databinding.ActivityBlueRoseExchangeBinding;
import com.deepsea.mua.mine.databinding.ActivityIncomeDetailsBinding;
import com.deepsea.mua.mine.dialog.ExchangeBlueRoseDialog;
import com.deepsea.mua.mine.viewmodel.IncomeDetailsModel;
import com.deepsea.mua.mine.viewmodel.WalletViewModel;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.RecyclerAdapterWithHF;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.BlueRoseExchange;
import com.deepsea.mua.stub.entity.IncomeListBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.GridItemDecoration;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.TimeUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * @zu 蓝玫瑰兑换--初始化列表
 */
@Route(path = ArouterConst.PAGE_ME_MINE_BLUEROSE)

public class BlueRoseExchangeActivity extends BaseActivity<ActivityBlueRoseExchangeBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private WalletViewModel mViewModel;
    private BlueRoseExchangeAdapter mAdapter;
    private RecyclerAdapterWithHF mAdapterWithHF;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_blue_rose_exchange;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(WalletViewModel.class);
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.consTitlebar.getRightTv(), o -> {
            startActivity(new Intent(mContext, ExchangeBlueRoseExchangeActivity.class));
        });
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }


    private void initRecyclerView() {
        mAdapter = new BlueRoseExchangeAdapter(mContext);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
//        mBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                if (parent.getChildAdapterPosition(view)%3==1){
//                   outRect.left= ResUtils.dp2px(mContext, 14);
//                   outRect.right= ResUtils.dp2px(mContext, 14);
//                }
//            }
//        });
//        mBinding.recyclerView.setAdapter(mAdapter);

        mAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        mAdapterWithHF.setManagerType(RecyclerAdapterWithHF.TYPE_MANAGER_GRID);
        mAdapterWithHF.setRecycleView(mBinding.recyclerView);
        mBinding.recyclerView.setAdapter(mAdapterWithHF);
        mAdapter.setOnItemClickListener(new BaseBindingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showExchangeDialog(position);
            }
        });
        addHeader();
    }

    ExchangeBlueRoseDialog dialog;

    private void showExchangeDialog(int pos) {
        dialog = new ExchangeBlueRoseDialog(mContext);
        dialog.setMsg(mAdapter.getData().get(pos));
        dialog.setmListener(new ExchangeBlueRoseDialog.OnClickListener() {
            @Override
            public void onClick(String num, String gift_id) {
                fetchExchangeBlue(num, gift_id, pos);
            }
        });
        dialog.show();
    }

    private void fetchExchangeBlue(String num, String giftId, int pos) {
        showProgress();
        mViewModel.exchangeBlue(num, giftId).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                hideProgress();
                if (result.getCode() != 200) {
                    toastShort(result.getDesc());
                } else {
                    BlueRoseExchange blueRoseExchange = mAdapter.getData().get(pos);
                    blueRoseExchange.setPack_num(String.valueOf(Integer.valueOf(blueRoseExchange.getPack_num()) - Integer.valueOf(num)));
                    mAdapter.notifyItemChanged(pos);
                    toastShort("兑换成功");
                    setResult(Activity.RESULT_OK);
                    if (dialog != null)
                        dialog.dismiss();

                }

            }

            @Override
            public void onError(String msg, int code) {
                hideProgress();
                super.onError(msg, code);
            }
        });
    }


    private void addHeader() {
        View view = View.inflate(mContext, R.layout.item_blue_rose_exchange_header, null);
        mAdapterWithHF.addHeader(view);
        mAdapterWithHF.showHeaderView(true);
    }


    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.initBlueExchange().observe(this, new BaseObserver<List<BlueRoseExchange>>() {
                @Override
                public void onSuccess(List<BlueRoseExchange> result) {
                    if (result != null) {

                        mAdapter.setNewData(result);
                        mBinding.refreshLayout.finishRefresh();
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    toastShort(msg);
                    mBinding.refreshLayout.finishRefresh();
                }
            });
        });
        mBinding.refreshLayout.autoRefresh();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
