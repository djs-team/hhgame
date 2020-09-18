package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.deepsea.mua.stub.entity.ReportListBean;
import com.deepsea.mua.stub.entity.ReportsBean;
import com.deepsea.mua.voice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2019/4/8
 */
public class RoomReportAdapter extends BaseQuickAdapter<ReportListBean, BaseViewHolder> {

    private List<Integer> list = new ArrayList<>();
    private int defultPos = 0;

    public int getDefultPos() {
        return defultPos;
    }

    public RoomReportAdapter(Context context, int layoutResId, List<ReportListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ReportListBean item) {
        Integer pos = helper.getAdapterPosition();
        helper.setText(R.id.report_tv, item.content);
        helper.setVisible(R.id.select_iv, defultPos == pos);

        helper.convertView.setOnClickListener(v -> {
            defultPos = pos;
//            if (list.contains(pos)) {
//                list.remove(pos);
//            } else {
//                list.add(pos);
//            }
            notifyDataSetChanged();
        });
    }

    public String getOptionId() {
        return getData().get(defultPos).id;
    }

    public String getOptionContent() {
        return getData().get(defultPos).content;
    }


}
