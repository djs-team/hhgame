package com.deepsea.mua.stub.utils;

import android.graphics.Color;
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
            if (str.contains("离线") || str.contains("刚刚在线")) {
                textView.setWithBackgroundColor(Color.parseColor("#b5b5b6"));
                ViewBindUtils.setText(textView, str);
            } else if (str.contains("相亲")) {
                textView.setWithBackgroundColor(Color.parseColor("#7F3EF0"));
            } else if (str.contains("热聊")) {
                textView.setWithBackgroundColor(Color.parseColor("#EF51B2"));
            } else if (str.contains("开播")) {
                textView.setWithBackgroundColor(Color.parseColor("#FEBF00"));
            } else {
                textView.setWithBackgroundColor(Color.parseColor("#10E770"));
            }
            textView.setText(str);

        } else {
            textView.setVisibility(View.GONE);
        }
    }
    public static void setOnlineState(TextView textView, String str) {
        if (!TextUtils.isEmpty(str)) {
            textView.setVisibility(View.VISIBLE);
            if (str.contains("离线") || str.contains("刚刚在线")) {
                textView.setTextColor(Color.parseColor("#b5b5b6"));
                ViewBindUtils.setText(textView, str);
            } else if (str.contains("相亲")) {
                textView.setTextColor(Color.parseColor("#7F3EF0"));
            } else if (str.contains("热聊")) {
                textView.setTextColor(Color.parseColor("#EF51B2"));
            } else if (str.contains("开播")) {
                textView.setTextColor(Color.parseColor("#FEBF00"));
            } else {
                textView.setTextColor(Color.parseColor("#10E770"));
            }
            textView.setText(str);

        } else {
            textView.setVisibility(View.GONE);
        }
    }

}
