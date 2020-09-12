package com.deepsea.mua.core.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * 支付宝支付
 * Created by JUN on 2018/12/26
 */
public class Alipay implements Handler.Callback {

    private static final int SDK_PAY_FLAG = 1;

    private Activity activity;
    private Handler mHandler;
    private AlipayListener listener;

    public Alipay(Activity activity) {
        this.activity = activity;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void startPay(final String orderInfo) {
        new Thread(() -> payV2(orderInfo)).start();
    }

    private void payV2(String orderInfo) {
        PayTask payTask = new PayTask(activity);
        Map<String, String> result = payTask.payV2(orderInfo, true);
        Log.d("Alipay", result.toString());

        Message msg = new Message();
        msg.what = SDK_PAY_FLAG;
        msg.obj = result;
        mHandler.sendMessage(msg);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == SDK_PAY_FLAG) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                if (listener != null) {
                    listener.onSuccess(payResult);
                }
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                if (listener != null) {
                    listener.onError(payResult.getMemo());
                }
            }
        }
        return false;
    }

    public void setAlipayListener(AlipayListener listener) {
        this.listener = listener;
    }

    public interface AlipayListener {
        void onSuccess(PayResult result);

        void onError(String msg);
    }
}
