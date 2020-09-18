package com.deepsea.mua.mine.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemProfileEditBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.ProfileModel;

/**
 * Created by JUN on 2019/10/18
 */
public class ProfileEditAdapter extends BaseBindingAdapter<ProfileModel.ProfileMenu, ItemProfileEditBinding> {

    private OnItemClickListener mListener;

    public ProfileEditAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_profile_edit;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void bind(BindingViewHolder<ItemProfileEditBinding> holder, ProfileModel.ProfileMenu item) {
        holder.setVisible(holder.binding.menusTitle, !TextUtils.isEmpty(item.getMenuName()));
        holder.binding.menusTitle.setText(item.getMenuName());
        holder.binding.menuNameTv.setText(item.getType_name());
        holder.binding.menuTv.setTextColor(TextUtils.isEmpty(item.getName()) ? 0xFFFF4767 : 0xFF333333);
        if (TextUtils.equals(item.getMold(), "4")) {//输入框
            holder.binding.menuTv.setText(item.getName());
        } else if (TextUtils.equals(item.getMold(), "2")) {//数字区间
            String text = TextUtils.isEmpty(item.getName()) ? "请选择" : item.getName();
            if (text.contains(",")) {
                String[] split = text.split(",");
                if (TextUtils.equals(split[0], split[1])) {
                    holder.binding.menuTv.setText(split[0]);
                } else {
                    holder.binding.menuTv.setText(text.replace(",", "-"));
                }
            } else {
                holder.binding.menuTv.setText(text);
            }
        } else {
            holder.binding.menuTv.setText(TextUtils.isEmpty(item.getName()) ? "请选择" : item.getName());
        }

        holder.binding.itemLayout.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }
}
