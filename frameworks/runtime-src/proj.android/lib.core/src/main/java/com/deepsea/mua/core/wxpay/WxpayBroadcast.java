package com.deepsea.mua.core.wxpay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by JUN on 2019/4/16
 */
public class WxpayBroadcast {

    public static void sendWxpayResult(Context context, int result) {
        Intent intent = new Intent(WxCons.ACTION);
        intent.putExtra(WxCons.PAY_RESULT, result);
        context.sendBroadcast(intent);
    }

    public abstract static class WxpayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (WxCons.ACTION.equals(intent.getAction())) {
                int result = intent.getIntExtra(WxCons.PAY_RESULT, -1);
                if (result == WxCons.STATE_SUCCESS) {
                    onSuccess();
                } else {
                    onError();
                }
            }
        }

        public abstract void onSuccess();

        public abstract void onError();
    }
}
