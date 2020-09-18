package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.ProfileEditAdapter;
import com.deepsea.mua.mine.databinding.ActivityProfileEditBinding;
import com.deepsea.mua.mine.dialog.CharmDialog;
import com.deepsea.mua.mine.dialog.DoubleWheelDialog;
import com.deepsea.mua.mine.dialog.InputDialog;
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
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.ClickEvent;
import com.deepsea.mua.stub.utils.eventbus.ClickEventType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/16
 */
public class ProfileEditActivity extends BaseActivity<ActivityProfileEditBinding>
        implements BaseBindingAdapter.OnItemClickListener {

    @Inject
    ViewModelFactory mModelFactory;
    private ProfileEditViewModel mViewModel;

    private ProfileEditAdapter mAdapter;

    private WheelDialog mWheelDialog;
    private CharmDialog mCharmDialog;
    private DoubleWheelDialog mDoubleWheelDialog;
    private InputDialog mInputDialog;

    private Map<String, String> mEditMap = new HashMap<>();

    private List<String> mProvinces;

    private String mJyxs;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile_edit;
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
            String input = mBinding.signEdit.getText().toString();
            if (!TextUtils.equals(mJyxs, input)) {
                mEditMap.put("intro", input);
            }
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
        mViewModel.getMenusList().observe(this, new BaseObserver<ProfileModel>() {
            @Override
            public void onSuccess(ProfileModel result) {
                mJyxs = result.getJyxs();
                if (!TextUtils.isEmpty(mJyxs)) {
                    mBinding.signEdit.setText(mJyxs);
                    mBinding.signEdit.setSelection(mJyxs.length());
                }
                mAdapter.setNewData(result.getOptions());
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
//            Log.d("profile",json);
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
        String name = TextUtils.isEmpty(item.getName()) ? item.getDefault_name() : item.getName();
        String title = "修改" + item.getType_name();

        switch (mold) {
            case "1": //单选
                showWheelDialog(item.getList(), position);
                break;
            case "2":  //数字区间
                break;
            case "3":  //多选
                if (mCharmDialog == null) {
                    mCharmDialog = new CharmDialog(mContext);
                }
                mCharmDialog.setEnsureCallback(new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        item.setName(data);
                        mAdapter.notifyItemChanged(position);
                        mEditMap.put(item.getColumn(), data);
                    }
                });
                mCharmDialog.setTitle(title);
                mCharmDialog.setData(item.getList(), name);
                mCharmDialog.show();
                break;
            case "4":  //输入框
                if (mInputDialog == null) {
                    mInputDialog = new InputDialog(mContext);
                }
                mInputDialog.setEnsureCallback(new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        item.setName(data);
                        mAdapter.notifyItemChanged(position);
                        mEditMap.put(item.getColumn(), data);
                    }
                });
                mInputDialog.setData(name);
                mInputDialog.show();
                break;
            case "5":  //数字单选
                if (!CollectionUtils.isEmpty(item.getList())) {
                    String[] split = item.getList().get(0).split(",");
                    if (split.length == 2) {
                        List<String> options = new ArrayList<>();
                        for (int i = Integer.parseInt(split[0]); i <= Integer.parseInt(split[1]); i++) {
                            options.add(String.valueOf(i));
                        }
                        showWheelDialog(options, position);
                    }
                }
                break;
            case "6":  //城市
                showWheelDialog(getProvinces(), position);
                break;
            case "7":  //职业
                if (mDoubleWheelDialog == null) {
                    mDoubleWheelDialog = new DoubleWheelDialog(mContext);
                }
                mDoubleWheelDialog.setEnsureCallback(new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        item.setName(data);
                        mAdapter.notifyItemChanged(position);
                        mEditMap.put(item.getColumn(), data);
                    }
                });
                mDoubleWheelDialog.setTitle(title);
                mDoubleWheelDialog.setData(item.getOption(), name);
                mDoubleWheelDialog.show();
                break;
        }
    }

    private void showWheelDialog(List<String> list, int position) {
        ProfileModel.ProfileMenu item = mAdapter.getItem(position);
        String name = TextUtils.isEmpty(item.getName()) ? item.getDefault_name() : item.getName();
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
        if (item.getType_name().equals("身高")){
            mWheelDialog.setCurrentIndex(30);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mEditMap.isEmpty()) {
            mEditMap.clear();
            ClickEvent event = new ClickEvent();
            event.setClick(ClickEventType.Click8);
            EventBus.getDefault().post(event);
        }
    }
}
