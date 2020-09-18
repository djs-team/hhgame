package com.deepsea.mua.stub.glide;

import com.bumptech.glide.manager.ConnectivityMonitor;

/**
 * A no-op {@link com.bumptech.glide.manager.ConnectivityMonitor}.
 */
class NullConnectivityMonitor implements ConnectivityMonitor {

    @Override
    public void onStart() {
        // Do nothing.
    }

    @Override
    public void onStop() {
        // Do nothing.
    }

    @Override
    public void onDestroy() {
        // Do nothing.
    }
}
