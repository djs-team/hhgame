package com.deepsea.mua.stub.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.view.WithBackgroundTextView;

public class StateUtils {
    public static String getState(String state) {
        String stateMsg = "";
        switch (state) {
            case "1":
                stateMsg = "房间内";
                break;
            case "2":
                stateMsg = "等待相亲中";
                break;
            case "3":
                stateMsg = "相亲中";
                break;
        }
        return stateMsg;
    }

    public static void setRoomState(WithBackgroundTextView textView, String state) {
        if (!TextUtils.isEmpty(state)) {
            if (state.equals("2") || state.equals("3")) {
                textView.setVisibility(View.VISIBLE);
                textView.setText("相亲中");
            } else if (state.equals("4") || state.equals("5")) {
                textView.setVisibility(View.VISIBLE);
                textView.setText("热聊中");
            } else if (state.equals("6")) {
                textView.setVisibility(View.VISIBLE);
                textView.setText("开播中");
            } else {
                textView.setVisibility(View.GONE);
            }

        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public static void setState(WithBackgroundTextView textView, String str) {
        if (!TextUtils.isEmpty(str)) {
            textView.setVisibility(View.VISIBLE);
            if (str.contains("离线")) {
                textView.setWithBackgroundColor(R.color.state_color_grary);
                ViewBindUtils.setText(textView, str);
            } else if (str.contains("相亲")) {
                textView.setWithBackgroundColor(R.color.state_color_violet);
            } else if (str.contains("热聊")) {
                textView.setWithBackgroundColor(R.color.state_color_pink);
            } else if (str.contains("开播")) {
                textView.setWithBackgroundColor(R.color.state_color_yellow);
            } else {
                textView.setWithBackgroundColor(R.color.state_color_green);
            }
            textView.setText(str);

        } else {
            textView.setVisibility(View.GONE);
        }
    }

}
