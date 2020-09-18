package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityQueryDateDetailsBinding;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.utils.TimeUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import cn.addapp.pickers.picker.DatePicker;

import static com.deepsea.mua.core.utils.ToastUtils.showToast;

/**
 * 明细按日期查询
 */
public class QueryDateDetailsActivity extends BaseActivity<ActivityQueryDateDetailsBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_query_date_details;
    }

    @Override
    protected void initView() {
        startTimeStr = getIntent().getStringExtra("sTime");
        endTimeStr = getIntent().getStringExtra("eTime");
        String f_sTime = TimeUtils.formatTime(startTimeStr);
        String f_eTime = TimeUtils.formatTime(endTimeStr);
        startTime = TimeUtils.formatTime2(f_sTime);
        endTime = TimeUtils.formatTime2(f_eTime);
        ViewBindUtils.setText(mBinding.tvDateStart, f_sTime);
        ViewBindUtils.setText(mBinding.tvDateEnd, f_eTime);
    }

    private int startTime = 0;
    private int endTime = 0;
    private String startTimeStr = "";
    private String endTimeStr = "";

    @Override
    protected void initListener() {
        super.initListener();
        //提交
        subscribeClick(mBinding.tvSubmit, o -> {
            boolean checked = checkDate(true);
            if (checked) {
                Intent intent = new Intent();
                intent.putExtra("startDate", startTimeStr);
                intent.putExtra("endDate", endTimeStr);
                setResult(Activity.RESULT_OK, intent);
                QueryDateDetailsActivity.this.finish();
            }
        });
        //开始日期
        subscribeClick(mBinding.llDateStart, o -> {
            onYearMonthDayPicker("start");
        });
        //结束日期
        subscribeClick(mBinding.llDateEnd, o -> {
            onYearMonthDayPicker("end");
        });
    }

    public void onYearMonthDayPicker(String flag) {
        final DatePicker picker = new DatePicker(this);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setRangeStart(2016, 8, 29);
        picker.setRangeEnd(2111, 1, 11);
        picker.setSelectedItem(TimeUtils.getSysYear(), TimeUtils.getSysMonth(), TimeUtils.getSysDay());
        picker.setWeightEnable(true);
        picker.setLineColor(Color.BLACK);
        picker.setCancelTextColor(Color.parseColor("#343434"));
        picker.setCancelTextSize(16);
        picker.setSubmitTextColor(Color.parseColor("#343434"));
        picker.setSubmitTextSize(16);
        picker.setSelectedTextColor(Color.parseColor("#983A7E"));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                if (flag.equals("start")) {
                    startTime = Integer.valueOf(year + month + day);
                    startTimeStr = year + "-" + month + "-" + day;
                    ViewBindUtils.setText(mBinding.tvDateStart, year + "年" + month + "月" + day + "日");
                } else {
                    endTimeStr = year + "-" + month + "-" + day;
                    endTime = Integer.valueOf(year + month + day);
                    ViewBindUtils.setText(mBinding.tvDateEnd, year + "年" + month + "月" + day + "日");
                }
                checkDate(false);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    private boolean checkDate(boolean isSubmit) {
        boolean isEnableCheck = false;
        if (isSubmit && (startTime == 0 || endTime == 0)) {
            showToast("请选择要查询的开始日期和结束日期");
            return false;
        }
        if (endTime != 0 && startTime > endTime) {
            showToast("开始日期不能大于结束日期，请重新选择");
            isEnableCheck = false;

        } else {
            isEnableCheck = true;
        }
        return isEnableCheck;
    }
}
