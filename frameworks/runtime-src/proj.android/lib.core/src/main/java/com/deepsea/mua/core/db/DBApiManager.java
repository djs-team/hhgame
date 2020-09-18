package com.deepsea.mua.core.db;


import com.deepsea.mua.core.db.impl.DBApiImpl;

/**
 * Created by tong on 16/5/4.
 */
public class DBApiManager {

    private static volatile IDBApi mDBApi = null;

    public static IDBApi getDBApi() {
        if (mDBApi == null) {
            synchronized (DBApiManager.class) {
                if (mDBApi == null) {
                    mDBApi = new DBApiImpl();
                }
            }
        }
        return mDBApi;
    }
}
