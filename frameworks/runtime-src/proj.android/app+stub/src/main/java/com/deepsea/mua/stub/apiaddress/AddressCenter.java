package com.deepsea.mua.stub.apiaddress;

/**
 * Created by JUN on 2019/7/12
 */
public class AddressCenter {
    private static Address mAddress = null;
    private static final Object mLock = new Object();

    public static Address getAddress() {
        if (mAddress == null) {
            synchronized (mLock) {
                if (mAddress == null) {
                    mAddress = new Address();
                }
            }
        }
        return mAddress;
    }
}
