package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityInvitationCodeBinding;
import com.deepsea.mua.mine.viewmodel.InviteCodeModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.InviteCodeBean;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;


public class InvitationCodeActivity extends BaseActivity<ActivityInvitationCodeBinding> {
    @Inject
    ViewModelFactory mModelFactory;
    private InviteCodeModel mViewModel;
    private String dowonloadUrl = "";
    private String dowonloadMsg = "";
    private Bitmap bitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invitation_code;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(InviteCodeModel.class);
        User user = UserUtils.getUser();
        if (user != null) {
            String nickName = user.getNickname();
            String photoUrl = user.getAvatar();
            ViewBindUtils.setText(mBinding.tvInviteTitle, nickName + "邀请您体验合合交友");
            GlideUtils.circleImage(mBinding.ivUserhead, photoUrl, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        }
        getInviteDownUrl();

    }

    private void getInviteDownUrl() {
        showProgress();
        mViewModel.getInviteDownUrl().observe(this, new BaseObserver<InviteCodeBean>() {
            @Override
            public void onSuccess(InviteCodeBean result) {
                hideProgress();
                if (result != null) {
                    ViewBindUtils.setText(mBinding.tvMyInvitecode, result.getReferrercode());
                    dowonloadUrl = result.getUrl();
                    Bitmap mBitmap = CodeUtils.createImage(dowonloadUrl, 400, 400, null);
                    ViewBindUtils.setImageBitmap(mBinding.ivQrCode, mBitmap);
                    copyByCanvas(mBinding.rlQrInfo);
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                dowonloadMsg = msg;
                hideProgress();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void copyByCanvas(View v) {
        v.post(new Runnable() {
            @Override
            public void run() {
                v.setDrawingCacheEnabled(true);
                v.buildDrawingCache();
                Bitmap bp = v.getDrawingCache();
                bitmap = bp;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        ViewBindUtils.RxClicks(mBinding.tvShare, o -> {
            if (TextUtils.isEmpty(dowonloadUrl)) {
                toastShort(dowonloadMsg);
                return;
            }
            Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "分享到"));
            shareCallback();
        });
    }

    private void shareCallback() {
        mViewModel.shareCallback().observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {

            }
        });
    }

}
