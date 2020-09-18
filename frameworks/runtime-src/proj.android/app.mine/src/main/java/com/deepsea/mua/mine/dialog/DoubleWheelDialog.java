package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogDoubleWheelBinding;
import com.deepsea.mua.stub.callback.CommonCallback;
import com.deepsea.mua.stub.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JUN on 2019/10/18
 */
public class DoubleWheelDialog extends BaseDialog<DialogDoubleWheelBinding> {

    private CommonCallback<String> mCallback;

    private Map<String, List<String>> mDatas;

    public DoubleWheelDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_double_wheel;
    }

    @Override
    protected float getWidthPercent() {
        return 0.92F;
    }

    @Override
    protected void initListener() {
        mBinding.ensureTv.setOnClickListener(v -> {
            dismiss();
            if (mCallback != null) {
                mCallback.onSuccess(mBinding.wheelNo1.getCurrentItem() + "," + mBinding.wheelNo2.getCurrentItem());
            }
        });

        mBinding.wheelNo1.setOnWheelChangedListener((view, oldIndex, newIndex) -> {
            CharSequence item = view.getCurrentItem();
            if (mDatas != null && item != null) {
                List<String> subMenus = mDatas.get(item.toString());
                mBinding.wheelNo2.setEntries(subMenus);
            }
        });
    }

    public void setEnsureCallback(CommonCallback<String> callback) {
        mCallback = callback;
    }

    public DoubleWheelDialog setTitle(String title) {
        mBinding.titleTv.setText(title);
        return this;
    }

    public void setData(Map<String, List<String>> map, String value) {
        this.mDatas = map;
        if (CollectionUtils.isEmpty(map.keySet()))
            return;

        String menuDef = "";
        String subMenuDef = "";
        if (!TextUtils.isEmpty(value) && value.contains(",")) {
            String[] split = value.split(",");
            menuDef = split[0];
            subMenuDef = split[1];
        }

        List<String> menus = new ArrayList<>(map.keySet());
        mBinding.wheelNo1.setEntries(menus);
        int index = menus.contains(menuDef) ? menus.indexOf(menuDef) : 0;
        mBinding.wheelNo1.setCurrentIndex(index);

        List<String> subMenus = map.get(menus.get(index));
        mBinding.wheelNo2.setEntries(subMenus);
        if (subMenus != null) {
            index = subMenus.contains(subMenuDef) ? subMenus.indexOf(subMenuDef) : 0;
            mBinding.wheelNo2.setCurrentIndex(index);
        } else {
            mBinding.wheelNo2.setCurrentIndex(0);
        }
    }
}
