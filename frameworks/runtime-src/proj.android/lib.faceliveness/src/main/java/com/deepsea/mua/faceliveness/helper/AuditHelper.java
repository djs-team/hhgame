package com.deepsea.mua.faceliveness.helper;

import android.content.Context;
import android.util.Log;

import com.alibaba.security.realidentity.RPEventListener;
import com.alibaba.security.realidentity.RPResult;
import com.alibaba.security.realidentity.RPVerify;
import com.alibaba.security.rp.RPSDK;
import com.deepsea.mua.faceliveness.listener.RPAuditListener;

/**
 * Created by JUN on 2019/7/19
 */
public class AuditHelper {

    public static void initialize(Context ctx) {
        RPVerify.init(ctx);
    }

    public static void start(String verifyToken, Context context, RPAuditListener listener) {
        RPVerify.start(context, verifyToken, new RPEventListener() {
            @Override
            public void onFinish(RPResult rpResult, String code, String msg) {
                Log.d("AuditHelper", code + ":" + msg);

                if (listener == null)
                    return;
                if (rpResult == RPResult.AUDIT_PASS) {
                    listener.onAuditPass();
                } else if (rpResult == RPResult.AUDIT_FAIL) {
                    listener.onAuditFail();
                } else if (rpResult == RPResult.AUDIT_NOT) {
                    listener.onAuditNot(code);

                }

            }
        });


    }
}
