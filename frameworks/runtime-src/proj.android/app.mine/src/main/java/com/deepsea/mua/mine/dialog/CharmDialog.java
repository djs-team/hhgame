package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogCharmBinding;
import com.deepsea.mua.mine.databinding.ItemCharmBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.callback.CommonCallback;
import com.deepsea.mua.stub.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by JUN on 2019/10/18
 */
public class CharmDialog extends BaseDialog<DialogCharmBinding> {

    private CharmAdapter mAdapter;

    private CommonCallback<String> mCallback;

    public CharmDialog(@NonNull Context context) {
        super(context);
        initRecyclerView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_charm;
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
                mCallback.onSuccess(mAdapter.getSelect());
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new CharmAdapter(mContext);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    public void setData(List<String> list, String def) {
        mAdapter.setCharmDef(def);
        mAdapter.setNewData(list);
    }

    public CharmDialog setTitle(String title) {
        mBinding.titleTv.setText(title);
        return this;
    }

    public void setEnsureCallback(CommonCallback<String> callback) {
        mCallback = callback;
    }

    private static class CharmAdapter extends BaseBindingAdapter<String, ItemCharmBinding> {

        private List<String> list = new ArrayList<>();

        private final int max = 5;

        public CharmAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.item_charm;
        }

        public String getSelect() {
            if (!CollectionUtils.isEmpty(list)) {
                StringBuilder sb = new StringBuilder();
                for (String s : list) {
                    sb.append(s).append(",");
                }
                return sb.toString().substring(0, sb.toString().length() - 1);
            }
            return "";
        }

        public void setCharmDef(String def) {
            list.clear();
            if (!TextUtils.isEmpty(def)) {
                String[] split = def.split(",");
                list.addAll(Arrays.asList(split));
            }
        }

        @Override
        protected void bind(BindingViewHolder<ItemCharmBinding> holder, String item) {
            holder.binding.selectIv.setImageResource(list.contains(item) ? R.drawable.ic_selected : R.drawable.ic_unselected);
            holder.binding.selectTv.setText(item);
            holder.itemView.setOnClickListener(v -> {
                boolean contains = list.contains(item);
                if (contains) {
                    list.remove(item);
                } else {
                    if (list.size() == max) {
                        String remove = list.remove(0);
                        notifyItemChanged(mData.indexOf(remove));
                    }

                    list.add(item);
                }
                notifyItemChanged(holder.getAdapterPosition());
            });
        }
    }
}
