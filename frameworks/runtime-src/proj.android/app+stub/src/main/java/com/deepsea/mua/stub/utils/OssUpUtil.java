package com.deepsea.mua.stub.utils;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 作者：liyaxing  2019/8/22 10:08
 * 类别 ： 阿里云上传
 */
public class OssUpUtil {

    public static OssUpUtil instance;
    private OSS oss;

    public OssUpUtil() {
    }

    public static OssUpUtil getInstance() {
        if (instance == null) {
            return new OssUpUtil();
        }
        return instance;
    }


    //首先在初始化页面的时候 初始化oss 服务器sdk
    public OSS getOssConfig(Context aContext, String accessKeyId, String accessKeySecret, String securityToken, String expiration) {
//        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken) {
//            @Override
//            public OSSFederationToken getFederationToken() {
//                LogUtils.i("upLoadImg--getFederationToken-" );
//                //可实现自动获取token
//                return super.getFederationToken();
//            }
//        };
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
//        OSSLog.enableLog();
//        LogUtils.i("upLoadImg--enableLog-");
        oss = new OSSClient(aContext, expiration, credentialProvider, conf);
//        LogUtils.i("upLoadImg--enableLog-11--" + expiration);
        return oss;
    }

    public OSSAsyncTask upToOss(int aType, String imgPath, OSS mOss, String bucketname, final OssUpUtil.OssUpCallback ossUpCallback) {
        String objectKey;
        if (aType == 4) {//身份证
            objectKey = "memberpid/" + UserUtils.getUser().getUid() + "/" + System.currentTimeMillis() + ".jpg";
        } else if (aType == 3) {//上传个人头像
            String path = UserUtils.getUser() == null ? "register" : UserUtils.getUser().getUid();
            objectKey = "Avatar/" + path + "/" + System.currentTimeMillis() + ".jpg";
        } else if (aType == 5) {//举报
            String path = UserUtils.getUser() == null ? "register" : UserUtils.getUser().getUid();
            objectKey = "MyReport/" + path + "/" + System.currentTimeMillis() + ".jpg";
        } else if (aType == 6) {//棋牌头像
            String path = UserUtils.getUser() == null ? "register" : UserUtils.getUser().getUid();
            objectKey = "GameAvatar/" + path + "/" + System.currentTimeMillis() + ".jpg";
        } else {
            objectKey = UserUtils.getUser().getUid() + "/" + System.currentTimeMillis() + (aType == 0 ? ".jpg" : ".amr");
        }

        /**
         * aType  0 图片  1 录音     3 个人头像 4身份证
         * bucketname 存储空间，你上传到哪个空间里面去
         * objectKey：上传后的文件名
         * uploadFilePath：本地文件（需要上传的文件）路径
         */
        PutObjectRequest put = new PutObjectRequest(bucketname, objectKey, imgPath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                LogUtils.i("upLoadImg", "当前大小: " + currentSize + " 总大小: " + totalSize);
                ossUpCallback.upProgress(currentSize, totalSize);
            }
        });
        OSSAsyncTask task = mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                //1110836101163991040/1566443276752   返回的
                //https://kkorange-read.oss-cn-beijing.aliyuncs.com/1110836101163991040/1566443276752  传给后台，后台拼接好后就是url链接
//                LogUtils.i("upLoadImg", "UploadSuccess");
//                LogUtils.i("ETag", result.getETag());
//                LogUtils.i("RequestId", result.getRequestId());
                ossUpCallback.upSuccessFile(objectKey);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                ossUpCallback.upOnFailure();
            }
        });
        // task.cancel(); // 可以取消任务
        task.waitUntilFinished(); // 可以等待直到任务完成
        return task;
    }

    public interface OssUpCallback {
        void upSuccessFile(String objectKey);

        void upProgress(long progress, long zong);

        void upOnFailure();
    }


    /**
     * requestGet
     *
     * @param strUrl
     * @param param
     * @param appcode
     * @return
     */
    public static String requestGet(String strUrl, String param, String appcode) {

        String returnStr = null; // 返回结果定义
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(strUrl + "?" + param);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET"); // get方式
            httpURLConnection.setUseCaches(false); // 不用缓存
            httpURLConnection.connect();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            returnStr = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }


}


