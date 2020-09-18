package com.deepsea.mua.mine.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogUpdateBinding;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.network.download.DownloadListener;
import com.deepsea.mua.stub.network.download.DownloadUtils;
import com.deepsea.mua.stub.utils.CacheUtils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by JUN on 2019/7/4
 */
public class UpdateDialog extends BaseDialog<DialogUpdateBinding> {

    private String url;
    private static final String fileName = "update.apk";

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public UpdateDialog(@NonNull Context context) {
        super(context);
        setCancelable(false);
//        mBinding.progressbar.setOnProgressUpdatedListener(progress -> mBinding.percentTv.setText(progress + "%"));
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_update;
    }

    public void setUpdateUrl(String url) {
        this.url = url;
    }

    @Override
    protected void initListener() {
        mBinding.ensureBtn.setOnClickListener(v -> {
//            mBinding.ensureBtn.setVisibility(View.GONE);
////            mBinding.progressbar.setVisibility(View.VISIBLE);
//            mBinding.progressRl.setVisibility(View.VISIBLE);
            startJump();
        });
    }

    private void startJump() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        //如果info.activityInfo.packageName为android,则没有设置,否则,有默认的程序.
        if (!hasPreferredApplication(mContext, intent)) {
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        }
        mContext.startActivity(intent);


    }

    //判断系统是否设置了默认浏览器
    public static boolean hasPreferredApplication(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !"android".equals(info.activityInfo.packageName);
    }

    private String getFilePath() {
        return CacheUtils.getAppDownLoadDir() + File.separator + fileName;
    }

    private void startDownload() {
        DownloadUtils.download(url, getFilePath(), new DownloadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long total, long current) {
                mHandler.post(() -> onLoading(total, current));
            }

            @Override
            public void onFinish(String path) {
                mHandler.post(() -> successFile(new File(path)));
            }

            @Override
            public void onFail(String error) {
                mHandler.post(() -> errorDownload());
            }
        });
    }

    private void onLoading(long total, long current) {
        if (total == 0 || current == 0) {
            return;
        }
        final DecimalFormat def = new DecimalFormat("0.0");
        float percent = current / 1024F / 1024F;
        int progress = Math.abs((int) ((float) current / (float) total * 100));
//        mBinding.progressbar.setAnimationProgress(progress);
        mBinding.progressTv.setText("正在下载:\n" + def.format(percent) + "MB/" + def.format(total / 1024F / 1024F) + "MB");
    }

    private void errorDownload() {
        ToastUtils.showToast("下载失败");
        resetViewStatus();
    }

    public void resetViewStatus() {
        mBinding.ensureBtn.setVisibility(View.VISIBLE);
        mBinding.progressRl.setVisibility(View.GONE);
//        mBinding.progressbar.setVisibility(View.GONE);
    }

    private void successFile(final File result) {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = mContext.getPackageManager().canRequestPackageInstalls();
            if (b) {
                try {
                    ToastUtils.showToast("请在安装界面点击“安装”完成更新！");
                    setup(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                resetViewStatus();
            } else {
                AAlertDialog dialog = new AAlertDialog(mContext);
                dialog.setMessage("安装应用需要打开未知来源权限，请点击前往开启权限", R.color.black);
                dialog.setButton("前往", R.color.black, (v, dialog1) -> {
                    Uri packageURI = Uri.parse("package:" + mContext.getPackageName());
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                    ((Activity) mContext).startActivityForResult(intent, 10000);
                    dialog1.dismiss();
                });
                dialog.show();
            }
        } else {
            try {
                ToastUtils.showToast("请在安装界面点击“安装”完成更新！");
                setup(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resetViewStatus();
        }
    }

    public void setup(File f) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data;
        if (Build.VERSION.SDK_INT >= 24) { //Build.VERSION_CODES.N = 24
            data = FileProvider.getUriForFile(mContext, mContext.getApplicationInfo().packageName + ".host.fileprovider", f);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(f);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public void checkPermissionAndInstall() {
        File apkFile = new File(getFilePath());
        if (apkFile.exists()) {
            successFile(apkFile);
        }
    }
}
