package com.deepsea.mua.mine.activity;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.activity.InviteDialogActivity;
import com.deepsea.mua.mine.viewmodel.CollectionAccountModel;
import com.deepsea.mua.mine.viewmodel.InviteDialogViewModel;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.GalleryDialog;
import com.deepsea.mua.stub.dialog.InviteOutRoomDialog;
import com.deepsea.mua.stub.dialog.PhotoDialog;
import com.deepsea.mua.stub.entity.HeartBeatBean;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.OssGameConfigVo;
import com.deepsea.mua.stub.entity.ReportPicVo;
import com.deepsea.mua.stub.utils.AppManager;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.OssUpUtil;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.noober.background.BackgroundLibrary;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class UploadPhotoDialogActivity extends FragmentActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    AppExecutors mExecutors;
    private String loadPhoto = "";

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Inject
    ViewModelFactory mModelFactory;
    private CollectionAccountModel mViewModel;
    private Context mContext;
    OssGameConfigVo gameConfigVo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        mContext = this;
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(CollectionAccountModel.class);
        gameConfigVo = (OssGameConfigVo) getIntent().getSerializableExtra("ossConfig");
        Log.d("getPictureFrom", JsonConverter.toJson(gameConfigVo));
        showPhotoDialog();
    }


    private Handler mHandler = new Handler() {
    };


    /*
    上传图片证据 ---------------------------------------------------
*/
    PhotoDialog mPhotoDialog;

    private void showPhotoDialog() {
        if (mPhotoDialog == null) {
            mPhotoDialog = new PhotoDialog();
            mPhotoDialog.setDismissListener(new PhotoDialog.OnPhotoDismissListener() {
                @Override
                public void onMyDismiss() {
                    Log.d("dismiss", "dismiss-e");

                    UploadPhotoDialogActivity.this.finish();
                }
            });
            mPhotoDialog.setOnPhotoSelectedListener(path -> {
                Log.d("showPhotoDialog", path);
                upLoadHeadIv(path);
            });
        }
        mPhotoDialog.showAtBottom(getSupportFragmentManager());
    }

    private OSSAsyncTask ossAsyncTask;

    private void upLoadHeadIv(String aHeadIv) {
        mExecutors.diskIO().execute(() -> {
            OSS oSs = OssUpUtil.getInstance().getOssConfig(mContext, gameConfigVo.getAccessKeyIdOss(), gameConfigVo.getAccessKeySecretOss(), gameConfigVo.getToken(), gameConfigVo.getEndpoint());
            ossAsyncTask = OssUpUtil.getInstance().upToOss(6, aHeadIv, oSs, gameConfigVo.getBucketName(), gameConfigVo.getEndpoint(), new OssUpUtil.OssUpCallback() {
                @Override
                public void upSuccessFile(String objectKey) {
                    Log.d("report", "upSuccessFile:" + objectKey);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String path = "https://" + gameConfigVo.getBucketName() + "." + gameConfigVo.getEndpoint() + "/";
                            loadPhoto = path + objectKey;
                            mPhotoDialog.dismiss();
                        }
                    });
                }

                @Override
                public void upProgress(long progress, long zong) {

                }

                @Override
                public void upOnFailure() {

                    if (ossAsyncTask != null) {//取消上传任务
                        ossAsyncTask.cancel();
                        ossAsyncTask = null;
                    }
                    mHandler.post(() -> {
                        ToastUtils.showToast("上传失败");
                    });
                    mPhotoDialog.dismiss();
                }
            });
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPhotoDialog != null) {
            mPhotoDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void finish() {
        if (!TextUtils.isEmpty(loadPhoto)) {
            Intent intent = getIntent();
            intent.putExtra(Constant.HHGAME_PHOTO, loadPhoto);
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
