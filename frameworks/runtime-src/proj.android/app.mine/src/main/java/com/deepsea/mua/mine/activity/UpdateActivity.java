package com.deepsea.mua.mine.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.deepsea.mua.mine.dialog.UpdateDialog;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.DisposeUtils;
import com.noober.background.BackgroundLibrary;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Created by JUN on 2019/7/4
 */
@Route(path = ArouterConst.PAGE_UPDATE)
public class UpdateActivity extends FragmentActivity {

    private UpdateDialog mUpdateDialog;

    @Autowired
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);

        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .as(DisposeUtils.autoDisposable(this))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        showUpdateDialog();
                    }
                });
    }

    private void showUpdateDialog() {
        if (mUpdateDialog == null) {
            mUpdateDialog = new UpdateDialog(this);
        }
        mUpdateDialog.setUpdateUrl(url);
        mUpdateDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (mUpdateDialog != null) {
            mUpdateDialog.dismiss();
            mUpdateDialog = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10000) {
            if (mUpdateDialog != null) {
                mUpdateDialog.checkPermissionAndInstall();
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == 10000) {
            if (mUpdateDialog != null) {
                mUpdateDialog.resetViewStatus();
            }
        }
    }
}
