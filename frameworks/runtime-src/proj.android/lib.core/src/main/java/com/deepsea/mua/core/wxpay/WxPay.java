package com.deepsea.mua.core.wxpay;

import android.content.Context;
import android.content.IntentFilter;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by JUN on 2018/12/27
 */
public class WxPay {

    private IWXAPI iwxapi;
    private WXPayBuilder builder;
    private Context mContext;

    public WxPay(WXPayBuilder builder) {
        this.builder = builder;
    }

    /**
     * 调起微信支付的方法
     *
     * @param context
     **/
    public void startPay(Context context) {
        this.mContext = context;
        iwxapi = WXAPIFactory.createWXAPI(mContext, null);
        iwxapi.registerApp(builder.getAppId());

        Runnable payRunnable = () -> {
            PayReq request = new PayReq();
            request.appId = builder.getAppId();
            request.partnerId = builder.getPartnerId();
            request.prepayId = builder.getPrepayId();
            request.packageValue = builder.getPackageValue();
            request.nonceStr = builder.getNonceStr();
            request.timeStamp = builder.getTimeStamp();
            request.sign = builder.getSign();
            iwxapi.sendReq(request);//发送调起微信的请求
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void registerWxpayResult(WxpayBroadcast.WxpayReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WxCons.ACTION);
        mContext.registerReceiver(receiver, filter);
    }

    public void unregisterWxpayResult(WxpayBroadcast.WxpayReceiver receiver) {
        try {
            mContext.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class WXPayBuilder {
        //应用ID
        private String appId;
        //商户号
        private String partnerId;
        //预支付交易会话ID
        private String prepayId;
        //扩展字段 (暂填写固定值Sign=WxPay)
        private String packageValue;
        //随机字符串
        private String nonceStr;
        //时间戳
        private String timeStamp;
        //签名
        private String sign;

        public WxPay build() {
            return new WxPay(this);
        }

        public String getAppId() {
            return appId;
        }

        public WXPayBuilder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public WXPayBuilder setPartnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public WXPayBuilder setPrepayId(String prepayId) {
            this.prepayId = prepayId;
            return this;
        }

        public String getPackageValue() {
            return packageValue;
        }

        public WXPayBuilder setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public WXPayBuilder setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public WXPayBuilder setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public String getSign() {
            return sign;
        }

        public WXPayBuilder setSign(String sign) {
            this.sign = sign;
            return this;
        }
    }

}
