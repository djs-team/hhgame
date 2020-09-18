package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.ProfileEditAdapter;
import com.deepsea.mua.mine.databinding.ActivityMarriageSeekingBinding;
import com.deepsea.mua.mine.dialog.NumberRangeDialog;
import com.deepsea.mua.mine.dialog.WheelDialog;
import com.deepsea.mua.mine.viewmodel.ProfileEditViewModel;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.callback.CommonCallback;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.entity.ProfileModel;
import com.deepsea.mua.stub.entity.ProvinceBean;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.FileUtils;
import com.deepsea.mua.stub.utils.RegulerUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/10/14
 */
public class MarriageSeekingActivity extends BaseActivity<ActivityMarriageSeekingBinding>
        implements BaseBindingAdapter.OnItemClickListener {

    @Inject
    ViewModelFactory mModelFactory;
    private ProfileEditViewModel mViewModel;

    private WheelDialog mWheelDialog;
    private NumberRangeDialog mNumberDialog;

    private ProfileEditAdapter mAdapter;

    private Map<String, String> mEditMap = new HashMap<>();

    private List<String> mProvinces;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_marriage_seeking;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileEditViewModel.class);
        initRecyclerView();
        getMenus();
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.titleBar.getRightTv(), o -> {
            updateInfo();
        });

        subscribeClick(mBinding.titleBar.getLeftImg(), o -> {
            onBackPressed();
        });
    }

    private void initRecyclerView() {
        mAdapter = new ProfileEditAdapter(mContext);
        mAdapter.setOnItemClickListener(this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void getMenus() {
        mViewModel.getConditionList().observe(this, new BaseObserver<List<ProfileModel.ProfileMenu>>() {
            @Override
            public void onSuccess(List<ProfileModel.ProfileMenu> result) {
                mAdapter.setNewData(result);
            }
        });
    }

    private List<String> getProvinces() {
        if (mProvinces == null) {
            mProvinces = new ArrayList<>();
            String json = FileUtils.readFromAssets(mContext, "province.json");
            List<ProvinceBean> source = JsonConverter.getListFromJSON(json, ProvinceBean[].class);
            for (ProvinceBean bean : source) {
                mProvinces.add(bean.getName());
            }
        }
        return mProvinces;
    }

    private void updateInfo() {
        if (mEditMap.isEmpty()) {
            finish();
        } else {
            String json = JsonConverter.toJson(mEditMap);
            mViewModel.updateInfo(json).observe(this, new ProgressObserver<BaseApiResult>(mContext) {
                @Override
                public void onSuccess(BaseApiResult result) {
                    toastShort(result.getDesc());
                    finish();
                }
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ProfileModel.ProfileMenu item = mAdapter.getItem(position);
        String mold = item.getMold();
        String title = "修改" + item.getType_name();
        String name = TextUtils.isEmpty(item.getName()) ? item.getDefault_name() : item.getName();

        switch (mold) {
            case "1": //单选
                showWheelDialog(item.getList(), name, position);
                break;
            case "2":  //数字区间
                if (mNumberDialog == null) {
                    mNumberDialog = new NumberRangeDialog(mContext);
                }
                mNumberDialog.setEnsureCallback(new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String value) {
                        item.setName(value);
                        mAdapter.notifyItemChanged(position);
                        mEditMap.put(item.getColumn(), value);
                    }
                });

                if (TextUtils.equals("friend_age", item.getColumn())) {
                    mNumberDialog.setSuffix("岁");
                } else if (TextUtils.equals("friend_stature", item.getColumn())) {
                    mNumberDialog.setSuffix("cm");
                }

                mNumberDialog.setTitle(title);
                if (!CollectionUtils.isEmpty(item.getList())) {
                    mNumberDialog.setData(RegulerUtils.matchNumComma(item.getList().get(0)), name);
                }

                mNumberDialog.show();
                break;
            case "6":  //城市
                showWheelDialog(getProvinces(), name, position);
        }
    }

    private void showWheelDialog(List<String> list, String name, int position) {
        ProfileModel.ProfileMenu item = mAdapter.getItem(position);
        if (mWheelDialog == null) {
            mWheelDialog = new WheelDialog(mContext);
        }
        mWheelDialog.setWheelDialogListener(value -> {
            item.setName(value);
            mAdapter.notifyItemChanged(position);
            mEditMap.put(item.getColumn(), value);
        });
        mWheelDialog.setTitle("修改" + item.getType_name());
        mWheelDialog.setEntries(list, name);
        mWheelDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (mEditMap.isEmpty()) {
            super.onBackPressed();
        } else {
            AAlertDialog dialog = new AAlertDialog(mContext);
            dialog.setTitle("是否保存当前信息", R.color.black, 15)
                    .setLeftButton("取消", R.color.gray, (v, dialog1) -> {
                        dialog1.dismiss();
                        finish();
                    })
                    .setRightButton("保存", R.color.primary_pink, (v, dialog1) -> {
                        dialog1.dismiss();
                        updateInfo();
                    })
                    .show();
        }
    }
}
