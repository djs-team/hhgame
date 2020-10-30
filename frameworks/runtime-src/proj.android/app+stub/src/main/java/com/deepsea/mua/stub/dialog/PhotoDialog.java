package com.deepsea.mua.stub.dialog;

import android.Manifest;
import android.content.Intent;
import android.util.Log;

import com.deepsea.mua.core.dialog.BaseDialogFragment;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogPhotoBinding;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.DisposeUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by JUN on 2019/7/8
 */
public class PhotoDialog extends BaseDialogFragment<DialogPhotoBinding> {

    private OnPhotoSelectedListener mListener;

    public interface OnPhotoSelectedListener {
        void onSelected(String path);
    }

    private OnPhotoDismissListener dismissListener;

    public interface OnPhotoDismissListener {
        void onMyDismiss();
    }

    public void setDismissListener(OnPhotoDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_photo;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {
        mBinding.cameraTv.setOnClickListener(v -> {
            startCamera();
        });
        mBinding.photoTv.setOnClickListener(v -> {
            startPhoto();
        });
    }

    public void setOnPhotoSelectedListener(OnPhotoSelectedListener listener) {
        this.mListener = listener;
    }

    private void startCamera() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .as(DisposeUtils.autoDisposable(this))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        PictureSelector.create(getActivity())
                                .openCamera(PictureMimeType.ofImage())
                                .compress(true)// 是否压缩
                                .minimumCompressSize(500)// 小于500kb的图片不压缩
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                    }
                });
    }

    private void startPhoto() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .as(DisposeUtils.autoDisposable(this))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        PictureSelector.create(getActivity())
                                .openGallery(PictureMimeType.ofImage())
                                .theme(R.style.picture_QQ_style)
                                .enableCrop(false)// 是否裁剪
                                .compress(true)// 是否压缩
                                .maxSelectNum(1)
                                .minSelectNum(1)
                                .isGif(true)
                                .imageSpanCount(3)// 每行显示个数 int
                                .selectionMode(PictureConfig.SINGLE)
                                .circleDimmedLayer(false)//是否圆形裁剪
                                .previewImage(true)// 是否可预览图片
                                .minimumCompressSize(500)// 小于500kb的图片不压缩
                                .forResult(PictureConfig.CHOOSE_REQUEST);

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: ");

        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
            if (!CollectionUtils.isEmpty(list)) {
                if (mListener != null) {
                    mListener.onSelected(list.get(0).getPath());
                }
            }
        }
    }

    @Override
    public void dismiss() {
        if (dismissListener != null) {
            dismissListener.onMyDismiss();
        }
        super.dismiss();

    }
}
