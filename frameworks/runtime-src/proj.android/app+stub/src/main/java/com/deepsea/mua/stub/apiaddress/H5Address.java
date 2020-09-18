package com.deepsea.mua.stub.apiaddress;

/**
 * Created by JUN on 2019/7/12
 */
public class H5Address extends BaseAddress {

    public String getRegisterProtocol() {//mua用户协议
        return AddressCenter.getAddress().getHostUrl() + "Public/Download/useragree.html";
    }

    public String getPrivacyPolicy() {//隐私协议
        return AddressCenter.getAddress().getHostUrl() + "Public/Download/privacy.html";
    }

    public String getReleaseDynamic() {//动态发布协议
        return AddressCenter.getAddress().getHostUrl() + "Public/Download/activity01.html";
    }

    public String getRechargeProtocol() {//充值协议
        return AddressCenter.getAddress().getHostUrl() + "Public/Download/charge.html";
    }

    public String getUserIdentityUrl() {
        return AddressCenter.getAddress().getHostUrl() + "index.php/home/userIdentity/identityIndex?token=";
    }
}
