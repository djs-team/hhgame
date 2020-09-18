package com.deepsea.mua.stub.client.app;

import android.content.Context;

import com.deepsea.mua.stub.client.hyphenate.HyphenateClient;
import com.deepsea.mua.stub.utils.Function;
import com.deepsea.mua.stub.utils.UserUtils;

/**
 * Created by JUN on 2019/8/19
 */
public class AppClient extends IAppClient {

    private static volatile IAppClient mClient;

    public static IAppClient getInstance() {
        if (mClient == null) {
            synchronized (AppClient.class) {
                if (mClient == null) {
                    mClient = new AppClient();
                }
            }
        }
        return mClient;
    }

    private AppClient() {
    }

    @Override
    public void init(Context context) {
        HyphenateClient.getInstance().init(context);
    }

    @Override
    public void release() {
        HyphenateClient.getInstance().release();
    }

    @Override
    public boolean isRunning() {
        return mAppStatus == RUNNING && UserUtils.getUser() != null;
    }

    @Override
    public void login(String uid) {
        mAppStatus = RUNNING;
    }

    @Override
    public void logout(Function function) {
        mAppStatus = LOGOUT;
        function.apply(() -> {
            release();
            mAppStatus = NONE;
        });
    }
}
