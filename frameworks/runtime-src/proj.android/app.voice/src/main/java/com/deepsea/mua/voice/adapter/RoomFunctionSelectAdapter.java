package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.RoomTagListBean;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRoomFunctionSelectBinding;
import com.deepsea.mua.voice.databinding.ItemUserFansBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JUN on 2019/10/14
 */
public class RoomFunctionSelectAdapter extends BaseBindingAdapter<RoomTagListBean.RoomFuncBean, ItemRoomFunctionSelectBinding> {

    private Map<String, Boolean> booleanMap = new HashMap<>();

    public Map<String, Boolean> getBooleanMap() {
        return booleanMap;
    }

    public RoomFunctionSelectAdapter(Context context) {
        super(context);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_room_function_select;
    }

    @Override
    public void setNewData(List<RoomTagListBean.RoomFuncBean> data) {
        super.setNewData(data);
        for (int i = 0; i < data.size(); i++) {
            booleanMap.put(data.get(i).getFunc(), data.get(i).isSelected());
        }

    }

    @Override
    protected void bind(BindingViewHolder<ItemRoomFunctionSelectBinding> holder, RoomTagListBean.RoomFuncBean item) {
        holder.binding.ivFunction.setSelected(booleanMap.get(item.getFunc()));
        ViewBindUtils.setText(holder.binding.tvFunctionName, item.getFunc_name());

    }
}
