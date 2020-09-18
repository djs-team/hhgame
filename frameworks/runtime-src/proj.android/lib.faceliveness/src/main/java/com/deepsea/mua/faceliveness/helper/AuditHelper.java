package com.deepsea.mua.faceliveness.helper;

import android.content.Context;

import com.alibaba.security.rp.RPSDK;
import com.deepsea.mua.faceliveness.listener.RPAuditListener;

/**
 * Created by JUN on 2019/7/19
 */
public class AuditHelper {

    public static void initialize(Context ctx) {
        RPSDK.initialize(ctx);
    }

    public static void start(String verifyToken, Context context, RPAuditListener listener) {
        RPSDK.start(verifyToken, context, (audit, code) -> {
            if (listener == null)
                return;

            switch (audit) {
                case AUDIT_PASS:  //认证通过
                    listener.onAuditPass();
                    break;
                case AUDIT_FAIL:  //认证不通过
                    listener.onAuditFail();
                    break;
                    case AUDIT_NOT:  //未认证，用户取消
                    listener.onAuditNot(code);
                    break;
            }
        });
    }
}
